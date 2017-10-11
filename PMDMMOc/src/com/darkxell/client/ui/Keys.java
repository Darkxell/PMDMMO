package com.darkxell.client.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.common.util.Logger;

public class Keys implements KeyListener {

	public static final short KEY_COUNT = 22;
	private static boolean[] isPressed = new boolean[KEY_COUNT];

	/**
	 * Keys.<br />
	 * <ul>
	 * <li>KEY_UP = 0</li>
	 * <li>KEY_DOWN = 1</li>
	 * <li>KEY_LEFT = 2</li>
	 * <li>KEY_RIGHT = 3</li>
	 * <li>KEY_ATTACK = 4</li>
	 * <li>KEY_ROTATE = 5</li>
	 * <li>KEY_RUN = 6</li>
	 * <li>KEY_DIAGONAL = 7</li>
	 * <li>KEY_MENU = 8</li>
	 * <li>KEY_MOVE_1 = 9</li>
	 * <li>KEY_MOVE_2 = 10</li>
	 * <li>KEY_MOVE_3 = 11</li>
	 * <li>KEY_MOVE_4 = 12</li>
	 * <li>KEY_ITEM_2 = 13</li>
	 * <li>KEY_ITEM_1 = 14</li>
	 * <li>KEY_INVENTORY = 15</li>
	 * <li>KEY_PARTY = 16</li>
	 * <li>KEY_MAP_UP = 17</li>
	 * <li>KEY_MAP_DOWN = 18</li>
	 * <li>KEY_MAP_LEFT = 19</li>
	 * <li>KEY_MAP_RIGHT = 20</li>
	 * <li>KEY_MAP_RESET = 21</li>
	 * </ul>
	 */
	public static final short KEY_UP = 0, KEY_DOWN = 1, KEY_LEFT = 2, KEY_RIGHT = 3, KEY_ATTACK = 4, KEY_ROTATE = 5, KEY_RUN = 6, KEY_DIAGONAL = 7,
			KEY_MENU = 8, KEY_MOVE_1 = 9, KEY_MOVE_2 = 10, KEY_MOVE_3 = 11, KEY_MOVE_4 = 12, KEY_ITEM_1 = 13, KEY_ITEM_2 = 14, KEY_INVENTORY = 15,
			KEY_PARTY = 16, KEY_MAP_UP = 17, KEY_MAP_DOWN = 18, KEY_MAP_LEFT = 19, KEY_MAP_RIGHT = 20, KEY_MAP_RESET = 21;

	/** User-defined keys. */
	private static int[] keys;
	/** Key names. */
	private static String[] NAMES = new String[] { "up", "down", "left", "right", "attack", "rotate", "run", "diagonal", "menu", "move1", "move2", "move3", "move4", "item1", "item2", "inventory", "party",
			"map.up", "map.down", "map.left", "map.right", "map.reset" };

	/**
	 * @param keyID
	 *            - The ID of the pressed key. See {@link KeyEvent}
	 * @return The {@link Keys#KEY_UP Key} that was pressed. -1 if doesn't match
	 *         a key for this game.
	 */
	public static short getKeyFromID(int keyID) {
		for (int i = 0; i < keys.length; ++i)
			if (keyID == keys[i])
				return (short) i;
		return -1;
	}

	/**
	 * @return the name of the input key.
	 * @see Keys#KEY_UP
	 */
	public static String getKeyName(short key) {
		return NAMES[key];
	}

	/**
	 * @return true if the input key is pressed.
	 * @see Keys#KEY_UP
	 */
	public static boolean isPressed(short key) {
		return isPressed[key];
	}

	public Keys() {
		keys = new int[KEY_COUNT];
		for (int i = 0; i < KEY_COUNT; ++i) {
			String s = "key." + NAMES[i];
			try {
				keys[i] = Integer.parseInt(ClientSettings.getSetting(s));
			} catch (Exception e) {
				Logger.e("Invalid key ID: " + ClientSettings.getSetting(s));
				ClientSettings.resetSetting(s);
				keys[i] = Integer.parseInt(ClientSettings.getSetting(s));
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		short key = getKeyFromID(e.getKeyCode());
		if (key != -1 && !isPressed(key)) {
			isPressed[key] = true;
			Persistance.stateManager.onKeyPressed(e, key);
		} else
			Persistance.stateManager.onKeyPressed(e, (short) -1);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		short key = getKeyFromID(e.getKeyCode());
		if (key != -1) {
			isPressed[key] = false;
			Persistance.stateManager.onKeyReleased(e, key);
		} else
			Persistance.stateManager.onKeyReleased(e, (short) -1);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		Persistance.stateManager.onKeyTyped(e);
	}

}
