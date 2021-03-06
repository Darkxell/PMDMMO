package com.darkxell.client.model.io;

public final class ClientModelIOHandlers {
    
    public static final AnimationModelIOHandler animation = new AnimationModelIOHandler();
    public static final CutsceneModelIOHandler cutscene = new CutsceneModelIOHandler();
    public static final FreezoneModelIOHandler freezone = new FreezoneModelIOHandler();
    public static final FriendLocationModelIOHandler friendLocations = new FriendLocationModelIOHandler();
    public static final PokemonAnimationModelIOHandler pokemonAnimation = new PokemonAnimationModelIOHandler(); 
    public static final QuizModelIOHandler quiz = new QuizModelIOHandler();
    
    private ClientModelIOHandlers() {}

}
