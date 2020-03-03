package com.darkxell.client.state;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.state.dialog.NarratorDialogScreen;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.language.Message;

public class TransitionState extends AbstractState {
    public static final int FADEIN = NarratorDialogScreen.FADETIME, STAY = 10, TEXT = 60, FADEOUT = FADEIN;

    private AbstractState current;
    public int fadeIn, stay, text, fadeOut, stayEnd, textStart, textFade, duration;
    public final Message[] messages;
    public AbstractState previous, next;
    private int tick, alpha;

    public TransitionState(AbstractState previous, AbstractState next) {
        this(previous, next, new Message[0]);
    }

    public TransitionState(AbstractState previous, AbstractState next, Message[] message) {
        this(previous, next, message, FADEIN, STAY, TEXT, FADEOUT);
    }

    public TransitionState(AbstractState previous, AbstractState next, Message[] message, int fadeIn, int stay,
            int text, int fadeOut) {
        this.previous = previous;
        this.next = next;
        this.messages = message;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.text = text;
        this.fadeOut = fadeOut;
        this.tick = 0;

        this.textStart = this.fadeIn + this.stay;
        this.textFade = this.fadeIn + this.stay + this.text;
        this.stayEnd = this.fadeIn + this.stay + this.text * 2;
        this.duration = this.fadeIn + this.stay + this.text * 2 + this.fadeOut;
        this.current = this.previous;

        if (this.previous == null)
            this.tick = this.fadeIn;
    }

    public int minimapFading() {
        return this.tick >= this.fadeIn && this.tick <= this.stayEnd ? 255 : this.alpha;
    }

    @Override
    public void onKeyPressed(Key key) {
    }

    @Override
    public void onKeyReleased(Key key) {
    }

    public void onTransitionHalf() {
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        if (this.current != null)
            this.current.render(g, width, height);

        int y = height / 2 - (TextRenderer.height() + TextRenderer.lineSpacing()) * this.messages.length / 2;
        if (this.tick >= this.textStart && this.tick <= this.stayEnd)
            for (Message m : this.messages) {
                TextRenderer.render(g, m, width / 2 - TextRenderer.width(m) / 2, y);
                y += TextRenderer.height() + TextRenderer.lineSpacing();
            }

        g.setColor(new Color(0, 0, 0, this.alpha));
        g.fillRect(0, 0, width, height);
    }

    @Override
    public void update() {
        ++this.tick;
        if (this.tick == this.fadeIn)
            this.current = null;
        else if (this.tick == this.textStart) {
            this.onTransitionHalf();
            if (this.messages == null)
                this.tick += this.text + this.stay;
        }
        if (this.tick == this.stayEnd)
            this.current = this.next;

        if (this.tick >= this.duration)
            Persistence.stateManager.setState(this.next);

        this.alpha = 0;
        if (this.tick < this.fadeIn)
            this.alpha = this.fadeIn == 0 ? 0 : this.tick * 255 / this.fadeIn;
        else if (this.tick > this.stayEnd)
            this.alpha = this.fadeOut == 0 ? 0 : (this.duration - this.tick) * 255 / this.fadeOut;
        else if (this.tick >= this.textFade && this.tick <= this.stayEnd)
            this.alpha = (this.tick - this.textFade) * 255 / this.text;

        if (this.current != null)
            this.current.update();
    }

}
