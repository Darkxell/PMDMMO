package com.darkxell.common.mission;

import com.darkxell.common.Registries;
import com.darkxell.common.util.language.Message;

import java.util.ArrayList;
import java.util.Random;

/**
 * Structure that contains all the informations for a mission. A mission object
 * is immutable.
 */
public class Mission {

    public static final int TYPE_RESCUEME = 1;
    public static final int TYPE_RESCUEHIM = 2;
    public static final int TYPE_ESCORT = 3;
    public static final int TYPE_BRINGITEM = 4;
    public static final int TYPE_DEFEAT = 5;
    public static final int TYPE_FINDITEM = 6;

    /**
     * The difficulty of the mission. Note that the values of this string are
     * not restricted, so this should not be used to determine other
     * informations about a mission. Such info should be attributes of this
     * object.<br/>
     * TL:DR : for display purposes mainly.
     */
    private String difficulty = "";
    /**
     * The id of the dungeon where this mission takes place.
     */
    private int dungeonid = 1;
    /**
     * The floor at which you can complete this mission.
     */
    private int floor = 1;
    /**
     * The id of the first Pokemon. Usually, this is the one asking for rescue,
     * to escort...
     */
    private int pokemonid1 = 1;
    /**
     * The id of the secondary Pokemon for the missions that need one.
     */
    private int pokemonid2 = 4;
    /**
     * An itemID used for the missions that are resolved by finding or bringing
     * an item.
     */
    private int itemid = 2;
    /**
     * The list of rewards for this mission. This has its own structure.
     */
    private MissionReward rewards = new MissionReward();
    /**
     * The type of the mission. This defines what you have to do.
     */
    private int missiontype = TYPE_RESCUEME;
    /**
     * The mission flavor texts.
     */
    private MissionFlavourText flavor = null;

    /**
     * Creates a new Mission Object with the wanted attributes.
     */
    public Mission(String difficulty, int dungeonid, int floor, int pokemonid1, int pokemonid2, int itemid,
            MissionReward rewards, int missiontype) {
        if (difficulty != null) {
            this.difficulty = difficulty;
        }
        this.dungeonid = dungeonid;
        this.floor = floor;
        this.pokemonid1 = pokemonid1;
        this.pokemonid2 = pokemonid2;
        this.itemid = itemid;
        if (rewards != null) {
            this.rewards = rewards;
        }
        this.missiontype = missiontype;
    }

