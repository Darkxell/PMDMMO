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
 * @return If the script was successful (or at least unsuccessful in a known
 * way).
 */
bool exec_setup(QString work_path, QString script_path) {
    QProcess process;
    process.setProcessChannelMode(QProcess::MergedChannels);

    touch_dir(work_path);
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
    bool success = process.exitCode() == QProcess::NormalExit;
    if (!success) {
        flush_logs(&process);
    }
    return success;
}

bool attempt_load() {
    QStringList data_paths = QStandardPaths::standardLocations(
        QStandardPaths::AppDataLocation);

    QString script_path = copy_tmp(SETUP_SCRIPT);
    bool success = false;
    for (QString data_path : data_paths) {
        if (exec_setup(data_path, script_path)) {
            success = true;
            break;
        }
    }
    QFile::remove(script_path);
    return success;
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

    if (!attempt_load()) {
        // TODO: error box message instead of crash
        qFatal("Error setting up. Check error logs for details.");
    }

    splash.finish(nullptr);

    return 0;
}
