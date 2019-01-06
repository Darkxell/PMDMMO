#ifndef CONSTANTS_H
#define CONSTANTS_H

namespace Constants {
#ifdef Q_OS_WIN
constexpr auto SetupScript = ":/static/setup.ps1";
#else
constexpr auto SetupScript = ":/static/setup.sh";
#endif

constexpr auto PlaceholderImage = ":/static/splash.png";

constexpr auto LogsDir = "logs";
constexpr auto LogPrefix = "setup";
constexpr auto LogTimestamp = "yyyy-MM-dd-hh-mm-ss.zzz";
constexpr auto LogMaxSize = 1024 * 1024; // 1 MiB
constexpr auto LogMax = 100;
}

#endif // CONSTANTS_H
