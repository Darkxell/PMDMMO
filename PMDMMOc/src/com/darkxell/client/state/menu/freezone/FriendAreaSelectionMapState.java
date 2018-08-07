package com.darkxell.client.state.menu.freezone;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.client.resources.images.others.MapResources;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.zones.FreezoneInfo;
import com.darkxell.common.zones.LocalMapLocation;

public class FriendAreaSelectionMapState extends AbstractState
{

	private float camerax = LocalMapLocation.BASE.x - 70;
	private float cameray = LocalMapLocation.BASE.y;
	private float cursorx = LocalMapLocation.BASE.x;
	private float cursory = LocalMapLocation.BASE.y;

	private int memorywidth = 300;
	private int memoryheight = 200;

	private boolean movingleft = false;
	private boolean movingright = false;
	private boolean movingdown = false;
	private boolean movingup = false;

	private static final Color boxgray_inside = new Color(0, 0, 0, 120);

	private boolean setmusic = true;

	/** threshhold distance for cursor snapping */
	private final float thresholddistance = 16.1f;

	public FriendAreaSelectionMapState()
	{
		super();
	}

	@Override
	public void onKeyPressed(Key key)
	{
		switch (key)
		{
			case RIGHT:
				movingright = true;
				break;
			case LEFT:
				movingleft = true;
				break;
			case UP:
				movingup = true;
				break;
			case DOWN:
				movingdown = true;
				break;
			case RUN:
				StateManager.setExploreState(FreezoneInfo.BASE, Direction.EAST, 4, 42);
				break;
			case ATTACK:
				LocalMapLocation[] points = LocalMapLocation.values();
				for (int i = 0; i < points.length; i++)
					if (points[i].showsonfriendsmap && isnearpoint(points[i]))
					{
						if (points[i] == LocalMapLocation.BASE)
						{
							StateManager.setExploreState(FreezoneInfo.BASE, Direction.EAST, 4, 42);
							break;
						}
						ArrayList<FreezoneInfo> dests = new ArrayList<>(5);
						FreezoneInfo[] allinfos = FreezoneInfo.values();
						for (int j = 0; j < allinfos.length; j++)
							if (allinfos[j].maplocation == points[i]) dests.add(allinfos[j]);
						SoundManager.playSound("ui-select");
						Persistance.stateManager.setState(new FriendmapSelectionState(this, dests, points[i].displayname));
						break;
					}
				break;
			default:
				break;
		}
	}

	@Override
	public void onKeyReleased(Key key)
	{
		switch (key)
		{
			case RIGHT:
				movingright = false;
				break;
			case LEFT:
				movingleft = false;
				break;
			case UP:
				movingup = false;
				break;
			case DOWN:
				movingdown = false;
				break;
			default:
				break;
		}
		// If stopped moving, snap to closest point if near one
		if (!(movingdown || movingup || movingleft || movingright))
		{
			LocalMapLocation[] points = LocalMapLocation.values();
			for (int i = 0; i < points.length; i++)
				if (points[i].showsonfriendsmap && points[i].x > cursorx - thresholddistance && points[i].x < cursorx + thresholddistance
						&& points[i].y > cursory - thresholddistance && points[i].y < cursory + thresholddistance)
				{
					this.lockOn(points[i]);
					break;
				}
		}
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		this.memoryheight = height;
		this.memorywidth = width;
		// CALCULATES TRANSLATIONS
		int translateX = (int) (-camerax + (width / 2));
		int translateY = (int) (-cameray + (height / 2));

		g.translate(translateX, translateY);
		// DRAWS THE MAP
		g.drawImage(MapResources.LOCALMAP, 0, 0, null);
		LocalMapLocation[] infos = LocalMapLocation.values();
		for (int i = 0; i < infos.length; i++)
			if (infos[i].showsonfriendsmap)
			{
				boolean text = isnearpoint(infos[i]);
				g.drawImage(infos[i] == LocalMapLocation.BASE ? MapResources.PIN_RED : text ? MapResources.PIN_GREEN : MapResources.PIN_YELLOW, infos[i].x - 6,
						infos[i].y - 6, null);
				if (text)
				{
					int twidth = TextRenderer.width(infos[i].displayname);
					g.setColor(boxgray_inside);
					int leftpart = infos[i].x - (twidth / 2) - 4;
					if (leftpart <= 0) leftpart = 0;
					else if (leftpart + twidth + 8 >= MapResources.LOCALMAP.getWidth()) leftpart = MapResources.LOCALMAP.getWidth() - twidth - 8;
					g.fillRect(leftpart, infos[i].y - 20, twidth + 8, 13);
					TextRenderer.render(g, infos[i].displayname, leftpart + 4, infos[i].y - 18);
				}
			}

		g.drawImage(Sprites.Hud.menuHud.selectionArrow(), (int) cursorx, (int) cursory, null);
		// TRANSLATES THE GRAPHICS BACK
		g.translate(-translateX, -translateY);

		if (this.isMain())
		{
			// DRAWS THE HUD OVER THE MAP
			int textxpos = shouldInvertUI() ? 15
					: width - Math.max(TextRenderer.width(new Message("localmap.select")), TextRenderer.width(new Message("dungeonmap.return"))) - 10;
			boolean cursornear = isnearpoint();
			if (cursornear) TextRenderer.render(g, new Message("localmap.select"), textxpos, 20);
			TextRenderer.render(g, new Message("dungeonmap.return"), textxpos, cursornear ? 40 : 20);
		}
	}

