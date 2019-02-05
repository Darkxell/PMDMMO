package com.darkxell.client.mechanics.freezone.entity;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.bank.BankDialog;
import com.darkxell.client.state.dialog.storage.StorageDialog;
import com.darkxell.client.state.menu.freezone.MissionBoardSelectionState;
import com.darkxell.common.util.Logger;

/**
 * Generate entities from freezones based on XML data.
 *
 * <p>
 * TODO: serialize or generalize entity behavior, whichever makes more sense
 * </p>
 */
public class FreezoneEntityFactory {
    private static class InteractiveEntity extends FreezoneEntity {
        {
            this.interactive = true;
        }
    }

    public static FreezoneEntity getEntity(Element el) {
        FreezoneEntity entity = createEntity(el.getAttributeValue("type"));
        entity.initialize(el);
        return entity;
    }

    private static FreezoneEntity createEntity(String type) {
        switch (type == null ? "null" : type) {
        case "debug":
            return new DebugEntity();
        case "flower":
            return new AnimatedFlowerEntity();
        case "flag":
            return new FlagEntity();
        case "sign":
            return new SignEntity();
        case "water":
            return new WaterSparklesEntity();
        case "npc":
            return new PokemonFreezoneEntity();
        case "bank":
            return new InteractiveEntity() {
                @Override
                public void onInteract() {
                    new BankDialog(Persistence.stateManager.getCurrentState()).start();
                }
            };
        case "storage":
            return new InteractiveEntity() {
                @Override
                public void onInteract() {
                    new StorageDialog(Persistence.stateManager.getCurrentState()).start();
                }
            };
        case "dialog":
            return new DialogEntity();
        case "mission":
            return new InteractiveEntity() {
                @Override
                public void onInteract() {
                    AbstractState state = Persistence.stateManager.getCurrentState();
                    Persistence.stateManager.setState(new MissionBoardSelectionState(state));
                }
            };
        case "crystal":
            return new CrystalEntity();
        case "null":
        default:
            Logger.w("Attempted to create freezone of nonexistent type " + type + ".");
            return new FreezoneEntity();
        }
    }
}
