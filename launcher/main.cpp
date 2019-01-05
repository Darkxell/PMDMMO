#if defined(WIN32) || defined(_WIN32) || defined(__WIN32) && !defined(__CYGWIN__)
#define IS_WIN
#endif

#define LOGS_PATH "logs"

#ifdef IS_WIN
#define SETUP_SCRIPT ":/static/setup.ps1"
#else
#define SETUP_SCRIPT ":/static/setup.sh"
#endif

#include <QApplication>
#include <QDateTime>
#include <QDir>
#include <QErrorMessage>
#include <QFile>
#include <QProcess>
#include <QSplashScreen>
#include <QStandardPaths>
#include <QTemporaryFile>

QString copy_tmp(QString resource) {
    QFile file(resource);
    QTemporaryFile *tmp = QTemporaryFile::createNativeFile(file);
    QString tmp_name = tmp->fileName();

#ifdef IS_WIN
    // powershell only works if the extension is ps1
    // because of course it does
    tmp_name += ".ps1";
    tmp->rename(tmp_name);
#endif

    return tmp_name;
}

QString create_log_name() {
    QDateTime now = QDateTime::currentDateTime();
    QString timestamp = now.toString("yyyy-MM-dd-hh-mm-ss.zzz");
    return "setup-" + timestamp;
}

void touch_dir(QString path) {
    QDir dir(path);
    if (!dir.exists()) {
        dir.mkpath(".");
    }
}

void flush_logs(QProcess *process) {
    auto sep = QDir::separator();
    QString logs_path = process->workingDirectory() + sep + LOGS_PATH;
    touch_dir(logs_path);

    QString log_name = logs_path + sep + create_log_name();

    QFile log(log_name + ".log");
    QFile err(log_name + ".err");

    if (log.open(QIODevice::WriteOnly)) {
        log.write(process->readAllStandardOutput());
    }

    if (err.open(QIODevice::WriteOnly)) {
        log.write(process->readAllStandardError());
    }
}

/**
 * @brief exec_setup Execute shell script.
 * @return Return code from script.
 */
int exec_setup(QString work_path, QString script_path) {
    QProcess process;
    process.setProcessChannelMode(QProcess::MergedChannels);
    process.setWorkingDirectory(work_path);

#ifdef IS_WIN
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
    if (status != QProcess::NormalExit) {
        flush_logs(&process);
    }
    return status;
}

int attempt_load() {
    QString data_path = QStandardPaths::standardLocations(
        QStandardPaths::AppDataLocation)[0];
    QString script_path = copy_tmp(SETUP_SCRIPT);

    touch_dir(data_path);

    int status = exec_setup(data_path, script_path);
    QFile::remove(script_path);
    return status;
}

/**
 * @brief code_to_reason Convert code to reason.
 * @param code Code returned from script.
 * @return Message explaining error.
 */
QString code_to_reason(int code) {
    switch(code) {
    case 1:
        return "JRE 1.8 or higher must be available.";
    default:
        return "Unknown error.";
    }
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

    QPixmap image(":/static/placeholder.png");
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