	@Override
	public void update()
	{
		if (setmusic)
		{
			setmusic = false;
			Persistance.soundmanager.setBackgroundMusic(SoundsHolder.getSong("town.mp3"));
		}
		// Cursor
		float cursorspeed = 1.8f;
		float newcursorx = cursorx, newcursory = cursory;
		if (movingleft) newcursorx -= cursorspeed;
		if (movingright) newcursorx += cursorspeed;
		if (newcursorx >= 0 && newcursorx <= MapResources.LOCALMAP.getWidth()) cursorx = newcursorx;
		if (movingdown) newcursory += cursorspeed;
		if (movingup) newcursory -= cursorspeed;
		if (newcursory >= 0 && newcursory <= MapResources.LOCALMAP.getHeight()) cursory = newcursory;
		// Camera X
		float cameraspeed = 1.5f;
		float newcamerax = camerax;
		if (camerax < cursorx - 12) newcamerax += cameraspeed;
		else if (camerax > cursorx + 12) newcamerax -= cameraspeed;
		if (newcamerax < memorywidth / 2)
		{
			if (camerax < memorywidth / 2) camerax += cameraspeed;
		} else if (newcamerax > MapResources.LOCALMAP.getWidth() - (memorywidth / 2))
		{
			if (camerax > MapResources.LOCALMAP.getWidth() - (memorywidth / 2)) camerax -= cameraspeed;
		} else camerax = newcamerax;
		// Camera Y
		float newcameray = cameray;
		if (cameray < cursory - 12) newcameray += cameraspeed;
		else if (cameray > cursory + 12) newcameray -= cameraspeed;
		if (newcameray < memoryheight / 2)
		{
			if (cameray < memoryheight / 2) cameray += cameraspeed;
		} else if (newcameray > MapResources.LOCALMAP.getHeight() - (memoryheight / 2))
		{
			if (cameray > MapResources.LOCALMAP.getHeight() - (memoryheight / 2)) cameray -= cameraspeed;
		} else cameray = newcameray;
	}

	public void lockOn(LocalMapLocation location)
	{
		cursorx = location.x;
		cursory = location.y;
	}

	/** Predicate that returns true if the cursor is near a point on the map. */
	private boolean isnearpoint()
	{
		LocalMapLocation[] points = LocalMapLocation.values();
		for (int i = 0; i < points.length; i++)
			if (isnearpoint(points[i])) return true;
		return false;
	}

	/** Predicate that returns true if the cursor is near the parsed point on the map. */
	private boolean isnearpoint(LocalMapLocation point)
	{
		if (point.showsonfriendsmap && point.x > cursorx - thresholddistance && point.x < cursorx + thresholddistance && point.y > cursory - thresholddistance
				&& point.y < cursory + thresholddistance)
			return true;
		return false;
	}

	/** Predicate that returns true if the cursor is on the right side of the screen of this state, meaning the UI above should be displayed on the left instead. */
	public boolean shouldInvertUI()
	{
		return cursorx > MapResources.LOCALMAP.getWidth() - (this.memorywidth / 3);
	}

}
