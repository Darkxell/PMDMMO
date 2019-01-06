#include <QDir>
#include <QString>
#include <QTemporaryFile>

#include "util.h"

#include "constants.h"

QString copy_tmp(QString resource) {
    QFile file(resource);
    QTemporaryFile *tmp = QTemporaryFile::createNativeFile(file);
    QString tmp_name = tmp->fileName();

#ifdef Q_OS_WIN
        // powershell only works if the extension is ps1
        // because of course it does
        tmp_name += ".ps1";
        tmp->rename(tmp_name);
#endif

    return tmp_name;
}

void touch_dir(QString path) {
    QDir dir(path);
    if (!dir.exists()) {
        dir.mkpath(".");
    }
}

/**
 * @brief code_to_reason Convert code to reason.
 * @param code Code returned from script.
 * @return Message explaining error.
 */
QString code_to_reason(int code) {
    switch(code) {
    case 1:
        return "Something is wrong with the script.";
    case 2:
        return "JRE 1.8 or higher must be available.";
    default:
        return "Unknown error" + QString::number(code) + ".";
    }
}
