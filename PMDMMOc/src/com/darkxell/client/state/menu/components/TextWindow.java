package com.darkxell.client.state.menu.components;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.graphics.TextRenderer;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.common.util.language.Message;

public class TextWindow extends MenuWindow {

    private int cursor = 0;
    /** True if this Window leads to another when accepted. (will draw an arrow at bottom.) */
    public final boolean hasNext;
    public boolean leftTab = false, rightTab = false;
    private ArrayList<String> lines;
    /** The message to display. */
    private Message message;

    public TextWindow(Rectangle dimensions, Message message, boolean hasNext) {
        super(dimensions);
        this.message = message;
        this.hasNext = hasNext;
    }

    @Override
    public void render(Graphics2D g, Message name, int width, int height) {
        super.render(g, name, width, height);
        this.renderText(g, width, height);

        if (this.rightTab)
            g.drawImage(Sprites.Res_Hud.menuHud.tabRight(),
                    (int) this.inside.getMaxX() - Sprites.Res_Hud.menuHud.tabRight().getWidth(),
                    this.dimensions.y - Sprites.Res_Hud.menuHud.tabRight().getHeight() / 3, null);
        if (this.leftTab)
            g.drawImage(Sprites.Res_Hud.menuHud.tabLeft(),
                    (int) this.inside.getMaxX() - Sprites.Res_Hud.menuHud.tabLeft().getWidth()
                            - Sprites.Res_Hud.menuHud.tabRight().getWidth() - 5,
                    this.dimensions.y - Sprites.Res_Hud.menuHud.tabLeft().getHeight() / 3, null);

        if (this.hasNext) {
            int x = this.dimensions.x - this.dimensions.width / 2
                    - Sprites.Res_Hud.menuHud.nextWindowArrow().getWidth() / 2;
            int y = (int) (this.dimensions.getMaxY() - Sprites.Res_Hud.menuHud.nextWindowArrow().getHeight() / 2);
            g.drawImage(Sprites.Res_Hud.menuHud.nextWindowArrow(), x, y, null);
        }
    }

    private void renderText(Graphics2D g, int width, int height) {
        if (this.lines == null)
            this.lines = TextRenderer.splitLines(this.message.toString(), this.inside().width - 10);

        int x = this.inside().x + 5, y = this.inside().y + 5;
        for (String line : this.lines) {
            TextRenderer.render(g, line, x, y);
            y += TextRenderer.height() + TextRenderer.lineSpacing();
        }
    }

    public void setMessage(Message message) {
        this.message = message;
        this.lines = null;
    }

    public void update() {
        ++this.cursor;
        if (this.cursor > 20)
            this.cursor = 0;
    }

}
