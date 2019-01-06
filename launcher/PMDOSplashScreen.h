#ifndef PMDOSPLASHSCREEN_H
#define PMDOSPLASHSCREEN_H

#include <QSplashScreen>

namespace Ui {
class PMDOSplashScreen;
}

class PMDOSplashScreen : public QSplashScreen
{
    Q_OBJECT

public:
    explicit PMDOSplashScreen(QPixmap image);
    bool load();
};

#endif // WIDGET_H
