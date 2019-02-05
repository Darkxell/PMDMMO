package fr.darkxell.dataeditor.application.data;

import com.darkxell.common.Registries;
import com.darkxell.common.move.Move;

import fr.darkxell.dataeditor.application.util.CustomTreeItem;

public class MoveListItem extends CustomTreeItem {

    public final Move move;

    public MoveListItem(Move move) {
        this.move = move;
    }

    @Override
    public String toString() {
        if (this.move.id == 0)
            return "0: Basic Attack";
        if (this.move.id >= 2500)
            return this.move.id + ": "
                    + Registries.items().find(this.move.id - 2419).name().toString().replaceAll("<.*?>", "");
        return this.move.id + ": " + this.move.name().toString().replaceAll("<.*?>", "");
    }

}
