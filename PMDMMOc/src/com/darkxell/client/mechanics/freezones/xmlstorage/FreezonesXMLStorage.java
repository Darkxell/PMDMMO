package com.darkxell.client.mechanics.freezones.xmlstorage;

import java.io.IOException;
import java.util.HashMap;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.darkxell.client.resources.Res;
import com.darkxell.common.util.Logger;

/** Utility class that stores the XML elements of freezones instead of looking in files each time. */
public abstract class FreezonesXMLStorage {

    /** Stores JDOM elements representing the different freezones files. */
    private static HashMap<String, Element> zonesXML = new HashMap<>(10);

    /**
     * Gets the freezone XML jdom element contained in the game files. This methods stores elements in a cache and is
     * faster than accessing disc space after the first call.
     */
    public static synchronized Element getFreezoneXML(String xmlPath) throws IOException, JDOMException {
        if (zonesXML.containsKey(xmlPath)) {
            return zonesXML.get(xmlPath);
        }
        SAXBuilder builder = new SAXBuilder();
        Element root = builder.build(Res.get(xmlPath)).getRootElement();
        Logger.i("Read freezone XML for the first time:" + xmlPath);
        zonesXML.put(xmlPath, root);
        return root;
    }

}
