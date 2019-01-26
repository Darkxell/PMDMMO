package com.darkxell.common.util;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Object that is primarily initialized by an XML object.
 *
 * <p>The initialization is done in a separate method so that subclasses do not have to include the {@link Element}
 * class in their imports if they do not alter the initialization behavior of the superclass. Because of the highly
 * centralized nature of XML object creation where there are more classes created than classes where initialization
 * takes place, it is probably worth the extra hassle. At least, that's what I tell myself so I can sleep with a
 * clean conscience.</p>
 */
public abstract class XMLObject {
    /**
     * Has the initialization method already been called?
     */
    private boolean initialized;

    /**
     * No-op constructor. Initialize instead with {@link #initialize(Element)}.
     *
     * <p>If there are some fields that require default values, it is probably a good idea to stick them in a
     * pre-initialization block so that you don't have to call {@code super()} every single time, but if you want to
     * annoy everyone it is also possible to put them in the constructor.</p>
     */
    public XMLObject() {
    }

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
            Logger.w("Attempted to initialize object twice.");
            return;
        }

        this.onInitialize(el);
        this.initialized = true;
    }

    /**
     * Custom initialization behavior.
     */
    protected abstract void onInitialize(Element el);
}
