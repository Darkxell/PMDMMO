package com.darkxell.client.mechanics.freezones.trigger;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.freezone.DungeonSelectionMapState;
import com.darkxell.client.state.menu.freezone.FriendSelectionState;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.zones.FreezoneInfo;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Trigger zone deserialization utility class.
 *
 * <p>All trigger zones have "hitbox" attributes on their root, i.e. {@code x}, {@code y}, {@code width}, and {@code
 * height}, used as parameters to the constructor of {@link DoubleRectangle}, and a {@code type} attribute,
 * specifying the type of trigger zone to create.</p>
 *
 * <h3>Trigger zone types</h3>
 *
 * <h4>{@link WarpZone Warp zones} (type {@code warp})</h4>
 *
 * <p>The default type of trigger zones. They simply warp you to another freezone.</p>
 *
 * <p>Zones without a type are automatically designated this type.</p>
 *
 * <h5>Additional properties</h5>
 *
 * <ul>
 * <li>Destination ({@code dest}) - see {@link FreezoneInfo} for more details.</li>
 * <li>Destination coordinates ({@code destx} and {@code desty}) - Where on the destination map to warp to. If left
 * off, this will spawn the player at the default x and y positions.</li>
 * <li>Destination coordinates ({@code direction} - Which direction to face initially. Direction numbers
 * correspond to the indices of {@link Direction#directions}.</li>
 * </ul>
 *
 * <h4>Dungeon and friend zone selection zones (types {@code dungeon} and {@code friend})</h4>
 *
 * <p>Activates the dungeon and friend zone selection screens, respectively.</p>
 *
 * <h5>{@link MultiTriggerZone Multi-condition zones} (type {@code multi})</h5>
 *
 * <p>Activate multiple trigger zones based on conditions. All children {@code trigger} elements will be evaluated
 * as trigger zones. Hitbox coordinates will be inherited.</p>
 *
 * <h5>Additional properties</h5>
 *
 * <ul>
 * <li>Fallthrough ({@code fallthrough}) - If one condition is true, should the others still be evaluated?</li>
 * </ul>
 *
 * <h4>{@link StoryConditionTriggerZone Story zones} (type {@code story})</h4>
 *
 * <p>Conditional zones that only activate when a certain story position is reached. The conditions to activate are
 * contained in a child {@code <if />} tag, and properties and the values to set them to after the trigger zone is
 * activated are contained in a child {@code <then />} tag. Property tags are always in the format {@code <property
 * key="" val="" />}. If used as a top level trigger zone, these zones will always be triggered.</p>
 *
 * <h5>Additional properties</h5>
 *
 * <ul>
 * <li>Cutscene ID ({@code cutscene}) - Which cutscene to play upon valid condition.</li>
 * </ul>
 */
public class TriggerZoneFactory {
    public static TriggerZone getZone(Element el) {
        String type = el.getAttributeValue("type");
        DoubleRectangle hitbox = getHitbox(el);
        return createTriggerZone(type, hitbox, el);
    }

    /**
     * Trigger zone creator with the ability to supply top-level properties.
     *
     * @param type   Type of trigger zone to create.
     * @param hitbox Hitbox of the trigger zone.
     * @param el     Remaining properties.
     * @return New trigger zone.
     */
    private static TriggerZone createTriggerZone(String type, DoubleRectangle hitbox, Element el) {
        switch (type == null ? "warp" : type) {
            case "multi":
                return createMultiZone(hitbox, el);
            case "friend":
                return new TriggerZone(hitbox) {
                    @Override
                    public void onEnter() {
                        AbstractState state = Persistence.stateManager.getCurrentState();
                        Persistence.stateManager.setState(new FriendSelectionState(state));
                    }
                };
            case "dungeon":
                return new TriggerZone(hitbox) {
                    @Override
                    public void onEnter() {
                        Persistence.stateManager.setState(new DungeonSelectionMapState());
                    }
                };
            case "story":
                return createStoryZone(hitbox, el);
            case "warp":
            default:
                int x = XMLUtils.getAttribute(el, "destx", -1);
                int y = XMLUtils.getAttribute(el, "desty", -1);
                int direction = XMLUtils.getAttribute(el, "direction", 4);
                FreezoneInfo destination = getDestination(el);
                return new WarpZone(x, y, destination, Direction.directions[direction], hitbox);
        }
    }

    private static DoubleRectangle getHitbox(Element el) {
        return inheritHitbox(null, el);
    }

    private static DoubleRectangle inheritHitbox(DoubleRectangle parent, Element el) {
        DoubleRectangle target;
        if (parent == null) {
            target = new DoubleRectangle(0, 0, 0, 0);
        } else {
            target = new DoubleRectangle(parent);
        }

        target.x = XMLUtils.getAttribute(el, "x", target.x);
        target.y = XMLUtils.getAttribute(el, "y", target.y);
        target.width = XMLUtils.getAttribute(el, "width", target.width);
        target.height = XMLUtils.getAttribute(el, "height", target.height);
        return target;
    }

    private static FreezoneInfo getDestination(Element el) {
        String destination = el.getAttributeValue("dest");
        if (destination == null) {
            throw new IllegalArgumentException("There is no destination for this warp zone.");
        }
        return FreezoneInfo.find(destination);
    }

    private static TriggerZone createMultiZone(DoubleRectangle hitbox, Element el) {
        boolean fallthrough = XMLUtils.getAttribute(el, "fallthrough", false);
        List<TriggerZone> childZones = new ArrayList<>();
        for (Element triggerEl : el.getChildren("trigger")) {
            childZones.add(createTriggerZone(triggerEl.getAttributeValue("type"), inheritHitbox(hitbox, triggerEl), triggerEl));
        }

        return new MultiTriggerZone(hitbox, childZones.toArray(new TriggerZone[0]), fallthrough);
    }

    private static Map<String, String> getPropertyElement(Element el) {
        Map<String, String> properties = new HashMap<>();

        if (el != null) {
            for (Element propertyEl : el.getChildren("property")) {
                String variable = propertyEl.getAttributeValue("key");
                String value = propertyEl.getAttributeValue("val");
                if (variable != null && value != null) {
                    properties.put(variable, value);
                }
            }
        }
        return properties;
    }

    private static TriggerZone createStoryZone(DoubleRectangle hitbox, Element el) {
        Map<String, String> conditions = getPropertyElement(el.getChild("if"));
        Map<String, String> postConditions = getPropertyElement(el.getChild("then"));
        String cutscene = el.getAttributeValue("cutscene");

        return new StoryConditionTriggerZone(hitbox, conditions, postConditions, cutscene);
    }
}
