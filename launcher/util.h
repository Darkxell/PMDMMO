#ifndef UTIL_H
#define UTIL_H

#include <QString>

#include "constants.h"

QString copy_tmp(QString resource);
void touch_dir(QString path);
QString code_to_reason(int code);

#endif // UTIL_H
