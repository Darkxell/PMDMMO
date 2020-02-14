package com.darkxell.common.model.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.darkxell.common.util.Logger;

abstract class ModelIOHandler<T> {

    private final Class<T> clazz;

    public ModelIOHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void export(T object, File file) {
        object = this.handleBeforeExport(object);
        if (file == null) {
            Logger.e("File doesn't exist. Couldn't export " + this.clazz.getName());
            return;
        }

        try (OutputStream os = new FileOutputStream(file)) {
            Marshaller marsh = JAXBContext.newInstance(this.clazz).createMarshaller();
            marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marsh.marshal(object, os);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }

    protected T handleAfterImport(T object) {
        return object;
    }

    protected T handleBeforeExport(T object) {
        return object;
    }

    @SuppressWarnings("unchecked")
    public T read(URL resource) {
        T object = null;
        if (resource == null) {
            Logger.e("File doesn't exist. Couldn't read " + this.clazz.getName());
            return null;
        }

        try (InputStream is = resource.openConnection().getInputStream()) {
            Unmarshaller marsh = JAXBContext.newInstance(this.clazz).createUnmarshaller();
            object = (T) marsh.unmarshal(is);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        object = this.handleAfterImport(object);
        return object;
    }

}
