#include <QApplication>
#include <QErrorMessage>
#include <QFile>
#include <QSplashScreen>
#include <QStandardPaths>

#include "constants.h"

#include "log.h"
#include "util.h"

/**
 * @brief exec_setup Execute shell script.
 * @return Return code from script.
 */
int exec_setup(QString work_path, QString script_path) {
    QProcess process;
    process.setProcessChannelMode(QProcess::MergedChannels);
    process.setWorkingDirectory(work_path);

#ifdef Q_OS_WIN
    process.start(
        "powershell.exe",
        QStringList() << "-NonInteractive" << "-NoLogo" << "-NoProfile"
            << "-File" << script_path
    );
#else
    // TODO: test on macos and linux (at least ubuntu)
    process.start("/bin/sh", QStringList() << script_path);
#endif

    process.waitForFinished(-1);
    int status = process.exitCode();
    flush_logs(process);
    return status;
}

int attempt_load() {
    QString data_path = QStandardPaths::standardLocations(
        QStandardPaths::AppDataLocation)[0];
    QString script_path = copy_tmp(Constants::SetupScript);

    touch_dir(data_path);

    int status = exec_setup(data_path, script_path);
    QFile::remove(script_path);
    return status;
}

/**
 * Basically a fancy shell script.
 *
 * See setup.ps1 and setup.sh for windows and unix systems for more
 * implementation details.
 */
int main(int argc, char *argv[]) {
    // qt must include a QtGuiApplication before any widgets are allowed to
    // be shown, but we don't actually use any qt stuff here.
    QApplication dummy_app(argc, argv);

    QPixmap image(Constants::PlaceholderImage);
    QSplashScreen splash(image);

    splash.show();

    int status = attempt_load();
    if (status != QProcess::NormalExit) {
        QErrorMessage error(&splash);
        error.showMessage("<p>Error setting up:</p>"
                          "<p>" + code_to_reason(status) + "</p>");
        error.exec();
    }

    splash.finish(nullptr);

    return 0;
}
