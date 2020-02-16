package com.darkxell.common.model.io;

import com.darkxell.common.model.floor.StaticFloorModel;

public class ModelIOHandlers {

    public static final DungeonModelIOHandler dungeon = new DungeonModelIOHandler();
    public static final ItemModelIOHandler item = new ItemModelIOHandler();
    public static final MoveModelIOHandler move = new MoveModelIOHandler();
    public static final PokemonModelIOHandler pokemon = new PokemonModelIOHandler();

    public static final ModelIOHandler<StaticFloorModel> staticFloor = new ModelIOHandler<>(StaticFloorModel.class);

}
