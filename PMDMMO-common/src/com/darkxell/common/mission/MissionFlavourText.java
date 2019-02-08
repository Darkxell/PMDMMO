package com.darkxell.common.mission;

import java.util.Random;

import com.darkxell.common.Registries;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.util.language.Message;

public class MissionFlavourText {

    private Mission source;

    public final String summaryid;
    public final String line1id;
    public final String line2id;

    /** Procedurally generated flavor text ids */
    public MissionFlavourText(Mission source) {
        this.source = source;
        Random cr = new Random(source.hashCode());
        int tid;
        switch (source.getMissiontype()) {
        case Mission.TYPE_RESCUEME:
            tid = cr.nextInt(15) + 1;
            summaryid = "mission.1.summary." + tid;
            tid = cr.nextInt(14) + 1;
            line1id = "mission.1.reason." + tid;
            tid = cr.nextInt(12) + 1;
            line2id = "mission.1.help." + tid;
            break;
        case Mission.TYPE_RESCUEHIM:
            tid = cr.nextInt(3) + 1;
            summaryid = "mission.2.summary." + tid;
            tid = cr.nextInt(6) + 1;
            line1id = "mission.2.help." + tid;
            tid = cr.nextInt(41) + 1;
            line2id = "mission.2.reason." + tid;
            break;
        case Mission.TYPE_ESCORT:
            tid = cr.nextInt(10) + 1;
            summaryid = "mission.3.summary." + tid;
            tid = cr.nextInt(6) + 1;
            line1id = "mission.3.task." + tid;
            tid = cr.nextInt(23) + 1;
            line2id = "mission.3.reason." + tid;
            break;
        case Mission.TYPE_BRINGITEM:
            tid = cr.nextInt(10) + 1;
            summaryid = "mission.4.summary." + tid;
            tid = cr.nextInt(10) + 1;
            line1id = "mission.4.summary." + tid;
            tid = cr.nextInt(6) + 1;
            line2id = "mission.4.reason." + tid;
            break;
        case Mission.TYPE_DEFEAT:
            tid = cr.nextInt(7) + 1;
            summaryid = "mission.5.summary." + tid;
            line1id = "mission.5.summary." + tid;
            tid = cr.nextInt(4) + 1;
            line2id = "mission.5.reward." + tid;
            break;
        case Mission.TYPE_FINDITEM:
            tid = cr.nextInt(5) + 1;
            summaryid = "mission.6.summary." + tid;
            line1id = "mission.6.task";
            tid = cr.nextInt(4) + 1;
            line2id = "mission.6.reason." + tid;
            break;

        default:
            summaryid = "mission.errorflavor";
            line1id = "mission.errorflavor";
            line2id = "mission.errorflavor";
            break;
        }
    }

    public Message getObjectiveText() {
        PokemonRegistry species = Registries.species();
        ItemRegistry items = Registries.items();
        return new Message("mission.objective." + this.source.getMissiontype())
                .addReplacement("<p1>", species.find(source.getPokemonid1()).speciesName())
                .addReplacement("<p2>", species.find(source.getPokemonid2()).speciesName())
                .addReplacement("<i>", items.find(source.getItemid()).name());
    }

}
