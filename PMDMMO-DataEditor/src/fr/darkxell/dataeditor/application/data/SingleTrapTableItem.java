package fr.darkxell.dataeditor.application.data;

import com.darkxell.common.trap.Trap;

public class SingleTrapTableItem implements Comparable<SingleTrapTableItem> {

    public Trap trap;
    public Integer weight;

    public SingleTrapTableItem(Trap trap, Integer weight) {
        this.trap = trap;
        this.weight = weight;
    }

    @Override
    public int compareTo(SingleTrapTableItem o) {
        return Integer.compare(this.trap.id, o.trap.id);
    }

    public Trap getTrap() {
        return this.trap;
    }

    public String getWeight() {
        return String.valueOf(this.weight);
    }

}
