#include <QApplication>
#include <QThread>

#include "PMDOSplashScreen.h"
#include "constants.h"

int main(int argc, char *argv[])
{
    // qt must include a QtGuiApplication before any widgets are allowed to
    // be shown, but we don't actually use any qt stuff here.
    QApplication a(argc, argv);

    QPixmap image(Constants::PlaceholderImage);
    PMDOSplashScreen w(image);

    w.load();
    return 0;
}
