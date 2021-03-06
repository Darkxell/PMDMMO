package fr.darkxell.dataeditor.application.data;

import com.darkxell.client.resources.image.pokemon.body.PokemonSpritesetData;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpritesets;

import fr.darkxell.dataeditor.application.util.FileManager;

public class PokemonSpritesetManager {

    public static void remove(PokemonSpritesetData item) {
        PokemonSpritesets.remove(item);
        FileManager.delete(FileManager.filePaths.get(FileManager.POKEMON_SPRITES) + "/" + item.id + ".xml");
    }

}
