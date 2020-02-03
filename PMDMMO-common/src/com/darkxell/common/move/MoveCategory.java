package com.darkxell.common.move;

import com.darkxell.common.util.language.Message;

public enum MoveCategory {
    Physical,
    Special,
    Status;

    public Message getName() {
        return new Message("move.info.category." + this.name());
    }
}