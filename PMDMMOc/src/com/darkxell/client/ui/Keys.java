package com.darkxell.client.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.ClientSettings.Setting;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;

import static com.darkxell.client.launchable.ClientSettings.Setting.*;

public class Keys implements KeyListener {
    public enum Key {
        /**
         * Movement direction keys.
         */
        UP(KEY_UP),
        DOWN(KEY_DOWN),
        LEFT(KEY_LEFT),
        RIGHT(KEY_RIGHT),

        /**
         * Attack keys.
         */
        MOVE_1(KEY_MOVE_1),
        MOVE_2(KEY_MOVE_2),
        MOVE_3(KEY_MOVE_3),
        MOVE_4(KEY_MOVE_4),

        /**
         * Free roam camera keys.
         */
        MAP_UP(KEY_MAP_UP),
        MAP_DOWN(KEY_MAP_DOWN),
        MAP_LEFT(KEY_MAP_LEFT),
        MAP_RIGHT(KEY_MAP_RIGHT),
        MAP_RESET(KEY_MAP_RESET),

        ATTACK(KEY_ATTACK),
        DIAGONAL(KEY_DIAGONAL),
        INVENTORY(KEY_INVENTORY),
        ITEM_1(KEY_ITEM_1),
        ITEM_2(KEY_ITEM_2),
        MENU(KEY_MENU),
        PAGE_LEFT(KEY_PAGE_LEFT),
        PAGE_RIGHT(KEY_PAGE_RIGHT),
        PARTY(KEY_PARTY),
        ROTATE(KEY_ROTATE),
        RUN(KEY_RUN),

        UNKNOWN(null);

        public final Setting setting;
        int value;

        boolean isPressed;
        boolean wasPressed;
        boolean willPress;

        public static Key getKeyFromID(int keyID) {
            for (Key key : Key.values()) {
                if (key.value == keyID) {
                    return key;
                }
            }
            Key.UNKNOWN.value = keyID;
            return Key.UNKNOWN;
        }

        Key(Setting setting) {
            this.isPressed = false;
            this.wasPressed = false;
            this.willPress = false;

            if (setting == null) {
                this.setting = null;
                return;
            }

            this.setting = setting;
            String rawValue = ClientSettings.getSetting(setting);

            try {
                this.value = Integer.parseInt(rawValue);
            } catch (NumberFormatException e) {
                Logger.e("Key " + setting.key + " has invalid value " + rawValue + ".");
                Logger.i("Using default of " + setting.defaultValue + " instead.");

                ClientSettings.resetSetting(setting);
            }
        }

        public Message getName() {
            return new Message(this.setting == null ? "unknown key" : this.setting.key);
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
            if (this.setting != null) {
                ClientSettings.setSetting(this.setting, String.valueOf(this.value));
            }
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

    public static void update() {
        for (Key key : Key.values()) {
            key.wasPressed = key.isPressed;
            key.isPressed = key.willPress;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Key key = Key.getKeyFromID(e.getKeyCode());
        if (!key.isPressed()) {
            if (key != Key.UNKNOWN) {
                key.willPress = true;
            }
            Persistence.stateManager.onKeyPressed(e, key);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Key key = Key.getKeyFromID(e.getKeyCode());
        if (key != Key.UNKNOWN) {
            key.willPress = false;
        }
        Persistence.stateManager.onKeyReleased(e, key);
    }

}
