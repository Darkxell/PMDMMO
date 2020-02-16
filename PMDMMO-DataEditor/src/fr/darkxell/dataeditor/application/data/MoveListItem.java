package fr.darkxell.dataeditor.application.data;

import com.darkxell.common.move.Move;
import com.darkxell.common.registry.Registries;

import fr.darkxell.dataeditor.application.util.CustomTreeItem;

public class MoveListItem extends CustomTreeItem {

    public final Move move;

    public MoveListItem(Move move) {
        this.move = move;
    }

    @Override
    public String toString() {
        if (this.move.getID() == 0)
            return "0: Basic Attack";
        if (this.move.getID() >= 2500)
            return this.move.getID() + ": "
                    + Registries.items().find(this.move.getID() - 2419).name().toString().replaceAll("<.*?>", "");
        return this.move.getID() + ": " + this.move.name().toString().replaceAll("<.*?>", "");
    }

}
