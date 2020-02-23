package fr.darkxell.dataeditor.application.data;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.darkxell.client.model.cutscene.CutsceneModel;
import com.darkxell.client.model.io.ClientModelIOHandlers;

import fr.darkxell.dataeditor.application.util.FileManager;

public class Cutscenes {

    private static HashMap<String, CutsceneModel> cutscenes = new HashMap<>();

    public static void add(CutsceneModel cutscene) {
        if (!cutscenes.containsKey(cutscene.getName()))
            put(cutscene.getName(), cutscene);
    }

    public static boolean containsKey(String name) {
        return cutscenes.containsKey(name);
    }

    public static CutsceneModel get(String cutsceneID) {
        return cutscenes.get(cutsceneID);
    }

    public static void load() {
        String base = FileManager.filePaths.get(FileManager.CUTSCENES);
        ArrayList<String> cutsceneFiles = FileManager.findAllSubFiles(base);
        for (String file : cutsceneFiles)
            try {
                @SuppressWarnings("deprecation")
                CutsceneModel cutscene = ClientModelIOHandlers.cutscene.read(new File(base + "/" + file).toURL());
                cutscenes.put(cutscene.getName(), cutscene);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
    }

    private static void put(String name, CutsceneModel cutscene) {
        cutscenes.put(name, cutscene);
        File f = FileManager.create(FileManager.filePaths.get(FileManager.CUTSCENES) + "/" + name + ".xml");
        ClientModelIOHandlers.cutscene.export(cutscene, f);
    }

    public static void remove(String name) {
        cutscenes.remove(name);
        FileManager.delete(FileManager.filePaths.get(FileManager.CUTSCENES) + "/" + name + ".xml");
    }

    public static void update(CutsceneModel cutscene) {
        put(cutscene.getName(), cutscene);
    }

    public static Collection<CutsceneModel> values() {
        return cutscenes.values();
    }

}
