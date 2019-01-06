#include <QApplication>
#include <QErrorMessage>
#include <QFile>
#include <QSplashScreen>
#include <QStandardPaths>

#include "PMDOSplashScreen.h"
#include "constants.h"

#include "log.h"
#include "util.h"

PMDOSplashScreen::PMDOSplashScreen(QPixmap image) :
    QSplashScreen(image) {}

/**
 * @brief exec_setup Execute shell script.
 * @return Return code from script.
 */
int exec_setup(QString work_path, QString script_path) {
    QProcess process;
    process.setProcessChannelMode(QProcess::MergedChannels);
    process.setWorkingDirectory(work_path);

#ifdef Q_OS_WIN
    process.start("powershell.exe",
                  QStringList() << "-NonInteractive"
                    << "-NoLogo"
                    << "-NoProfile"
                    << "-File" << script_path
    );
#else
    // TODO: test on macos and linux (at least ubuntu)
    process.start("/bin/sh", QStringList() << script_path);
#endif

    QEventLoop q;
    QProcess::connect(&process,
                      SIGNAL(finished(int, QProcess::ExitStatus)),
                      &q, SLOT(quit()));
    q.exec();
    // TODO: add incremental output reads
    int status = process.exitCode();
    flush_logs(process);
    return status;
}

bool PMDOSplashScreen::load() {
    this->show();

    QString data_path = QStandardPaths::standardLocations(
        QStandardPaths::AppDataLocation)[0];
    QString script_path = copy_tmp(Constants::SetupScript);

    touch_dir(data_path);

    int status = exec_setup(data_path, script_path);
    QFile::remove(script_path);

    this->finish(nullptr);

    return status;
}
