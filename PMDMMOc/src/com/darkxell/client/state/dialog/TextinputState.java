package com.darkxell.client.state.dialog;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.client.ui.MainUiUtility;
import com.darkxell.common.util.Callbackable;
import com.darkxell.common.util.language.Message;

public class TextinputState extends AbstractState {

    public AbstractState parent;
    /** Lists of chars allowed in the textfield. */
    private static final char[] ALLOWEDCHARS = new char[] { 'a', 'z', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'q', 's',
            'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'w', 'x', 'c', 'v', 'b', 'n', 'A', 'Z', 'E', 'R', 'T', 'Y', 'U',
            'I', 'O', 'P', 'Q', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'W', 'X', 'C', 'V', 'B', 'N',
            // silly french characters
            '\u00E9', // e acute
            '\u00E8', // e grave
            '\u00E7', // c cedilla
            '\u00F9', // u grave
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
    private String messageid = "ui.textinput.prompt";
    private String content = "";

    private boolean isinvalidationmode = false;
    private boolean validationstate = false;

    private Callbackable addedcallback = null;

    public TextinputState(AbstractState parent) {
        super();
        this.parent = parent;
    }

    public TextinputState(AbstractState parent, String messageid) {
        this(parent);
        this.messageid = messageid;
    }

    public TextinputState(AbstractState parent, String messageid, Callbackable additionnalcallback) {
        this(parent, messageid);
        this.addedcallback = additionnalcallback;
    }

    @Override
    public void onKeyPressed(Key key) {
        if (key.keyValue() == KeyEvent.VK_BACK_SPACE && content.length() > 0 && !isinvalidationmode) {
            content = content.substring(0, content.length() - 1);
        } else if (key.keyValue() == KeyEvent.VK_ENTER && content.length() > 0 && !isinvalidationmode) {

            isinvalidationmode = true;

        } else if (isinvalidationmode && key == Key.ATTACK) {
            if (validationstate) {
                ((PrincipalMainState) Persistence.stateManager).setState(parent);
                if (parent instanceof Callbackable)
                    ((Callbackable) parent).callback(content);
                if (addedcallback != null)
                    addedcallback.callback(content);
            } else {
                counter = 0;
                isinvalidationmode = false;
            }
        } else if (isinvalidationmode && (key == Key.DOWN || key == Key.UP)) {
            validationstate = !validationstate;
        }
    }

    @Override
    public void onKeyReleased(Key key) {
    }

    @Override
    public void onKeyTyped(KeyEvent e) {
        if (!isinvalidationmode) {
            char typed = e.getKeyChar();
            if (counter >= 15 && isCharAllowed(typed)) {
                content += typed;
            }
        }
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        parent.render(g, width, height);

        // Draws the text box.
        int temp_width = width - 40;
        int temp_height = temp_width * Sprites.Res_Hud.textwindow.image().getHeight()
                / Sprites.Res_Hud.textwindow.image().getWidth();
        Rectangle box = new Rectangle(20, height / 2 - temp_height / 2, temp_width, temp_height);
        g.drawImage(Sprites.Res_Hud.textwindow.image(), box.x, box.y, box.width, box.height, null);

        TextRenderer.render(g, new Message(messageid), 40, height / 2 - 20);
        g.setColor(new Color(56, 130, 184));
        g.fillRect(40, height / 2, width - 80, 20);

        TextRenderer.render(g, content, 45, height / 2 + 3);
        int xtemp = 48 + TextRenderer.width(content);
        if (currentflicker > 0) {
            g.setColor(Color.WHITE);
            g.drawLine(xtemp, height / 2 + 3, xtemp, height / 2 + 14);
        }

        // Draws the confirmation box if needed.
        if (isinvalidationmode) {
            MainUiUtility.drawBoxOutline(g, width - 150, height / 2 + temp_height - 15, 110, 50);
            g.setColor(new Color(32, 72, 104));
            g.fillRect(width - 150, height / 2 + temp_height - 15, 110, 50);
            TextRenderer.render(g, new Message("ui.textinput.validate"), width - 140, height / 2 + temp_height - 10);
            TextRenderer.render(g, new Message("ui.no"), width - 130, height / 2 + temp_height + 3);
            TextRenderer.render(g, new Message("ui.yes"), width - 130, height / 2 + temp_height + 16);
            g.drawImage(Sprites.Res_Hud.menuHud.tabRight(), width - 140,
                    height / 2 + temp_height + 4 + (validationstate ? 13 : 0), null);
        }

    }

    private static final int FLICKERLENGTH = 40;
    private int currentflicker = 10;
    private int counter = 0;

    @Override
    public void update() {
        parent.update();
        counter++;
        // Updates the flickering bar.
        currentflicker++;
        if (currentflicker >= FLICKERLENGTH)
            currentflicker = -FLICKERLENGTH;
    }

    private boolean isCharAllowed(char c) {
        for (int i = 0; i < ALLOWEDCHARS.length; i++)
            if (ALLOWEDCHARS[i] == c)
                return true;
        return false;
    }

}
