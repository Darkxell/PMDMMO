package com.darkxell.common.dungeon.data;

import java.util.ArrayList;
import java.util.Comparator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.jdom2.Element;

import com.darkxell.common.util.Pair;
import com.darkxell.common.util.XMLUtils;

/** Holds a set of Floors. */
@XmlRootElement(name = "floors")
@XmlAccessorType(XmlAccessType.FIELD)
public class FloorSet implements Comparable<FloorSet> {

    private static class IntegerPairAdapter extends XmlAdapter<String, Pair<Integer, Integer>> {
        @Override
        public Pair<Integer, Integer> unmarshal(String v) throws Exception {
            if (v == null)
                return null;
            String[] parts = v.split(",");
            return new Pair<Integer, Integer>(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]));
        }

        @Override
        public String marshal(Pair<Integer, Integer> v) throws Exception {
            if (v == null)
                return null;
            return v.first + "," + v.second;
        }
    }

    public static final String XML_ROOT = "floors";

    /** Lists of floors not part of this set. */
    @XmlElement
    private ArrayList<Integer> except = new ArrayList<>();

    /** Maps each start of parts with end of parts. */
    @XmlElement(name = "part")
    @XmlJavaTypeAdapter(IntegerPairAdapter.class)
    private ArrayList<Pair<Integer, Integer>> parts = new ArrayList<>();

    public FloorSet() {
    }

    public FloorSet(Element xml) {
        this.parts = new ArrayList<>();
        this.except = XMLUtils.readIntArrayAsList(xml.getChild("except", xml.getNamespace()));
        for (Element part : xml.getChildren("part", xml.getNamespace()))
            if (part.getAttribute("floor") != null)
                this.parts.add(new Pair<>(Integer.parseInt(part.getAttributeValue("floor")),
                        Integer.parseInt(part.getAttributeValue("floor"))));
            else
                this.parts.add(new Pair<>(Integer.parseInt(part.getAttributeValue("start")),
                        Integer.parseInt(part.getAttributeValue("end"))));
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

    public Element toXML() {
        Element root = new Element(XML_ROOT);
        for (Pair<Integer, Integer> part : this.parts)
            if (part.first.intValue() == part.second.intValue())
                root.addContent(new Element("part").setAttribute("floor", Integer.toString(part.first)));
            else
                root.addContent(new Element("part").setAttribute("start", Integer.toString(part.first))
                        .setAttribute("end", Integer.toString(part.second)));
        if (this.except.size() != 0)
            root.addContent(XMLUtils.toXML("except", this.except));
        return root;
    }

}
