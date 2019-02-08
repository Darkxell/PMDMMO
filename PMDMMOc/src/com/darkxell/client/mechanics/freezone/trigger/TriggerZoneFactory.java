package com.darkxell.client.mechanics.freezone.trigger;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.TransitionState;
import com.darkxell.client.state.menu.freezone.DungeonSelectionMapState;
import com.darkxell.client.state.menu.freezone.FriendAreaSelectionMapState;
import com.darkxell.client.state.menu.freezone.FriendSelectionState;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

/**
 * Trigger zone deserialization utility class.
 *
 * <p>
 * All trigger zones have "hitbox" attributes on their root, i.e. {@code x}, {@code y}, {@code width}, and {@code
 * height}, used as parameters to the constructor of {@link DoubleRectangle}, and a {@code type} attribute, specifying
 * the type of trigger zone to create.
 * </p>
 *
 * <h3>Trigger zone types</h3>
 *
 * <h4>{@link WarpZone Warp zones} (type {@code warp})</h4>
 *
 * <p>
 * The default type of trigger zones. They simply warp you to another freezone.
 * </p>
 *
 * <p>
 * Zones without a type are automatically designated this type.
 * </p>
 *
 * <h5>Additional properties</h5>
 *
 * <ul>
 * <li>Destination ({@code dest}) - see {@link FreezoneInfo} for more details.</li>
 * <li>Destination coordinates ({@code destx} and {@code desty}) - Where on the destination map to warp to. If left off,
 * this will spawn the player at the default x and y positions.</li>
 * <li>Destination coordinates ({@code direction} - Which direction to face initially. Direction numbers correspond to
 * the indices of {@link Direction#DIRECTIONS}.</li>
 * </ul>
 *
 * <h4>Dungeon and friend zone selection zones (types {@code dungeon} and {@code friend})</h4>
 *
 * <p>
 * Activates the dungeon and friend zone selection screens, respectively.
 * </p>
 *
 * <h5>{@link MultiTriggerZone Multi-condition zones} (type {@code multi})</h5>
 *
 * <p>
 * Activate multiple trigger zones based on conditions. All children {@code trigger} elements will be evaluated as
 * trigger zones. Hitbox coordinates will be inherited.
 * </p>
 *
 * <h5>Additional properties</h5>
 *
 * <ul>
 * <li>Fallthrough ({@code fallthrough}) - If one condition is true, should the others still be evaluated?</li>
 * </ul>
 *
 * <h4>{@link StoryConditionTriggerZone Story zones} (type {@code story})</h4>
 *
 * <p>
 * Conditional zones that only activate when a certain story position is reached. The conditions to activate are
 * contained in a child {@code <if />} tag, and properties and the values to set them to after the trigger zone is
 * activated are contained in a child {@code <then />} tag. Property tags are always in the format {@code <property
 * key="" val="" />}. If used as a top level trigger zone, these zones will always be triggered.
 * </p>
 *
 * <h5>Additional properties</h5>
 *
 * <ul>
 * <li>Cutscene ID ({@code cutscene}) - Which cutscene to play upon valid condition.</li>
 * </ul>
 */
public class TriggerZoneFactory {
    public static TriggerZone getZone(Element el) {
        return getZone(null, el);
    }

    static TriggerZone getZone(TriggerZone context, Element el) {
        String type = el.getAttributeValue("type");
        TriggerZone trigger = createTriggerZone(type);
        if (context != null)
            trigger.setContext(context);
        trigger.initialize(el);
        return trigger;
    }

    private static TriggerZone createTriggerZone(String type) {
        switch (type == null ? "warp" : type) {
        case "multi":
            return new MultiTriggerZone();
        case "friend":
            return new TriggerZone() {
                @Override
                public void onEnter() {
                    AbstractState state = Persistence.stateManager.getCurrentState();
                    Persistence.stateManager.setState(new FriendSelectionState(state));
                }
            };
        case "friendmap":
            return new TriggerZone() {
                @Override
                public void onEnter() {
                    FriendAreaSelectionMapState state = new FriendAreaSelectionMapState();
                    if (Persistence.currentmap != null)
                        state.lockOn(Persistence.currentmap.info.maplocation);
                    Persistence.stateManager
                            .setState(new TransitionState(Persistence.stateManager.getCurrentState(), state));
                }
            };
        case "dungeon":
            return new TriggerZone() {
                @Override
                public void onEnter() {
                    Persistence.stateManager.setState(new DungeonSelectionMapState());
                }
            };
        case "story":
            return new StoryConditionTriggerZone();
        case "warp":
        default:
            return new WarpZone();
        }
    }
}