    /**
     * Creates a new Mission Object with the wanted attributes.
     */
    public Mission(String code) throws InvalidParammetersException {
        try {
            String[] splitted = code.split(":");
            this.difficulty = splitted[0];
            this.dungeonid = Integer.parseInt(splitted[1]);
            this.floor = Integer.parseInt(splitted[2]);
            this.pokemonid1 = Integer.parseInt(splitted[3]);
            this.pokemonid2 = Integer.parseInt(splitted[4]);
            this.itemid = Integer.parseInt(splitted[5]);
            this.rewards = new MissionReward(splitted[6]);
            this.missiontype = Integer.parseInt(splitted[7]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidParammetersException();
        }
    }

    private String stringrep = "";

    @Override
    public String toString() {
        if (stringrep != null && !stringrep.isEmpty()) {
            return stringrep;
        }
        // diff::did:floor:pkmn1:pkmn2:itemid:rewards:type
        return stringrep = difficulty + ":" + dungeonid + ":" + floor + ":" + pokemonid1 + ":" + pokemonid2 + ":" + itemid + ":"
                + rewards + ":" + missiontype;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getDungeonid() {
        return dungeonid;
    }

    public int getFloor() {
        return floor;
    }

    public int getPokemonid1() {
        return pokemonid1;
    }

    public int getPokemonid2() {
        return pokemonid2;
    }

    public int getItemid() {
        return itemid;
    }

    public MissionReward getRewards() {
        return rewards;
    }

    public int getMissiontype() {
        return missiontype;
    }

    public MissionFlavourText getMissionFlavor() {
        if (flavor == null) {
            flavor = new MissionFlavourText(this);
        }
        return flavor;
    }

    @Override
    public boolean equals(Object obj) {
        Mission x;
        if (obj instanceof Mission) {
            x = (Mission) obj;
        } else {
            return false;
        }
        return x.difficulty.equals(difficulty) && dungeonid == x.dungeonid && floor == x.floor
                && pokemonid1 == x.pokemonid1 && pokemonid2 == x.pokemonid2 && itemid == x.itemid
                && rewards.equals(x.rewards) && missiontype == x.missiontype;
    }

    @Override
    public int hashCode() {
        return dungeonid * pokemonid1 * pokemonid2 * missiontype * itemid * floor * difficulty.hashCode();
    }

    /**
     * Method used by the server to get the missions for the day.
     */
    public static ArrayList<Mission> getDailyMissions() {
        ArrayList<Mission> list = new ArrayList<>(10);
        int dungeonammount = 0;
        Random r = new Random();
        for (int dungeonid = 0; dungeonid < 100; dungeonid++) {
            switch (dungeonid) {
                case 1: // Tiny woods
                    dungeonammount = r(r, 2, 3);
                    for (int i = 0; i < dungeonammount; i++) {
                        try {
                            MissionReward rewards = new MissionReward(r(r, 20, 30), null, null, 5, null);
                            Mission mission = new Mission("E", dungeonid, r(r, 2, 3), r(r, 1, 376), r(r, 1, 376), 1, rewards,
                                    r(r, 1, 2));
                            list.add(mission);
                        } catch (InvalidParammetersException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2: // Thunderwave cave
                    dungeonammount = r(r, 2, 3);
                    for (int i = 0; i < dungeonammount; i++) {
                        try {
                            MissionReward rewards = new MissionReward(r(r, 30, 120), null, null, 5, null);
                            Mission mission = new Mission("E", dungeonid, r(r, 2, 4), r(r, 1, 376), r(r, 1, 376), 1, rewards,
                                    r(r, 1, 2));
                            list.add(mission);
                        } catch (InvalidParammetersException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 3: // Mt steel
                    dungeonammount = r(r, 2, 3);
                    for (int i = 0; i < dungeonammount; i++) {
                        try {
                            MissionReward rewards = new MissionReward(r(r, 150, 400), null, null, 10, null);
                            Mission mission = new Mission("D", dungeonid, r(r, 4, 7), r(r, 1, 376), r(r, 1, 376), 1, rewards,
                                    r(r, 1, 2));
                            list.add(mission);
                        } catch (InvalidParammetersException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
        return list;
    }

    /**
     * Returns a random number between a inclusive and b inclusive
     */
    private static int r(Random r, int a, int b) {
        return r.nextInt(b - a) + a;
    }
    
    
    // Behavior getters

	/** @return <code>true</code> if the 2 Pokemon IDs are used for this Mission. */
	public boolean has2Pokemon() {
		return this.getMissiontype() == TYPE_DEFEAT || this.getMissiontype() == TYPE_RESCUEHIM || this.getMissiontype() == TYPE_ESCORT;
	}

	/** @return The ID of the Client Pokemon. */
    public int getClientPokemon() {
    	return this.getPokemonid1();
    }

	/** @return The ID of the Target Pokemon, i.e. the Pokemon that will be found in the Dungeon if it appears there. */
	public int getTargetPokemon() {
		if (this.has2Pokemon()) return this.getPokemonid2();
		return this.getPokemonid1();
	}

	public String summary() {
		return "<mission> " + Registries.dungeons().find(this.getDungeonid()).name() + " " + new Message("mission.floor.short") +
                "<blue>" + this.getFloor()
				+ "</color>" + " - " + this.getMissionFlavor().getObjectiveText().toString() + " (" + this.getDifficulty() + ")";
	}

}
