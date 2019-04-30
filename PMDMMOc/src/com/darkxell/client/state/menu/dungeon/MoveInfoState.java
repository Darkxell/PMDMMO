package com.darkxell.client.state.menu.dungeon;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.InfoState;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.state.menu.components.TextWindow;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.util.language.Message;

public class MoveInfoState extends InfoState {

	private TextWindow details1, details2;
	private final Message details1Message, details2Message;
	public final Move move;

    public MoveInfoState(Move move, AbstractGraphiclayer background, AbstractState parent) {
        super(background, parent, new Message[] { move.name() }, new Message[] { move.description() });
        this.move = move;

		this.details1Message = new Message("move.info.details.0");
		this.details1Message.addReplacement("<type>", this.move.type.getName());
		this.details1Message.addReplacement("<category>", this.move.category.getName());
		this.details2Message = new Message("move.info.details.1");
		this.details2Message.addReplacement("<range>", this.move.range.getName(this.move.targets));
		this.details2Message.addReplacement("<power>",
				this.move.category == MoveCategory.Status ? new Message(" --", false)
						: TextRenderer.alignNumber(this.move.displayedPower(), 3));
		this.details2Message.addReplacement("<accuracy>", TextRenderer.alignNumber(this.move.accuracy, 3));
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		super.render(g, width, height);
		if (this.details1 == null) {
			Rectangle r = this.window.dimensions;
			this.details1 = new TextWindow(
					new Rectangle(r.x, r.y + r.height + 2, r.width * 2 / 5 - 1,
							2 * MenuWindow.MARGIN_Y + 2 * (TextRenderer.height() + TextRenderer.lineSpacing())),
					this.details1Message, false);
			this.details2 = new TextWindow(
					new Rectangle(r.x + r.width * 2 / 5 + 2, r.y + r.height + 2, r.width * 3 / 5 - 1,
							2 * MenuWindow.MARGIN_Y + 2 * (TextRenderer.height() + TextRenderer.lineSpacing())),
					this.details2Message, false);
			this.details1.leftTab = this.details1.rightTab = this.details2.leftTab = this.details2.rightTab = false;
			this.details1.isOpaque = this.details2.isOpaque = this.isOpaque;
		}
		this.details1.render(g, null, width, height);
		this.details2.render(g, null, width, height);
	}
}
