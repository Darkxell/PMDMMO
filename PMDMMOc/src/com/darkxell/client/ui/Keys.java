package com.darkxell.client.ui;

import static com.darkxell.client.launchable.ClientSettings.Setting.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.ClientSettings.Setting;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;

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

        private boolean isPressed;
        private boolean wasPressed;
        private boolean willPress;

        private int lastAssigned;

        public final Setting setting;

        public static Key getKeyFromID(int keyID) {
            for (Key key : Key.values()) {
                if (key.keyValue() == keyID) {
                    return key;
                }
            }
            Key.UNKNOWN.lastAssigned = keyID;
            return Key.UNKNOWN;
        }

        Key(Setting setting) {
            this.isPressed = false;
            this.wasPressed = false;
            this.willPress = false;
            this.lastAssigned = -1;
            this.setting = setting;
        }

        public Message getName() {
            return new Message(this.setting == null ? "unknown key" : this.setting.key);
        }

		public boolean isPressed() {
			if (Persistence.stateManager instanceof PrincipalMainState
					&& ((PrincipalMainState) Persistence.stateManager).isChatFocused())
				return false;
			return this.isPressed;
		}

		public int keyValue() {
            if (this.setting == null) {
                return this.lastAssigned;
            }

            String rawValue = ClientSettings.getSetting(this.setting);

            try {
                return Integer.parseInt(rawValue);
            } catch (NumberFormatException e) {
                String name = this.getName().toString();

                // guaranteed to return a valid int since the default value is created from an int
                // if not, just let it fail
                int defaultKeybind = Integer.parseInt(this.setting.defaultValue);
                ClientSettings.resetSetting(setting);

                Logger.e("Invalid value for " + name + ": " + rawValue);
                Logger.w("Using default value of " + defaultKeybind + " for key " + name);

                return defaultKeybind;
            }
		}

        public void setValue(int value) {
            System.out.println("tried to set " + this.getName() + " with " + value);
            if (this.setting != null) {
                ClientSettings.setSetting(this.setting, String.valueOf(value));
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
            return runDirection(targetKeys);
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

    /**
     * Flip key and fire event if the key's new state is different.
     *
     * @param e Key event from a {@link #keyPressed(KeyEvent) press} or {@link #keyReleased(KeyEvent) release} event.
     * @param newState New state.
     * @param cb Statement to execute, if necessary.
     */
    private void process(KeyEvent e, boolean newState, Consumer<Key> cb) {
        Key key = Key.getKeyFromID(e.getKeyCode());
        if (key.isPressed != newState) {
            if (key != Key.UNKNOWN) {
                key.willPress = !key.willPress;
            }
            cb.accept(key);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.process(e, true, k -> Persistence.stateManager.onKeyPressed(e, k));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.process(e, false, k -> Persistence.stateManager.onKeyReleased(e, k));
    }

    @Override
    public void keyTyped(KeyEvent e) {
        Persistence.stateManager.onKeyTyped(e);
    }
}
