#include <QDateTime>
#include <QDir>
#include <QFile>
#include <QProcess>
#include <QString>

#include "log.h"

#include "constants.h"

#include "util.h"

QString create_log_name() {
    QDateTime now = QDateTime::currentDateTime();
    QString timestamp = now.toString(Constants::LogTimestamp);
    return QString(Constants::LogPrefix) + "-" + timestamp;
}

void write_logs(QString dir, QString path, QByteArray output, QString ext) {
    if (output.trimmed().isEmpty()) return;

    touch_dir(dir);
    QFile log(dir + "/" + path + ext);
    if (log.open(QIODevice::WriteOnly)) {
        log.write(output);
    }
}

void flush_logs(QProcess &process) {
    QString logs_path = process.workingDirectory() + "/" + Constants::LogsDir;
    QString log_name = create_log_name();

    write_logs(logs_path, log_name, process.readAllStandardOutput(), ".log");
    write_logs(logs_path, log_name, process.readAllStandardError(), ".err");
}
