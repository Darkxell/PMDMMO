package com.darkxell.client.state;

import java.awt.Graphics2D;

public abstract class AbstractState
{

	public abstract void render(Graphics2D g, int width, int height);

	public abstract void update();

}
