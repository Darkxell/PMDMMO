package com.darkxell.common.util.xml;

import org.jdom2.Element;

/**
 * XML object that acts immutable once initialized, so that the serialization does not listen to state changes after
 * initialization.
 */
public abstract class XMLImmutableObject extends XMLObject {
    private Element serialization;

    @Override
    protected void onInitialize(Element el) {
        this.serialization = el;
    }

    @Override
    public Element serialize() {
        return this.serialization;
    }
}
