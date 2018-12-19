package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;

import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.images.entities.WaterSparklesSpriteSet;
import com.darkxell.client.resources.images.hud.DungeonHudSpriteset;
import com.darkxell.client.resources.images.hud.FontSpriteSet;
import com.darkxell.client.resources.images.hud.ItemsSpriteset;
import com.darkxell.client.resources.images.hud.MenuStateHudSpriteset;
import com.darkxell.client.resources.images.layers.LSDSpriteSet;
import com.darkxell.client.resources.images.layers.SeaSpriteSet;
import com.darkxell.client.resources.images.layers.WetDreamSpriteSet;
import com.darkxell.client.resources.images.pokemon.ShadowSpriteSet;
import com.darkxell.client.resources.images.tilesets.CommonDungeonTileset;
import com.darkxell.client.resources.images.tilesets.DungeonMapTileset;
import com.darkxell.client.ui.Frame.FrameIconSprite;
import com.darkxell.common.util.Logger;

/** Class that holds Sprites & SpriteSets used in the whole project. */
public final class Sprites
{

	public static class Res_Dungeon
	{
		public static final CommonDungeonTileset dungeonCommon = new CommonDungeonTileset();
		public static final DungeonHudSpriteset dungeonHud = new DungeonHudSpriteset();
		public static final DungeonMapTileset dungeonMap = new DungeonMapTileset();
		public static final ItemsSpriteset items = new ItemsSpriteset();
		public static final ShadowSpriteSet shadows = new ShadowSpriteSet();

		static void load()
		{}
	}

	public static class Res_Frame {
		public static final Sprite box_E, box_N, box_NE, box_NW, box_S, box_SE, box_SW, box_W;
		// why is this here? it isn't being used.
		public static final Sprite ICON = new FrameIconSprite("/hud/framebackgrounds/icon.png");

		/**
		 * Target file path of the background images.
		 */
		public static final String BACKGROUNDS_DIRECTORY = "/hud/framebackgrounds";

		/**
		 * Background images ooh pretty.
		 */
		public static final Sprite[] BACKGROUNDS;

		public static final int DEFAULT_BACKGROUND_INDEX = 0;

		/**
		 * Which files in the frame background image folder to ignore.
		 */
		public static final String[] EXCLUDE_FILES = new String[]{"icon.png"};

		static {
			String resourceDirectory;
			try {
				resourceDirectory = Res.class.getResource(BACKGROUNDS_DIRECTORY).toURI().getPath();
			} catch (URISyntaxException e) {
				Logger.e("Background resource " + BACKGROUNDS_DIRECTORY + " is not a valid resource.");
				resourceDirectory = null;
			}

			if (resourceDirectory == null) {
				// TODO: use actual screen size, not just a guess
				BACKGROUNDS = new Sprite[]{ SpriteFactory.getDefaultSprite(500, 500)};
			} else {
				File[] backgroundPaths = new File(resourceDirectory).listFiles(filepath -> {
					for (String excludeFilepath : EXCLUDE_FILES) {
						if (filepath.getName().equals(excludeFilepath)) {
							return false;
						}
					}
					return true;
				});

				BACKGROUNDS = Arrays.stream(backgroundPaths)
						.sorted()
						.map(f -> new Sprite(BACKGROUNDS_DIRECTORY + '/' + f.getName()))
						.toArray(Sprite[]::new);
			}

			Sprite source = new Sprite("/hud/boxcorners.png");

			box_N = SpriteFactory.instance().subSprite(source, 7, 0, 1, 3);
			box_S = SpriteFactory.instance().subSprite(source, 7, 4, 1, 3);

			box_E = SpriteFactory.instance().subSprite(source, 8, 3, 7, 1);
			box_W = SpriteFactory.instance().subSprite(source, 0, 3, 7, 1);

			box_NE = SpriteFactory.instance().subSprite(source, 8, 0, 7, 3);
			box_NW = SpriteFactory.instance().subSprite(source, 0, 0, 7, 3);
			box_SE = SpriteFactory.instance().subSprite(source, 8, 4, 7, 3);
			box_SW = SpriteFactory.instance().subSprite(source, 0, 4, 7, 3);
		}

		public static BufferedImage getBackground(int id) {
			id--; // backwards compatibility; was 1-indexed
			if (id >= 0 && id < BACKGROUNDS.length) {
				return BACKGROUNDS[id].image();
			}
			return BACKGROUNDS[DEFAULT_BACKGROUND_INDEX].image();
		}

		static void load() {
		}
	}

	public static class Res_FreezoneEntities
	{
		public static final Sprite cristal_red, cristal_yellow, cristal_lightray;
		public static final RegularSpriteSet flag = new RegularSpriteSet("/freezones/entities/flag.png", 32, 8, 192, 8);
		public static final RegularSpriteSet RedFlower = new RegularSpriteSet("/freezones/entities/redflower.png", 32, 32, 192, 32);

		public static final WaterSparklesSpriteSet waterSparkles = new WaterSparklesSpriteSet();

		public static final RegularSpriteSet YellowFlower = new RegularSpriteSet("/freezones/entities/yellowflower.png", 32, 32, 192, 32);

		static
		{
			Sprite cristal = new Sprite("/freezones/entities/cristal.png");
			cristal_red = SpriteFactory.instance().subSprite(cristal, 1, 54, 56, 81);
			cristal_yellow = SpriteFactory.instance().subSprite(cristal, 58, 54, 56, 81);
			cristal_lightray = SpriteFactory.instance().subSprite(cristal, 115, 1, 48, 134);
		}

		static void load()
		{}
	}

	public static class Res_GraphicalLayers
	{
		public static final LSDSpriteSet LSD = new LSDSpriteSet();
		public static final SeaSpriteSet Sea = new SeaSpriteSet();
		public static final WetDreamSpriteSet Dream = new WetDreamSpriteSet();

		static void load()
		{}
	}

	public static class Res_Hud
	{
		public static final Sprite button = new Sprite("/hud/button.png");
		public static final Sprite createaccountframe = new Sprite("/hud/create.png");

		public static final FontSpriteSet font = new FontSpriteSet();
		public static final Sprite gametitle = new Sprite("/hud/title.png");
		public static final Sprite loginframe = new Sprite("/hud/login.png");
		public static final MenuStateHudSpriteset menuHud = new MenuStateHudSpriteset();
		public static final Sprite portrait = new Sprite("/hud/portrait.png");
		public static final Sprite proceedaccountframe = new Sprite("/hud/create2.png");
		public static final Sprite textwindow = new Sprite("/hud/textwindow.png");
		public static final Sprite textwindow_transparent = new Sprite("/hud/textwindow_transparent.png");

		static void load()
		{}
	}

	public static class Res_Map
	{
		public static final Sprite GLOBALMAP = new Sprite("/hud/map/globalmap.png");
		public static final Sprite LOCALMAP = new Sprite("/hud/map/localmap.png");
		public static final Sprite PIN_RED, PIN_YELLOW, PIN_BLUE, PIN_GREEN;

		static
		{
			SpriteSet pinsBase = new SpriteSet("/hud/map/pins.png", 48, 12);
			PIN_RED = pinsBase.createSprite("red", 0, 0, 12, 12);
			PIN_YELLOW = pinsBase.createSprite("yellow", 12, 0, 12, 12);
			PIN_BLUE = pinsBase.createSprite("blue", 24, 0, 12, 12);
			PIN_GREEN = pinsBase.createSprite("green", 36, 0, 12, 12);
		}

		static void load()
		{}
	}

	private Sprites()
	{}

}
