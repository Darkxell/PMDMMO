package com.darkxell.common.util;

import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

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

}
