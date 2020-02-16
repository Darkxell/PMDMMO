package com.darkxell.common.model.move;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "moves")
@XmlAccessorType(XmlAccessType.FIELD)
public class MoveListModel {

    @XmlElement(name = "move")
    public final ArrayList<MoveModel> moves = new ArrayList<>();

    public MoveListModel copy() {
        MoveListModel clone = new MoveListModel();
        for (MoveModel move : this.moves)
            clone.moves.add(move.copy());
        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MoveListModel))
            return false;
        MoveListModel o = (MoveListModel) obj;
        if (this.moves.size() != o.moves.size())
            return false;
        for (int i = 0; i < moves.size(); ++i) {
            if (!this.moves.get(i).equals(o.moves.get(i)))
                return false;
        }
        return true;
    }

}
