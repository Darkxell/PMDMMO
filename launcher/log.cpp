#include <QDateTime>
#include <QDir>
#include <QFile>
#include <QProcess>
#include <QString>

#include <QDebug>

#include "log.h"

#include "constants.h"

#include "util.h"

/**
 * @brief write_logs Write logs to disk.
 *
 * @param dir Log file directory.
 * @param output Log data.
 * @param ext Log extension.
 * @return Log file size.
 */
qint64 write_logs(QString dir, QByteArray output, QString ext) {
    QFile log(dir + "/" + Constants::LogPrefix + ext);

    if (!output.trimmed().isEmpty()) {
        touch_dir(dir);

        if (log.open(QIODevice::Append)) {
            log.write(output);
        }
    }
    return log.size();
}

QString create_rotated_path(QFileInfo &file) {
    QString name = file.baseName();
    QString ext = file.completeSuffix();

    QDateTime now = QDateTime::currentDateTime();
    QString timestamp = now.toString(Constants::LogTimestamp);

    return file.path() + "/" + name + "-" + timestamp + "." + ext;
}

void rotate_logs(QString logs_path, int steps) {
    qInfo() << steps;
    QDir logs_dir(logs_path);
    QFileInfoList logs = logs_dir.entryInfoList(QDir::Files, QDir::Time);

    // move the {steps} most recently edited logs; should be the logs we just
    // edited.

    // however, if there's a race condition and some other log got accessed,
    // that's fine, since the setup logs should mostly be blank, and they
    // aren't mission critical anyways. We just want them to be rotated
    // most of the time when needed.
    for (int i = 0; i < steps; i++) {
        QFileInfo log_info = logs[i];
        QFile log_file(log_info.absoluteFilePath());
        log_file.rename(create_rotated_path(log_info));
    }

    // delete all files above the maximum number to keep, if needed
    for (int i = Constants::LogMax; i < logs.size(); i++) {
        QFile log_file(logs[i].absoluteFilePath());
        log_file.remove();
    }
}

void flush_logs(QProcess &process) {
    QString logs_path = process.workingDirectory() + "/" + Constants::LogsDir;

    qint64 log_size, err_size;
    log_size = write_logs(logs_path, process.readAllStandardOutput(), ".log");
    err_size = write_logs(logs_path, process.readAllStandardError(), ".err");

    if (qMax(log_size, err_size) > Constants::LogMaxSize) {
        // rotate both logs if they exist, even if one of them is not full
        // c++ standard §4.7/4 states that true/false converts to 1/0
        int rotate_steps = log_size != 0 + err_size != 0;
        rotate_logs(logs_path, rotate_steps);
    }
}
