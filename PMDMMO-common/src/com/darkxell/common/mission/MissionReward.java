package com.darkxell.common.mission;

/**
 * Structure holding the rewards for a mission.
 */
public class MissionReward {

    /**
     * The amount of money this mission gives when completed.
     */
    private int money = 0;
    /**
     * The ids of the items given to you upon completion.
     */
    private int[] items = new int[0];
    /**
     * The quantity of items given for each item.
     */
    private int[] quantities = new int[0];
    /**
     * The amount of rescue points this mission gives.
     */
    private int points = 0;
    /**
     * The additional triggers this mission does upon completion. Usually nothing, but can sometimes unlock a friend
     * area, advance a quest...<br/>
     * Triggers must contain ONLY alphanumerical characters.
     */
    private String[] triggers = new String[0];

    /**
     * Creates new empty Mission rewards.
     */
    public MissionReward() {
    }

    /**
     * Creates a new Mission rewards bundle using the parsed attributes.
     */
    public MissionReward(int money, int[] items, int[] quantities, int points, String[] triggers)
            throws InvalidParammetersException {
        if (items != null && quantities != null && items.length != quantities.length)
            throw new InvalidParammetersException();
        this.money = money;
        if (items != null)
            this.items = items;
        if (quantities != null)
            this.quantities = quantities;
        this.points = points;
        if (triggers != null)
            this.triggers = triggers;
    }

    /**
     * Creates a new Mission rewards bundle using the compressed String code parsed.
     */
    public MissionReward(String code) throws InvalidParammetersException {
        try {
            if (code.contains(":"))
                throw new InvalidParammetersException();
            String[] splitted = code.split(",");
            if (splitted.length != 3 && splitted.length != 4)
                throw new InvalidParammetersException();
            this.money = Integer.parseInt(splitted[0]);
            if (splitted[1].length() >= 1) {
                String[] itemssplit = splitted[1].split(";");
                if (itemssplit.length % 2 != 0)
                    throw new InvalidParammetersException();

                this.items = new int[itemssplit.length / 2];
                this.quantities = new int[itemssplit.length / 2];
                for (int i = 0; i < itemssplit.length; i += 2) {
                    this.items[i / 2] = Integer.parseInt(itemssplit[i]);
                    this.quantities[i / 2] = Integer.parseInt(itemssplit[i + 1]);
                }
            }
            this.points = Integer.parseInt(splitted[2]);
            if (splitted.length == 4)
                this.triggers = splitted[3].split(";");
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidParammetersException();
        }
    }

    private String stringrep = "";

    @Override
    public String toString() {
        if (stringrep != null && !stringrep.isEmpty())
            return stringrep;
        // money,i1*q1*i2*q2,points,t1*t2
        String toreturn = money + ",";
        for (int i = 0; i < items.length; i++) {
            if (i != 0)
                toreturn += ";";
            toreturn += items[i] + ";" + quantities[i];
        }
        toreturn += "," + points;
        if (triggers.length > 0) {
            toreturn += ",";
            for (int i = 0; i < triggers.length; i++) {
                if (i != 0)
                    toreturn += ";";
                toreturn += triggers[i];
            }
        }
        stringrep = toreturn;
        return toreturn;
    }

    public int getMoney() {
        return money;
    }

    public int[] getItems() {
        return items;
    }

    public int[] getQuantities() {
        return quantities;
    }

    public int getPoints() {
        return points;
    }

    public String[] getTriggers() {
        return triggers;
    }

    @Override
    public boolean equals(Object obj) {
        MissionReward x;
        if (obj instanceof MissionReward) {
            x = (MissionReward) obj;
            if (x.money == money && x.items.length == items.length && x.quantities.length == quantities.length
                    && points == x.points && triggers.length == x.triggers.length) {
                for (int i = 0; i < items.length; i++)
                    if (x.items[i] != items[i])
                        return false;
                for (int i = 0; i < quantities.length; i++)
                    if (x.quantities[i] != quantities[i])
                        return false;
                for (int i = 0; i < triggers.length; i++)
                    if (!x.triggers[i].equals(triggers[i]))
                        return false;
                return true;
            }
        }
        return false;
    }
}
