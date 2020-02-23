package com.darkxell.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.darkxell.common.zones.FreezoneInfo;

/** Utility methods for XML. */
public final class XMLUtils {
    public static class StringArrayAdapter extends XmlAdapter<String, String[]> {
        @Override
        public String marshal(String[] v) throws Exception {
            if (v == null)
                return null;
            String s = "";
            for (int i = 0; i < v.length; ++i) {
                if (i != 0)
                    s += ",";
                s += v[i];
            }
            return s;
        }

        @Override
        public String[] unmarshal(String v) throws Exception {
            if (v == null)
                return null;
            return v.split(",");
        }
    }

    public static class IntegerArrayAdapter extends XmlAdapter<String, Integer[]> {
        @Override
        public String marshal(Integer[] v) throws Exception {
            if (v == null)
                return null;
            String s = "";
            for (int i = 0; i < v.length; ++i) {
                if (i != 0)
                    s += ",";
                s += v[i];
            }
            return s;
        }

        @Override
        public Integer[] unmarshal(String v) throws Exception {
            if (v == null)
                return null;
            String[] vs = v.split(",");
            Integer[] tr = new Integer[vs.length];
            for (int i = 0; i < tr.length; ++i) {
                tr[i] = Integer.valueOf(vs[i]);
            }
            return tr;
        }
    }

    public static class IntegerListAdapter extends XmlAdapter<String, ArrayList<Integer>> {
        @Override
        public String marshal(ArrayList<Integer> v) throws Exception {
            if (v == null)
                return null;
            String s = "";
            for (int i = 0; i < v.size(); ++i) {
                if (i != 0)
                    s += ",";
                s += v.get(i);
            }
            return s;
        }

        @Override
        public ArrayList<Integer> unmarshal(String v) throws Exception {
            if (v == null)
                return null;
            String[] vs = v.split(",");
            ArrayList<Integer> tr = new ArrayList<>(vs.length);
            for (int i = 0; i < vs.length; ++i) {
                tr.add(Integer.valueOf(vs[i]));
            }
            return tr;
        }
    }

    public static class FreezoneInfoAdapter extends XmlAdapter<String, FreezoneInfo> {
        @Override
        public FreezoneInfo unmarshal(String v) throws Exception {
            if (v == null)
                return null;
            return FreezoneInfo.find(v);
        }

        @Override
        public String marshal(FreezoneInfo v) throws Exception {
            if (v == null)
                return null;
            return v.id;
        }
    }

    /** @return The Attribute with the input ID in the input Element. */
    public static Attribute getAttribute(Element element, String id) {
        return element.getAttribute(id) != null ? element.getAttribute(id)
                : element.getAttribute(id, element.getNamespace()) != null
                        ? element.getAttribute(id, element.getNamespace())
                        : null;
    }

    /**
     * @return The value of the Attribute with the input ID in the input Element. If that Attribute doesn't exist,
     *         returns the input default Value.
     */
    public static Boolean getAttribute(Element element, String id, Boolean defaultValue) {
        Attribute attribute = getAttribute(element, id);
        return attribute == null ? defaultValue : Boolean.parseBoolean(attribute.getValue());
    }

    /**
     * @return The value of the Attribute with the input ID in the input Element. If that Attribute doesn't exist,
     *         returns the input default Value.
     */
    public static Double getAttribute(Element element, String id, Double defaultValue) {
        Attribute attribute = getAttribute(element, id);
        return attribute == null ? defaultValue : Double.parseDouble(attribute.getValue());
    }

    /**
     * @return The value of the Attribute with the input ID in the input Element. If that Attribute doesn't exist,
     *         returns the input default Value.
     */
    public static Integer getAttribute(Element element, String id, Integer defaultValue) {
        Attribute attribute = getAttribute(element, id);
        return attribute == null ? defaultValue : Double.valueOf(attribute.getValue()).intValue();
    }

    /**
     * @return The value of the Attribute with the input ID in the input Element. If that Attribute doesn't exist,
     *         returns the input default Value.
     */
    public static Short getAttribute(Element element, String id, Short defaultValue) {
        Attribute attribute = getAttribute(element, id);
        return attribute == null ? defaultValue : Short.parseShort(attribute.getValue());
    }

    /**
     * @return The value of the Attribute with the input ID in the input Element. If that Attribute doesn't exist,
     *         returns the input default Value.
     */
    public static String getAttribute(Element element, String id, String defaultValue) {
        Attribute attribute = getAttribute(element, id);
        return attribute == null ? defaultValue : attribute.getValue();
    }

