#ifndef CONSTANTS_H
#define CONSTANTS_H

#if defined(WIN32) || defined(_WIN32) || defined(__WIN32) && !defined(__CYGWIN__)
#define IS_WIN
#endif

namespace Constants {
#ifdef IS_WIN
constexpr bool IsWindows = true;

constexpr auto SetupScript = ":/static/setup.ps1";
#else
constexpr bool IsWindows = false;

constexpr auto SetupScript = ":/static/setup.sh";
#endif

constexpr auto PlaceholderImage = ":/static/placeholder.png";

constexpr auto LogsDir = "logs";
constexpr auto LogPrefix = "setup";
constexpr auto LogTimestamp = "yyyy-MM-dd-hh-mm-ss.zzz";
}

#endif // CONSTANTS_H
