package com.darkxell.client.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.ClientSettings.Setting;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;

public class Keys implements KeyListener {
	public enum Key {
		ATTACK("attack"),
		DIAGONAL("diagonal"),
		DOWN("down"),
		INVENTORY("inventory"),
		ITEM_1("item1"),
		ITEM_2("item2"),
		LEFT("left"),
		MAP_DOWN("map.down"),
		MAP_LEFT("map.left"),
		MAP_RESET("map.reset"),
		MAP_RIGHT("map.right"),
		MAP_UP("map.up"),
		MENU("menu"),
		MOVE_1("move1"),
		MOVE_2("move2"),
		MOVE_3("move3"),
		MOVE_4("move4"),
		PAGE_LEFT("page.left"),
		PAGE_RIGHT("page.right"),
		PARTY("party"),
		RIGHT("right"),
		ROTATE("rotate"),
		RUN("run"),
		UNKNOWN("unknown"),
		UP("up");

		boolean isPressed;
		public final String nameID;
		int value;
		boolean wasPressed;
		boolean willPress;

		Key(String nameID)
		{
			this.nameID = nameID;
		}

		public Message getName() {
			return new Message("key." + this.nameID);
		}

		public boolean isPressed() {
			if (Persistance.stateManager instanceof PrincipalMainState
					&& ((PrincipalMainState) Persistance.stateManager).isChatFocused())
				return false;
			return this.isPressed;
		}

		public int keyValue() {
			return this.value;
		}

		public void setValue(int value) {
			this.value = value;
			ClientSettings.setSetting(Setting.getByKey("key." + this.nameID), String.valueOf(this.value));
		}
	}

    /**
     * TODO: delegate run event detection to dungeon state. somehow.
     * @return If a run event should take place.
     */
    private static boolean runDirection(Key[] targetKeys) {
        for (Key key: targetKeys) {
            if (key.wasPressed) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a set of keys corresponding to a direction is currently activated.
     * <p/>
     * If the {@link Key#RUN RUN} key is pressed, check that the key was not already pressed - this will only return
     * true once per run cycle (i.e. press run, release run).
     *
     * @param canRun     - True if the RUN key should be checked.
     * @param targetKeys - List of keys that correspond to a "direction".
     */
    public static boolean directionPressed(boolean canRun, Key... targetKeys) {
        if (Key.RUN.isPressed && canRun) {
            runDirection(targetKeys);
        }

        // check that the key combination matches the pressed list exactly
        List<Key> targetKeyList = Arrays.asList(targetKeys);

        for (Key key : new Key[]{Key.UP, Key.DOWN, Key.LEFT, Key.RIGHT}) {
            if (targetKeyList.contains(key) != key.isPressed) {
                return false;
            }
        }

        return true;
    }

	/** @param keyID - The ID of the pressed key. See {@link KeyEvent}
	 * @return The {@link Keys#UP Key} that was pressed. -1 if doesn't match a key for this game. */
	public static Key getKeyFromID(int keyID) {
		for (Key key : Key.values())
			if (key.value == keyID) return key;
		Key.UNKNOWN.value = keyID;
		return Key.UNKNOWN;
	}

	public static void update() {
		for (Key key : Key.values()) {
			key.wasPressed = key.isPressed;
			key.isPressed = key.willPress;
		}
	}

	public Keys() {
		for (Key key : Key.values())
			if (key != Key.UNKNOWN) {
				Setting s = Setting.getByKey("key." + key.nameID);
				String rawKey = ClientSettings.getSetting(s);
				try {
					key.value = Integer.parseInt(rawKey);
				} catch (NumberFormatException e) {
					Logger.e("Invalid key ID: " + rawKey);
					ClientSettings.resetSetting(s);
					key.value = Integer.parseInt(ClientSettings.getSetting(s));
				}
			}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Key key = getKeyFromID(e.getKeyCode());
		if (!key.isPressed()) {
			if (key != Key.UNKNOWN) key.willPress = true;
			Persistance.stateManager.onKeyPressed(e, key);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Key key = getKeyFromID(e.getKeyCode());
		if (key != Key.UNKNOWN) key.willPress = false;
		Persistance.stateManager.onKeyReleased(e, key);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		Persistance.stateManager.onKeyTyped(e);
	}

}
