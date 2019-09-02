package com.darkxell.client.state.dialog;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.resources.image.Sprites.HudSprites;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.client.ui.MainUiUtility;
import com.darkxell.common.util.Callbackable;
import com.darkxell.common.util.language.Message;

public class TextinputState extends AbstractState {

    /** Lists of chars allowed in the textfield. */
    private static final char[] ALLOWED_CHARS = new char[] { 'a', 'z', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'q', 's',
            'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'w', 'x', 'c', 'v', 'b', 'n', 'A', 'Z', 'E', 'R', 'T', 'Y', 'U',
            'I', 'O', 'P', 'Q', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'W', 'X', 'C', 'V', 'B', 'N',
            // silly french characters
            '\u00E9', // e acute
            '\u00E8', // e grave
            '\u00E7', // c cedilla
            '\u00F9', // u grave
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

    private static final int FLICKER_LENGTH = 40;
    private Callbackable addedCallback = null;
    public final AbstractGraphiclayer background;
    public String content = "";
    private int counter = 0;
    private int currentFlicker = 10;
    private boolean isInValidationMode = false;
    private Message message;
    private boolean validationState = false;

    public TextinputState(AbstractGraphiclayer background, Message message, Callbackable additionnalCallback) {
        this.background = background;
        this.message = message;
        this.addedCallback = additionnalCallback;
    }

    private boolean isCharAllowed(char c) {
        for (int i = 0; i < ALLOWED_CHARS.length; i++)
            if (ALLOWED_CHARS[i] == c)
                return true;
        return false;
    }

    @Override
    public void onKeyPressed(Key key) {
        if (key.keyValue() == KeyEvent.VK_BACK_SPACE && content.length() > 0 && !isInValidationMode)
            content = content.substring(0, content.length() - 1);
        else if (key.keyValue() == KeyEvent.VK_ENTER && content.length() > 0 && !isInValidationMode)
            isInValidationMode = true;
        else if (isInValidationMode && key == Key.ATTACK) {
            if (validationState)
                addedCallback.callback(content);
            else {
                counter = 0;
                isInValidationMode = false;
            }
        } else if (isInValidationMode && (key == Key.DOWN || key == Key.UP))
            validationState = !validationState;
    }

    @Override
    public void onKeyReleased(Key key) {
    }

    @Override
    public void onKeyTyped(KeyEvent e) {
        if (!isInValidationMode) {
            char typed = e.getKeyChar();
            if (counter >= 15 && isCharAllowed(typed)) {
                content += typed;
            }
        }
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        this.background.render(g, width, height);

        // Draws the text box.
        int temp_width = width - 40;
        int temp_height = temp_width * HudSprites.textwindow.image().getHeight()
                / HudSprites.textwindow.image().getWidth();
        Rectangle box = new Rectangle(20, height / 2 - temp_height / 2, temp_width, temp_height);
        g.drawImage(HudSprites.textwindow.image(), box.x, box.y, box.width, box.height, null);

        TextRenderer.render(g, this.message, 40, height / 2 - 20);
        g.setColor(new Color(56, 130, 184));
        g.fillRect(40, height / 2, width - 80, 20);

        TextRenderer.render(g, content, 45, height / 2 + 3);
        int xtemp = 48 + TextRenderer.width(content);
        if (currentFlicker > 0 && !isInValidationMode) {
            g.setColor(Color.WHITE);
            g.drawLine(xtemp, height / 2 + 3, xtemp, height / 2 + 14);
        }

        // Draws the confirmation box if needed.
        if (isInValidationMode) {
            MainUiUtility.drawBoxOutline(g, width - 150, height / 2 + temp_height - 15, 110, 50);
            g.setColor(new Color(32, 72, 104));
            g.fillRect(width - 150, height / 2 + temp_height - 15, 110, 50);
            TextRenderer.render(g, new Message("ui.textinput.validate"), width - 140, height / 2 + temp_height - 10);
            TextRenderer.render(g, new Message("ui.no"), width - 130, height / 2 + temp_height + 3);
            TextRenderer.render(g, new Message("ui.yes"), width - 130, height / 2 + temp_height + 16);
            if (currentFlicker > 0)
                g.drawImage(HudSprites.menuHud.selectionArrow(), width - 142,
                        height / 2 + temp_height + 4 + (validationState ? 13 : 0) - 3, null);
        }

    }

    @Override
    public void update() {
        this.background.update();
        counter++;
        // Updates the flickering bar.
        currentFlicker++;
        if (currentFlicker >= FLICKER_LENGTH)
            currentFlicker = -FLICKER_LENGTH;
    }

}
