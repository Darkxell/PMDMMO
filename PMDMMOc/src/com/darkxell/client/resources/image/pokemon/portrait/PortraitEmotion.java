package com.darkxell.client.resources.image.pokemon.portrait;

public enum PortraitEmotion {
    Amazed(4),
    Angry(12),
    Awkward(0),
    Confused(2),
    Crying(9),
    Determined(6),
    Happy(14),
    Joy(7),
    Laughing(5),
    Normal(-1),
    Relieved(1),
    Sad(10),
    Shocked(3),
    Sneezing(8),
    Struggle(13),
    Thoughtful(11);

    public final int index;

    PortraitEmotion(int index) {
        this.index = index;
    }
}
