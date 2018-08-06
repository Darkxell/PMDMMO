package com.darkxell.client.resources.images.others;

import com.darkxell.client.resources.images.Sprite;
import com.darkxell.client.resources.images.SpriteFactory;
import com.darkxell.client.ui.Frame.FrameIconSprite;

public class FrameResources {
	
	public static final Sprite ICON = new FrameIconSprite("/hud/framebackgrounds/icon.png");

	public static final Sprite source = new Sprite("/hud/boxcorners.png");
	public static final Sprite box_NW = SpriteFactory.instance().subSprite(source, 0, 0, 7, 3);
	public static final Sprite box_NE = SpriteFactory.instance().subSprite(source, 8, 0, 7, 3);
	public static final Sprite box_SW = SpriteFactory.instance().subSprite(source, 0, 4, 7, 3);
	public static final Sprite box_SE = SpriteFactory.instance().subSprite(source, 8, 4, 7, 3);
	public static final Sprite box_N = SpriteFactory.instance().subSprite(source, 7, 0, 1, 3);
	public static final Sprite box_E = SpriteFactory.instance().subSprite(source, 8, 3, 7, 1);
	public static final Sprite box_S = SpriteFactory.instance().subSprite(source, 7, 4, 1, 3);
	public static final Sprite box_W = SpriteFactory.instance().subSprite(source, 0, 3, 7, 1);
	
	public static final Sprite BG1 = new Sprite("/hud/framebackgrounds/1.jpg");
	public static final Sprite BG2 = new Sprite("/hud/framebackgrounds/2.jpg");
	public static final Sprite BG3 = new Sprite("/hud/framebackgrounds/3.png");
	public static final Sprite BG4 = new Sprite("/hud/framebackgrounds/4.png");
	public static final Sprite BG5 = new Sprite("/hud/framebackgrounds/5.jpg");
	public static final Sprite BG6 = new Sprite("/hud/framebackgrounds/6.png");
	public static final Sprite BG7 = new Sprite("/hud/framebackgrounds/7.jpg");

}
