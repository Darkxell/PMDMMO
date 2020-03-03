package com.darkxell.client.mechanics.cutscene.entity;

import com.darkxell.client.model.cutscene.CutsceneEntityModel;
import com.darkxell.client.model.cutscene.CutscenePokemonModel;

public class CutsceneEntityFactory {

    public static CutsceneEntity build(CutsceneEntityModel model) {
        if (model instanceof CutscenePokemonModel)
            return new CutscenePokemon((CutscenePokemonModel) model);
        return new CutsceneEntity(model);
    }

}
