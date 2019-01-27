package com.darkxell.client.mechanics.freezone.trigger;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.common.dbobject.DBPlayer;
import org.jdom2.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Conditional trigger zone that only plays when a certain story condition is reached.
 * <p>
 * Currently it is a very simple OR-Equals comparison as that is all we currently need, but if in the future more
 * complexity is required, this could use a DSL.
 */
public class StoryConditionTriggerZone extends TriggerZone {
    private Map<String, String> conditions;
    private Map<String, String> postConditions;
    private String cutscene;

    @Override
    protected void deserialize(Element el) {
        super.deserialize(el);

        this.conditions = getPropertyElement(el.getChild("if"));
        this.postConditions = getPropertyElement(el.getChild("then"));
        this.cutscene = el.getAttributeValue("cutscene");
    }

    private Map<String, String> getPropertyElement(Element el) {
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

    /**
     * Evaluate conditions of {@code long} type variables.
     *
     * <p>Currently, they are:</p>
     *
     * <ul>
     * <li>{@code moneyinbag} - Money currently held.</li>
     * <li>{@code moneyinbank} - Money in bank.</li>
     * </ul>
     */
    private boolean evaluateConditionType(DBPlayer player, String variable, long value) {
        long current;
        switch (variable) {
            case "moneyinbag":
                current = player.moneyinbag;
                break;
            case "moneyinbank":
                current = player.moneyinbank;
                break;
            default:
                throw new IllegalArgumentException("Unknown long condition " + variable + ".");
        }

        return current == value;
    }

    /**
     * Evaluate conditions of {@code int} type variables.
     *
     * <p>Currently, they are:</p>
     *
     * <ul>
     * <li>{@code storyposition} - Current story position.</li>
     * <li>{@code points} - Mission reward points.</li>
     * </ul>
     */
    private boolean evaluateConditionType(DBPlayer player, String variable, int value) {
        long current;
        switch (variable) {
            case "storyposition":
                current = player.storyposition;
                break;
            case "points":
                current = player.points;
                break;
            default:
                throw new IllegalArgumentException("Unknown int condition " + variable + ".");
        }

        return current == value;
    }

    /**
     * Evaluate conditions relating to {@link com.darkxell.client.launchable.Persistence#currentplayer the current
     * player}.
     */
    private boolean evaluateCondition(DBPlayer player, String variable, String value) {
        // Hopefully eventually we can just look these up in a hash map but until then we have to do this gross stuff.
        switch (variable) {
            case "moneyinbag":
            case "moneyinbank":
                return evaluateConditionType(player, variable, Long.parseLong(value));
            case "storyposition":
            case "points":
                return evaluateConditionType(player, variable, Integer.parseInt(value));
        }
        throw new IllegalArgumentException("Unknown condition " + variable + ".");
    }

    /**
     * Set conditions of {@code long} type variables.
     *
     * <p>Currently, they are:</p>
     *
     * <ul>
     * <li>{@code moneyinbag} - Money currently held.</li>
     * <li>{@code moneyinbank} - Money in bank.</li>
     * </ul>
     */
    private void setConditionType(DBPlayer player, String variable, long value) {
        switch (variable) {
            case "moneyinbag":
                player.moneyinbag = value;
                return;
            case "moneyinbank":
                player.moneyinbank = value;
                return;
        }
    }

    /**
     * Set conditions of {@code int} type variables.
     *
     * <p>Currently, they are:</p>
     *
     * <ul>
     * <li>{@code storyposition} - Current story position.</li>
     * <li>{@code points} - Mission reward points.</li>
     * </ul>
     */
    private void setConditionType(DBPlayer player, String variable, int value) {
        switch (variable) {
            case "storyposition":
                player.storyposition = value;
                return;
            case "points":
                player.points = value;
        }
    }

    /**
     * Set conditions relating to {@link com.darkxell.client.launchable.Persistence#currentplayer the current player}.
     */
    private void setCondition(DBPlayer player, String variable, String value) {
        // Hopefully eventually we can just look these up in a hash map but until then we have to do this gross stuff.
        switch (variable) {
            case "moneyinbag":
            case "moneyinbank":
                setConditionType(player, variable, Long.parseLong(value));
                return;
            case "storyposition":
            case "points":
                setConditionType(player, variable, Integer.parseInt(value));
        }
    }

    public boolean isActive() {
        DBPlayer player = Persistence.player.getData();
        for (Map.Entry<String, String> condition : this.conditions.entrySet()) {
            if (evaluateCondition(player, condition.getKey(), condition.getValue())) {
                return true;
            }
        }
        return false;
    }

    public void onEnter() {
        CutsceneManager.playCutscene(this.cutscene);

        DBPlayer player = Persistence.player.getData();
        for (Map.Entry<String, String> condition : this.postConditions.entrySet()) {
            this.setCondition(player, condition.getKey(), condition.getValue());
        }
    }
}
