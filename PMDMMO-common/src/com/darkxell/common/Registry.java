package com.darkxell.common;

import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;
import org.jdom2.Element;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Base class for all registries.
 */
public abstract class Registry<T extends Registrable<T>> {
    /**
     * Registry name. Should be unique, but is just a label.
     */
    private final String name;

    /**
     * Path of the file that this was originally loaded from.
     */
    private final URL originalURL;

    protected HashMap<Integer, T> cache;

    public Registry(URL registryURL, String name) throws IOException {
        Logger.d("Loading " + name + "...");

        this.name = name;
        this.originalURL = registryURL;

        Element root = XMLUtils.read(registryURL.openStream());
        this.cache = this.deserializeDom(root);
    }

    protected abstract Element serializeDom(HashMap<Integer, T> registryCache);
    protected abstract HashMap<Integer, T> deserializeDom(Element root);

    public void register(T entry) {
        this.cache.put(entry.getID(), entry);
    }

    public void unregister(int id) {
        this.cache.remove(id);
    }

    public T find(int id) {
        if (!cache.containsKey(id)) {
            return cache.get(id);
        }
        Logger.e("Invalid ID " + id + " for registry " + this.name);
        return null;
    }

    /**
     * @return All registry entries in cache.
     */
    public ArrayList<T> toList() {
        ArrayList<T> list = new ArrayList<>(cache.values());
        list.sort(Comparator.naturalOrder());
        return list;
    }

    public void save(File file) {
        XMLUtils.saveFile(file, this.serializeDom(this.cache));
    }

    /**
     * Attempt to save to where the file was first retrieved from.
     *
     * @throws IOException If the original URL was read-only (e.g. is a web connection).
     */
    public void save() throws IOException {
        OutputStream urlStream = this.originalURL.openConnection().getOutputStream();
        XMLUtils.saveFile(urlStream, this.serializeDom(this.cache));
    }
}