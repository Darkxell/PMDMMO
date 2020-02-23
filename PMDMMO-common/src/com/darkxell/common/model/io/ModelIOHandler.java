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

public class ModelIOHandler<T> {

    private final Class<T> clazz;
    private final Class<?>[] typesToBind;

    public ModelIOHandler(Class<T> clazz, Class<?>... additionalTypesToBind) {
        this.clazz = clazz;
        this.typesToBind = new Class<?>[additionalTypesToBind.length + 1];
        this.typesToBind[0] = clazz;
        System.arraycopy(additionalTypesToBind, 0, this.typesToBind, 1, additionalTypesToBind.length);
    }

    public void export(T object, File file) {
        object = this.handleBeforeExport(object);
        if (file == null) {
            Logger.e("File doesn't exist. Couldn't export " + this.clazz.getName());
            return;
        }

        if (file.exists())
            file.delete();
        try (OutputStream os = new FileOutputStream(file)) {
            Marshaller marsh = JAXBContext.newInstance(this.typesToBind).createMarshaller();
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
            Unmarshaller marsh = JAXBContext.newInstance(this.typesToBind).createUnmarshaller();
            object = (T) marsh.unmarshal(is);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        object = this.handleAfterImport(object);
        return object;
    }

}
