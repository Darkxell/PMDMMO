package com.darkxell.client.state;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.state.dialog.NarratorDialogState;
import com.darkxell.common.util.language.Message;

public class TransitionState extends AbstractState
{

	public static final int FADEIN = NarratorDialogState.FADETIME, STAY = 10, TEXT = 60, FADEOUT = FADEIN;

	private AbstractState current;
	public final int fadeIn, stay, text, fadeOut, stayEnd, textStart, textFade, duration;
	public final Message message;
	public final AbstractState previous, next;
	private int tick, alpha;

	public TransitionState(AbstractState previous, AbstractState next)
	{
		this(previous, next, null);
	}

	public TransitionState(AbstractState previous, AbstractState next, Message message)
	{
		this(previous, next, message, FADEIN, STAY, TEXT, FADEOUT);
	}

	public TransitionState(AbstractState previous, AbstractState next, Message message, int fadeIn, int stay, int text, int fadeOut)
	{
		this.previous = previous;
		this.next = next;
		this.message = message;
		this.fadeIn = fadeIn;
		this.stay = stay;
		this.text = text;
		this.fadeOut = fadeOut;
		this.tick = 0;

		this.stayEnd = this.fadeIn + this.stay * 2 + this.text;
		this.textStart = this.fadeIn + this.stay;
		this.textFade = this.fadeIn + this.stay + this.text;
		this.duration = this.fadeIn + this.stay * 2 + this.text + this.fadeOut;
		this.current = this.previous;

		if (this.previous == null) this.tick = this.fadeIn;
	}

	@Override
	public void onKeyPressed(short key)
	{}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.current != null) this.current.render(g, width, height);
		if (this.message != null && this.tick >= this.textStart && this.tick <= this.stayEnd)
			TextRenderer.render(g, this.message, width / 2 - TextRenderer.width(this.message) / 2, height / 2 - TextRenderer.height());
		g.setColor(new Color(0, 0, 0, this.alpha));
		g.fillRect(0, 0, width, height);
	}

	@Override
	public void update()
	{
		++this.tick;
		if (this.tick == this.fadeIn) this.current = null;
		else if (this.tick == this.textStart && this.message == null) this.tick += this.text;
		else if (this.tick == this.stayEnd) this.current = this.next;

		if (this.tick >= this.duration) Persistance.stateManager.setState(this.next);

		if (this.tick < this.fadeIn) this.alpha = this.tick * 255 / this.fadeIn;
		else if (this.tick > this.stayEnd) this.alpha = (this.duration - this.tick) * 255 / this.fadeOut;
		else if (this.tick >= this.textFade && this.tick <= this.stayEnd) this.alpha = (this.tick - this.textFade) * 255 / this.stay;

		if (this.previous != null && this.tick < this.stay) this.previous.update();
		if (this.tick >= this.stayEnd) this.next.update();
	}

}
