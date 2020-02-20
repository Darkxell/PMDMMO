package com.darkxell.client.model.io;

public final class ClientModelIOHandlers {
    
    public static final AnimationModelIOHandler animation = new AnimationModelIOHandler();
    public static final PokemonAnimationModelIOHandler pokemonAnimation = new PokemonAnimationModelIOHandler(); 
    public static final QuizModelIOHandler quiz = new QuizModelIOHandler();
    
    private ClientModelIOHandlers() {}

}
