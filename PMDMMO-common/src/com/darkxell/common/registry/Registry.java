package com.darkxell.common.registry;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import com.darkxell.common.model.io.ModelIOHandler;
import com.darkxell.common.util.Logger;

/**
 * Base class for all registries.
 */
public abstract class Registry<T extends Registrable<T>, M> {
    /**
     * Registry name. Should be unique, but is just a label.
     */
    private final String name;

    /**
     * Path of the file that this was originally loaded from.
     */
    private final URL originalURL;

    /**
     * What item should be retrieved if there are no matches.
     */
    private final T defaultIndex;

    private final ModelIOHandler<M> ioHandler;

    protected HashMap<Integer, T> cache;

    public Registry(URL registryURL, ModelIOHandler<M> ioHandler, String name) throws IOException {
        this(registryURL, ioHandler, name, null);
    }

    public Registry(URL registryURL, ModelIOHandler<M> ioHandler, String name, Integer defaultIndex)
            throws IOException {
        Logger.d("Loading " + name + "...");

        this.name = name;
        this.originalURL = registryURL;
        this.ioHandler = ioHandler;

        this.cache = this.deserializeDom(this.ioHandler.read(this.originalURL));

        this.defaultIndex = defaultIndex == null ? null : this.cache.get(defaultIndex);
    }

    /**
     * Dummy constructor for {@link com.darkxell.common.trap.TrapRegistry TrapRegistry}.
     *
     * TODO: remove once TrapRegistry is implemented.
     */
    public Registry(String name) {
        Logger.d("Loading " + name + "...");

        this.name = name;
        this.originalURL = null;
        this.ioHandler = null;
        this.cache = this.deserializeDom(null);
        this.defaultIndex = null;
    }

    protected abstract M serializeDom(HashMap<Integer, T> registryCache);

    protected abstract HashMap<Integer, T> deserializeDom(M model);

    public void register(T entry) {
        this.cache.put(entry.getID(), entry);
    }

    public void unregister(int id) {
        this.cache.remove(id);
    }

    public T find(int id) {
        if (cache.containsKey(id))
            return cache.get(id);
        Logger.e("Invalid ID " + id + " for registry " + this.name);
        return this.defaultIndex;
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
        this.ioHandler.export(this.serializeDom(this.cache), file);
    }

    /**
     * Attempt to save to where the file was first retrieved from.
     *
     * @throws IOException If the original URL was read-only (e.g. is a web connection).
     */
    public void save() throws IOException {
        // TODO: remove null check when trap registry is complete because this.originalURL cannot be null otherwise
        if (this.ioHandler == null)
            return;
        this.save(new File(this.originalURL.getFile()));
    }
}
