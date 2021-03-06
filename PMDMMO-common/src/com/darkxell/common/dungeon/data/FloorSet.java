package com.darkxell.common.dungeon.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.darkxell.common.util.Pair;

/** Holds a set of Floors. */
@XmlRootElement(name = "floors")
@XmlAccessorType(XmlAccessType.FIELD)
public class FloorSet implements Comparable<FloorSet> {

    private static class IntegerPairAdapter extends XmlAdapter<String, Pair<Integer, Integer>> {
        @Override
        public String marshal(Pair<Integer, Integer> v) throws Exception {
            if (v == null)
                return null;
            return v.first + "," + v.second;
        }

        @Override
        public Pair<Integer, Integer> unmarshal(String v) throws Exception {
            if (v == null)
                return null;
            String[] parts = v.split(",");
            return new Pair<Integer, Integer>(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]));
        }
    }

    /** Lists of floors not part of this set. */
    @XmlElement
    private ArrayList<Integer> except = new ArrayList<>();

    /** Maps each start of parts with end of parts. */
    @XmlElement(name = "part")
    @XmlJavaTypeAdapter(IntegerPairAdapter.class)
    private ArrayList<Pair<Integer, Integer>> parts = new ArrayList<>();

    public FloorSet() {
    }

    public FloorSet(ArrayList<Pair<Integer, Integer>> parts, ArrayList<Integer> except) {
        this.parts = parts;
        this.except = except;
    }

    public FloorSet(int start, int end) {
        this.parts = new ArrayList<>();
        this.parts.add(new Pair<>(start, end));
        this.except = new ArrayList<>();
    }

    @Override
    public int compareTo(FloorSet o) {
        int[] alls = this.list(), allo = o.list();
        if (alls.length != 0 && allo.length != 0) {
            int first = Integer.compare(alls[0], allo[0]);
            if (first != 0)
                return first;
        }
        return Integer.compare(alls.length, allo.length);
    }

    /** @return True if this Set contains the input floor. */
    public boolean contains(int floor) {
        for (Pair<Integer, Integer> part : this.parts)
            if (floor >= part.first && floor <= part.second && !this.except.contains(floor))
                return true;
        return false;
    }

    /** @return A copy of this Floor set. */
    public FloorSet copy() {
        return new FloorSet(new ArrayList<>(this.parts), new ArrayList<>(this.except));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FloorSet))
            return false;
        return Arrays.equals(this.list(), ((FloorSet) obj).list());
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Integer> except() {
        return (ArrayList<Integer>) this.except.clone();
    }

    /** @return The number of Floors in this Set. */
    public int floorCount() {
        int count = 0;
        for (Pair<Integer, Integer> part : this.parts)
            count += part.second - part.first + 1; // 15 - 15 + 1 = 1 ; 15 - 18 + 1 = 4
        return count - this.except.size();
    }

    /** @return The list of Floors this Set holds. */
    public int[] list() {
        ArrayList<Integer> floors = new ArrayList<>();
        for (Pair<Integer, Integer> part : this.parts) {
            int floor = part.first;
            do {
                if (!this.except.contains(floor))
                    floors.add(floor);
                ++floor;
            } while (floor <= part.second);
        }

        floors.sort(Comparator.naturalOrder());
        int[] array = new int[floors.size()];
        for (int i = 0; i < array.length; ++i)
            array[i] = floors.get(i);
        return array;
    }

    public ArrayList<Pair<Integer, Integer>> parts() {
        return new ArrayList<>(this.parts);
    }

    public String toString() {
        String s = "";
        int i = 0;
        for (Pair<Integer, Integer> part : this.parts) {
            if (i != 0)
                s += ", ";
            ++i;
            if (part.first.equals(part.second))
                s += part.first;
            else
                s += "[" + part.first + "-" + part.second + "]";
        }

        if (!this.except.isEmpty()) {
            s += ", except ";
            for (Integer e : this.except)
                s += (this.except.get(0).equals(e) ? "" : ",") + e;
        }

        return s;
    }

}