    /** Reads an XML File and returns its root Element. */
    public static Element read(InputStream input) {
        if (input != null)
            try {
                return new SAXBuilder().build(input).getRootElement();
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    /**
     * Reads an array of Integers in an XML element and returns it. <br />
     * e.g. 1,2,4,5,-1
     */
    public static int[] readIntArray(Element element) {
        ArrayList<Integer> list = readIntArrayAsList(element);
        int[] array = new int[list.size()];
        for (int i = 0; i < array.length; ++i)
            array[i] = list.get(i);
        return array;
    }

    /**
     * Reads an array of Integers in an XML element and returns it. <br />
     * e.g. 1,2,4,5,-1
     */
    public static Integer[] readIntegerArray(Element element) {
        return readIntArrayAsList(element).toArray(new Integer[0]);
    }

    /**
     * Reads an array of Integers in an XML element and returns it. <br />
     * e.g. 1,2,4,5,-1
     */
    public static ArrayList<Integer> readIntArrayAsList(Element element) {
        ArrayList<Integer> array = new ArrayList<Integer>();
        if (element != null) {
            String value = element.getText();
            if (value != null && !value.equals(""))
                for (String floor : value.split(","))
                    array.add(Integer.parseInt(floor));
        }
        return array;
    }

    /**
     * Reads a double array of Integers in an XML element and returns it. <br />
     * e.g. 1,2,4,5,-1;5,1,3
     */
    public static int[][] readIntMatrix(Element element) {
        String[] rows = element.getValue().split(";");
        int[][] array = new int[rows.length][];
        for (int i = 0; i < array.length; ++i) {
            String[] cells = rows[i].split(",");
            array[i] = new int[cells.length];
            for (int j = 0; j < cells.length; ++j)
                array[i][j] = Integer.parseInt(cells[j]);
        }
        return array;
    }

    public static short[] readShortArray(String value) {
        String[] values = value.split(",");
        short[] array = new short[values.length];
        for (int i = 0; i < array.length; ++i)
            array[i] = Short.parseShort(values[i]);
        return array;
    }

    /** Saves the input XML Element into the input file. */
    public static void saveFile(File file, Element element) {
        try {
            if (!file.exists())
                file.createNewFile();
            saveFile(new FileOutputStream(file), element);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(OutputStream ostream, Element element) {
        try {
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(new Document(element), ostream);
            ostream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setAttribute(Element element, String id, boolean value, boolean defaultValue) {
        if (value != defaultValue)
            element.setAttribute(id, Boolean.toString(value));
    }

    public static void setAttribute(Element element, String id, byte value, byte defaultValue) {
        if (value != defaultValue)
            element.setAttribute(id, Byte.toString(value));
    }

    public static void setAttribute(Element element, String id, char value, char defaultValue) {
        if (value != defaultValue)
            element.setAttribute(id, Character.toString(value));
    }

    public static void setAttribute(Element element, String id, double value, double defaultValue) {
        if (value != defaultValue)
            element.setAttribute(id, Double.toString(value));
    }

    public static void setAttribute(Element element, String id, float value, float defaultValue) {
        if (value != defaultValue)
            element.setAttribute(id, Float.toString(value));
    }

    public static void setAttribute(Element element, String id, int value, int defaultValue) {
        if (value != defaultValue)
            element.setAttribute(id, Integer.toString(value));
    }

    public static void setAttribute(Element element, String id, long value, long defaultValue) {
        if (value != defaultValue)
            element.setAttribute(id, Long.toString(value));
    }

    public static void setAttribute(Element element, String id, short value, short defaultValue) {
        if (value != defaultValue)
            element.setAttribute(id, Short.toString(value));
    }

    public static void setAttribute(Element element, String id, String value, String defaultValue) {
        if (value != null && !value.equals(defaultValue))
            element.setAttribute(id, value);
    }

    public static String toXML(short[] array) {
        String value = "";
        for (short floor : array)
            if (value.equals(""))
                value += floor;
            else
                value += "," + floor;
        return value;
    }

    /**
     * Exports an array of Integers to an XML element and returns it. <br />
     * e.g. 1,2,4,5,-1
     * 
     * @param id - The Element name.
     */
    public static Element toXML(String id, int[] array) {
        String value = "";
        for (int floor : array)
            if (value.equals(""))
                value += floor;
            else
                value += "," + floor;
        return new Element(id).setText(value);
    }

    /**
     * Exports an array of Integers to an XML element and returns it. <br />
     * e.g. 1,2,4,5,-1
     * 
     * @param id - The Element name.
     */
    public static Element toXML(String id, Integer[] array) {
        String value = "";
        for (int floor : array)
            if (value.equals(""))
                value += floor;
            else
                value += "," + floor;
        return new Element(id).setText(value);
    }

    /**
     * Exports a matrix of Integers to an XML element and returns it. <br />
     * e.g. 1,2,4,5,-1;5,1,3
     * 
     * @param id - The Element name.
     */
    public static Element toXML(String id, int[][] array) {
        String value = "";
        for (int[] row : array) {
            if (!value.equals(""))
                value += ";";
            for (int cell : row)
                if (value.equals("") || value.endsWith(";"))
                    value += cell;
                else
                    value += "," + cell;
        }
        return new Element(id).setText(value);
    }

    /**
     * Exports an array of Integers to an XML element and returns it. <br />
     * e.g. 1,2,4,5,-1
     * 
     * @param id - The Element name.
     */
    public static Element toXML(String id, Iterable<Integer> array) {
        String value = "";
        for (Integer floor : array)
            if (value.equals(""))
                value += floor;
            else
                value += "," + floor;
        return new Element(id).setText(value);
    }

}
