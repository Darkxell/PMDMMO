package com.darkxell.client.resources;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;

import com.darkxell.common.util.Logger;

/** Class that holds commonly used resources utility. */
public class Res {

    private static final int MAXMESSAGES = 5;
    private static final File jarFile;
    public static final boolean RUNNING_IN_IDE;
    static {
        jarFile = new File(
                Res.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
        RUNNING_IN_IDE = !jarFile.isFile();
    }

    private static int messagecounter = 0;

    private static Font mysterydungeonfont = null;

    /**
     * Gets a part of a buffered Image. This method is safe: if the dimensions specified are out of the image's bound,
     * this will throw no exception but may return unexpected results.
     */
    public static BufferedImage createimage(BufferedImage image, int x, int y, int width, int height) {
        if (width > image.getWidth() || height > image.getHeight())
            return image;
        if (x + width > image.getWidth())
            x = image.getWidth() - width;
        if (y + height > image.getHeight())
            y = image.getHeight() - height;
        return image.getSubimage(x, y, width, height);
    }

    /**
     * @param  path - Path to a resource.
     * @return      True if the resource exists.
     */
    public static boolean exists(String path) {
        return Res.class.getResourceAsStream(path) != null;
    }

    public static InputStream get(String path) {
        return Res.class.getResourceAsStream(path);
    }

    /** Gets an image from the res folder as a BufferedImage. */
    public static BufferedImage getBase(String path) {
        BufferedImage img = null;

        try {
            InputStream is = Res.class.getResourceAsStream(path);
            img = ImageIO.read(is);
        } catch (Exception e) {
            try {
                img = ImageIO.read(new File(path));
            } catch (Exception e1) {
                Logger.e("Could not read the following image file : " + path + "\nerrors : " + e + " / " + e1);
            }
        }
        if (messagecounter < MAXMESSAGES) {
            Logger.i("Loaded resource from hard memory : " + path);
            messagecounter++;
        } else if (messagecounter == MAXMESSAGES) {
            Logger.i("Disabled the \"loaded resource from hard memory\" message for future loads");
            messagecounter++;
        }
        return img;
    }

    /**
     * Gets the mystery dungeon font. if it can't get it, will return a default font, most likely Arial. The returned
     * font is PLAIN and has a point size of 1.
     */
    public static Font getFont() {
        if (mysterydungeonfont == null)
            try {
                mysterydungeonfont = Font.createFont(Font.TRUETYPE_FONT, Res.get("/mysterydungeon.ttf"));
            } catch (Exception e) {
                Logger.e("Could not load mystery dungeon font!");
                e.printStackTrace();
                mysterydungeonfont = new Font("Arial", Font.PLAIN, 1);
            }
        return mysterydungeonfont;
    }

    /** @param path - Shouldn't start or end with / */
    public static String[] getResourceFiles(String path) {

        ArrayList<String> files = new ArrayList<>();

        if (!RUNNING_IN_IDE) { // Run with JAR file
            JarFile jar;
            try {
                jar = new JarFile(jarFile);
                final Enumeration<JarEntry> entries = jar.entries(); // gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    if (name.startsWith(path + "/") && !name.endsWith(path + "/"))
                        files.add('/' + name);
                }
                jar.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // Run with IDE
            final URL url = Res.class.getResource("/" + path);
            if (url != null)
                try {
                    final File folder = new File(url.toURI());
                    for (File file : folder.listFiles())
                        files.add('/' + path + '/' + file.getName());
                } catch (URISyntaxException ex) {
                    // never happens
                }
        }
        return files.toArray(new String[files.size()]);

        /*
         * List<String> filenames = new ArrayList<>(); try { InputStream in = Res.class.getResourceAsStream(path);
         * BufferedReader br = new BufferedReader(new InputStreamReader(in)); String resource; while ((resource =
         * br.readLine()) != null) { filenames.add(resource); } } catch (Exception e) { e.printStackTrace(); } return
         * filenames.toArray(new String[filenames.size()]);
         */
    }

    /**
     * Basically the same a s a getspriterow command, but the sprite doesn't have to be a square.
     * 
     * @see <code>getSpriteRow()</code>
     */
    public static BufferedImage[] getSpriteRectRow(BufferedImage sheet, int x, int y, int width, int height,
            int spritesAmmount) {
        BufferedImage[] toreturn = new BufferedImage[spritesAmmount];
        for (int i = 0; i < toreturn.length; i++)
            toreturn[i] = Res.createimage(sheet, x + (i * width), y, width, height);
        return toreturn;
    }

    /**
     * Gets multiple player sprites next to each other in the specified BufferImage sheet file and returns them inside
     * an array.
     * 
     * @param x              the X position of the sprite in the player.png file.
     * @param y              the Y position of the sprite in the player.png file.
     * @param size           the size of the sprite. The sprite is a square, and the x/y coordinates are its top left
     *                       corner.
     * @param spritesAmmount the amount of sprites in the array. Also the length of the array, obviously.
     */
    public static BufferedImage[] getSpriteRow(BufferedImage sheet, int x, int y, int size, int spritesAmmount) {
        return getSpriteRectRow(sheet, x, y, size, size, spritesAmmount);
    }

    public static String[] readFile(String path) {
        InputStream is = get(path);
        if (is == null)
            return null;

        String content = "";
        int c = -1;
        try {
            while ((c = is.read()) != -1) {
                if (c == 13)
                    continue;
                content += ((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.split("\n");
    }

}
