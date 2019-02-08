package com.darkxell.client.state.quiz;

import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.common.util.language.Message;

public enum Nature {

    Brave(0, 5),
    Calm(1, 5),
    Docile(2, 6),
    Hardy(3, 7),
    Hasty(4, 6),
    Impish(5, 7),
    Jolly(6, 6),
    Lonely(7, 6),
    Naive(8, 6),
    Quirky(9, 7),
    Relaxed(10, 5),
    Sassy(11, 6),
    Timid(12, 6);

    public static Nature get(int id) {
        for (Nature n : values())
            if (n.id == id)
                return n;
        return Nature.Brave;
    }

    public final int id;
    /** The number of lines this Nature's description has. */
    public final int textLines;

    Nature(int id, int textLines) {
        this.id = id;
        this.textLines = textLines;
    }

    public DialogScreen[] description() {
        DialogScreen[] desc = new DialogScreen[this.textLines];
        for (int i = 0; i < this.textLines; ++i)
            desc[i] = new DialogScreen(new Message("quiz." + this.name() + "." + i));
        return desc;
    }

}
