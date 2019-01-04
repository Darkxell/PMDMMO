#if defined(WIN32) || defined(_WIN32) || defined(__WIN32) && !defined(__CYGWIN__)
#define IS_WIN
#endif

#ifdef IS_WIN
#define SETUP_SCRIPT ":/static/setup.ps1"
#else
#define SETUP_SCRIPT ":/static/setup.sh"
#endif

#include <QDebug>

#include <QApplication>
#include <QFile>
#include <QProcess>
#include <QSplashScreen>
#include <QTemporaryFile>

/**
 * @brief exec_setup Execute shell script.
 * @return If the process has
 */
bool exec_setup() {
    QFile file(SETUP_SCRIPT);
    QTemporaryFile *tmp = QTemporaryFile::createNativeFile(file);
    QString tmp_path = tmp->fileName();

    QProcess process;

#ifdef IS_WIN
    // powershell only works if the extension is ps1
    // because of course it does
    tmp_path += ".ps1";
    tmp->rename(tmp_path);

    process.start(
        "powershell.exe",
        QStringList() << "-NonInteractive" << "-NoLogo" << "-NoProfile"
            << "-File" << tmp_path
    );
#else
    // TODO: test on macos and linux (at least ubuntu)
    process.start("/bin/sh", QStringList() << tmp_path);
#endif

    tmp->remove();
    return process.waitForFinished();
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

    if (!exec_setup()) {
        // TODO: error box message instead of crash
        qFatal("Error in setup.");
    }

    splash.finish(nullptr);

    return 0;
}
