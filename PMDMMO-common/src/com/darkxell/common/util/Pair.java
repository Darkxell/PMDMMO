package com.darkxell.common.util;

import java.util.Comparator;

public class Pair<F, S> {

    public static final Comparator<Pair<Integer, Integer>> integerComparator = (o1, o2) -> {
        int first = Integer.compare(o1.first, o2.first);
        if (first == 0) return Integer.compare(o1.second, o2.second);
        return first;
    };

    public final F first;
    public final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public Pair(Pair<F, S> pair) {
        this.first = pair.first;
        this.second = pair.second;
    }

    @Override
    protected Pair<F, S> clone() {
        return new Pair<>(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.first != null ? this.first.hashCode() : 0);
        hash = 31 * hash + (this.second != null ? this.second.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) return false;
        try {
            @SuppressWarnings("unchecked")
            Pair<F, S> p = (Pair<F, S>) obj;
            return (this.first == p.first || this.first.equals(p.first)) && (this.second == p.second || this.second.equals(p.second));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "<" + this.first.toString() + "," + this.second.toString() + ">";
    }

}
