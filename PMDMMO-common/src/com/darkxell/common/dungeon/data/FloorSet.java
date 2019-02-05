package com.darkxell.common.dungeon.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.common.util.Pair;
import com.darkxell.common.util.xml.XMLUtils;

/** Holds a set of Floors. */
public class FloorSet implements Comparable<FloorSet> {
    public static final String XML_ROOT = "floors";

    /** Lists of floors not part of this set. */
    private ArrayList<Integer> except;
    /** Maps each start of parts with end of parts. */
    private HashMap<Integer, Integer> parts;

    public FloorSet(Element xml) {
        this.parts = new HashMap<Integer, Integer>();
        this.except = XMLUtils.readIntArrayAsList(xml.getChild("except", xml.getNamespace()));
        for (Element part : xml.getChildren("part", xml.getNamespace()))
            if (part.getAttribute("floor") != null)
                this.parts.put(Integer.parseInt(part.getAttributeValue("floor")),
                        Integer.parseInt(part.getAttributeValue("floor")));
            else
                this.parts.put(Integer.parseInt(part.getAttributeValue("start")),
                        Integer.parseInt(part.getAttributeValue("end")));
    }

    public FloorSet(HashMap<Integer, Integer> parts, ArrayList<Integer> except) {
        this.parts = parts;
        this.except = except;
    }

    public FloorSet(int start, int end) {
        this.parts = new HashMap<Integer, Integer>();
        this.parts.put(start, end);
        this.except = new ArrayList<Integer>();
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
        for (Integer start : this.parts.keySet())
            if (floor >= start && floor <= this.parts.get(start) && !this.except.contains(floor))
                return true;
        return false;
    }

    /** @return A copy of this Floor set. */
    public FloorSet copy() {
        HashMap<Integer, Integer> p = new HashMap<Integer, Integer>();
        for (Integer start : this.parts.keySet())
            p.put(start, this.parts.get(start));

        ArrayList<Integer> e = new ArrayList<Integer>();
        e.addAll(this.except);

        return new FloorSet(p, e);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Integer> except() {
        return (ArrayList<Integer>) this.except.clone();
    }

    /** @return The number of Floors in this Set. */
    public int floorCount() {
        int count = 0;
        for (Integer start : this.parts.keySet())
            count += this.parts.get(start) - start + 1; // 15 - 15 + 1 = 1 ; 15 - 18 + 1 = 4
        return count - this.except.size();
    }

    /** @return The list of Floors this Set holds. */
    public int[] list() {
        ArrayList<Integer> floors = new ArrayList<Integer>();
        for (Integer start : this.parts.keySet()) {
            int floor = start;
            do {
                if (!this.except.contains(floor))
                    floors.add(floor);
                ++floor;
            } while (floor <= this.parts.get(start));
        }

        floors.sort(Comparator.naturalOrder());
        int[] array = new int[floors.size()];
        for (int i = 0; i < array.length; ++i)
            array[i] = floors.get(i);
        return array;
    }

    @SuppressWarnings("unchecked")
    public HashMap<Integer, Integer> parts() {
        return (HashMap<Integer, Integer>) this.parts.clone();
    }

    public ArrayList<Pair<Integer, Integer>> partsAsArray() {
        ArrayList<Pair<Integer, Integer>> parts = new ArrayList<>();
        for (Integer key : this.parts.keySet())
            parts.add(new Pair<>(key, this.parts.get(key)));
        parts.sort(Pair.integerComparator);
        return parts;
    }

    public String toString() {
        String s = "";
        int i = 0;
        for (Pair<Integer, Integer> part : this.partsAsArray()) {
            if (i != 0)
                s += ", ";
            ++i;
            if (part.first == part.second)
                s += part.first;
            else
                s += "[" + part.first + "-" + part.second + "]";
        }

        if (!this.except.isEmpty()) {
            s += ", except ";
            for (Integer e : this.except)
                s += (this.except.get(0) == e ? "" : ",") + e;
        }

        return s;
    }

    public Element toXML() {
        Element root = new Element(XML_ROOT);
        for (Integer start : this.parts.keySet())
            if (start.intValue() == this.parts.get(start).intValue())
                root.addContent(new Element("part").setAttribute("floor", Integer.toString(start)));
            else
                root.addContent(new Element("part").setAttribute("start", Integer.toString(start)).setAttribute("end",
                        Integer.toString(this.parts.get(start))));
        if (this.except.size() != 0)
            root.addContent(XMLUtils.toXML("except", this.except));
        return root;
    }

}
