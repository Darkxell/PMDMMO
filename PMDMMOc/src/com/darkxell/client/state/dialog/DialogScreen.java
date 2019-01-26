package com.darkxell.client.state.dialog;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.TextRenderer.PMDChar;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.language.Message;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DialogScreen {
    public enum DialogScreenState {
        FINISHED,
        PAUSED,
        PRINTING,
        SWITCHING
    }

    public static final BufferedImage arrow = Sprites.Res_Hud.menuHud.nextWindowArrow();
    public static final int ARROW_TICK_LENGTH = 20;

    int arrowtick;

    /**
     * The current line to display. When displayed, paused until the player skips.
     */
    private int currentLine;

    /**
     * The current maximum character to print.
     */
    private int cursor;

    /**
     * Customizable ID, usable for complex Dialog states. 0 by default.
     */
    public byte id = 0;

    /**
     * True if this DialogScreen prints text centered horizontally.
     */
    private boolean isCentered = false;

    /**
     * True if this DialogScreen prints instantaneously.
     */
    private boolean isInstant = false;

    /**
     * True if this Dialog's window is opaque.
     */
    public boolean isOpaque = false;

    /**
     * The split lines of the current message.
     */
    ArrayList<ArrayList<PMDChar>> lines;

    /**
     * The Message to show in this Screen.
     */
    public final Message message;

    /**
     * Text offset.
     */
    private int offset;
    protected DialogState parentState;

    /**
     * The current state of this dialog.
     */
    DialogScreenState state;

    /**
     * The offset to reach.
     */
    private int targetOffset;

    public DialogScreen(Message message) {
        this.message = message;
        this.lines = new ArrayList<>();
    }

    private int currentLength() {
        if (this.lines.isEmpty()) {
            return 0;
        }
        int length = 0;
        for (int line = 0; line <= this.currentLine && line < this.lines.size(); ++line) {
            length += this.lines.get(line).size();
        }
        return length;
    }

    public boolean dialogBoxVisible() {
        return true;
    }

    private void nextLine() {
        ++this.currentLine;
        if (this.currentLine >= this.lines.size()) {
            this.parentState.nextMessage();
        }
        this.state = DialogScreenState.PRINTING;
    }

    public void onKeyPressed(Key key) {
        if (this.state == DialogScreenState.PAUSED && (key == Key.ATTACK || key == Key.RUN)) {
            this.requestNextLine();
        }
    }

    public void onMouseClick(int x, int y) {
        if (this.parentState().dialogBoxInside().contains(x, y)) {
            if (this.state == DialogScreenState.PRINTING) {
                this.cursor = this.currentLength();
                this.state = DialogScreenState.PAUSED;
            } else if (this.state == DialogScreenState.PAUSED) {
                this.requestNextLine();
            }
        }
    }

    public void onStart() {
        this.cursor = this.offset = this.targetOffset = this.arrowtick = 0;
        this.currentLine = 2;
        this.lines.clear();
        this.state = DialogScreenState.PRINTING;
    }

    public DialogState parentState() {
        return this.parentState;
    }

    protected void reformLines(int maxwidth) {
        ArrayList<String> l = TextRenderer.splitLines(this.message.toString(), maxwidth);
        for (String line : l) {
            this.lines.add(TextRenderer.decode(line));
        }
    }

    public void render(Graphics2D g, int width, int height) {
        Rectangle dialogBox = this.parentState.dialogBox();
        Rectangle inside = this.parentState.dialogBoxInside();

        if (this.lines.isEmpty()) {
            this.reformLines(inside.width);
        }

        if (this.dialogBoxVisible()) {
            g.drawImage(
                    this.isOpaque ? Sprites.Res_Hud.textwindow.image() : Sprites.Res_Hud.textwindow_transparent.image(),
                    dialogBox.x, dialogBox.y, dialogBox.width, dialogBox.height, null);
            Shape c = g.getClip();
            g.setClip(inside);
            int length = 0;
            for (int i = 0; i < this.lines.size() && length < this.cursor; ++i) {
                int count = Math.min(this.cursor - length, this.lines.get(i).size());
                List<PMDChar> line = this.lines.get(i).subList(0, count);
                int x = inside.x;
                if (this.isCentered) {
                    x += inside.getWidth() / 2 - TextRenderer.width(line) / 2;
                }

                TextRenderer.render(g, line, x,
                        inside.y - this.offset + i * (TextRenderer.height() + TextRenderer.lineSpacing()));
                length += count;
            }
            g.setClip(c);

            if (this.state == DialogScreenState.PAUSED && this.arrowtick > 9 && this.parentState.isMain()) {
                g.drawImage(arrow, dialogBox.x + dialogBox.width / 2 - arrow.getWidth() / 2,
                        (int) (dialogBox.getMaxY() - arrow.getHeight() * 3 / 4), null);
            }
        }
    }

    protected void requestNextLine() {
        if (this.currentLine < this.lines.size() - 1 || this.switchAnimation()) {
            this.state = DialogScreenState.SWITCHING;
            this.targetOffset = this.offset + TextRenderer.height() + TextRenderer.lineSpacing();
            if (this.currentLine >= this.lines.size() - 1) {
                this.targetOffset += (TextRenderer.height() + TextRenderer.lineSpacing()) * 2;
            }
        } else {
            this.nextLine();
        }
    }

    /**
     * @return True if this message can be finished.
     */
    public boolean requestNextMessage() {
        return true;
    }

    public DialogScreen setCentered() {
        this.isCentered = true;
        return this;
    }

    public DialogScreen setID(byte id) {
        this.id = id;
        return this;
    }

    public DialogScreen setInstant() {
        this.isInstant = true;
        return this;
    }

    /**
     * @return True if the background should be rendered when this DialogScreen is active.
     */
    public boolean shouldRenderBackground() {
        return true;
    }

    /**
     * @return True if the current message should be followed with a switching animation.
     */
    protected boolean switchAnimation() {
        if (this.parentState.nextScreen() == null) {
            return false;
        }
        return true;
    }

    public void update() {
        if (this.state == DialogScreenState.PRINTING && !this.lines.isEmpty()) {
            if (this.isInstant) {
                this.cursor = this.currentLength();
            } else {
                ++this.cursor;
            }
            if (this.cursor >= this.currentLength()) {
                this.state = DialogScreenState.PAUSED;
            }
            if (this.state == DialogScreenState.PAUSED && Key.RUN.isPressed() && this.parentState.isMain()) {
                this.requestNextLine();
            }
        } else if (this.state == DialogScreenState.SWITCHING) {
            this.offset += 3;
            if (this.offset >= this.targetOffset + 10) // +10 to add some delay
            {
                this.offset = this.targetOffset;
                this.nextLine();
            }
        }

        ++this.arrowtick;
        if (this.arrowtick >= ARROW_TICK_LENGTH) {
            this.arrowtick = 0;
        }
    }
}