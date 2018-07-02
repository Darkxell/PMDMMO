package com.darkxell.client.state.menu.components;

import java.awt.Graphics2D;

import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.AbstractState;

public class IntegerSelectionState extends AbstractState
{

	public static interface IntegerSelectionListener
	{
		public void onIntegerSelected(int selection);
	}

	public final AbstractGraphiclayer background;
	public final IntegerSelectionListener listener;
	public final long min, max, start;

	public IntegerSelectionState(AbstractGraphiclayer background, IntegerSelectionListener listener, long min, long max, long start)
	{
		this.background = background;
		this.listener = listener;
		this.min = min;
		this.max = max;
		this.start = start;
	}

	@Override
	public void onKeyPressed(short key)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyReleased(short key)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub

	}

}
