package com.darkxell.common.util.xml;

import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.darkxell.common.util.Logger;

/**
 * Object whose state is primarily represented by an XML object.
 *
 * <p>
 * The initialization is done in a separate method so that subclasses do not have to include the {@link Element} class
 * in their imports if they do not alter the serialization behavior of the superclass. Because of the highly centralized
 * nature of XML object creation where there are more classes created than classes where initialization takes place, it
 * is probably worth the extra hassle. At least, that's what I tell myself so I can sleep with a clean conscience.
 * </p>
 *
 * <p>
 * If there are some fields that require default values, it is probably a good idea to stick them in a
 * pre-initialization block so that you don't have to call {@code super()} every single time, but if you want to annoy
 * everyone it is also possible to put them in the constructor.
 * </p>
 *
 * <p>
 * After initialization, this class should be treated as immutable for the most convenience in implementation. This
 * should be used mostly as a wrapper around the Element for the client's behavior and not as a primary storage method.
 * </p>
 */
public abstract class XMLObject {
    /**
     * What the root tag name should be.
     */
    public static final String TAG_NAME = "root";

    /**
     * Has the initialization method already been called?
     */
    protected boolean initialized;

    /**
     * Initialize object from an XML file stream.
     */
    public final void initialize(InputStream xmlFile) throws IOException, JDOMException {
        SAXBuilder builder = new SAXBuilder();
        Element root = builder.build(xmlFile).getRootElement();
        this.initialize(root);
    }

    /**
     * Initialize from XML, but only ever once.
     */
    public final void initialize(Element el) {
        if (initialized) {
            Logger.w("Attempted to initialize object more than once.");
            return;
        }

        this.onInitialize(el);
        this.deserialize(el);
        this.initialized = true;
    }

    /**
     * Custom initialization behavior that is not a part of deserialization.
     *
     * <p>
     * The default implementation has no behavior.
     * </p>
     */
    protected void onInitialize(Element el) {
    }

    /**
     * Custom serialization behavior.
     *
     * @return XML representation of object.
     */
    public abstract Element serialize();

    /**
     * Custom deserialization behavior.
     */
    protected abstract void deserialize(Element el);
}
