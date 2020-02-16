package com.darkxell.common.model.io;

import java.util.Comparator;

import com.darkxell.common.model.move.MoveListModel;
import com.darkxell.common.model.move.MoveModel;
import com.darkxell.common.move.MoveRange;
import com.darkxell.common.move.MoveTarget;

public class MoveModelIOHandler extends ModelIOHandler<MoveListModel> {

    public MoveModelIOHandler() {
        super(MoveListModel.class);
    }

    @Override
    protected MoveListModel handleAfterImport(MoveListModel object) {
        
        object.moves.sort(Comparator.naturalOrder());

        for (MoveModel move : object.moves) {
            if (move.isDealsDamage() == null)
                move.setDealsDamage(false);

            if (move.isGinsengable() == null)
                move.setGinsengable(false);

            if (move.isPiercesFreeze() == null)
                move.setPiercesFreeze(false);

            if (move.isReflectable() == null)
                move.setReflectable(false);

            if (move.isSnatchable() == null)
                move.setSnatchable(false);

            if (move.isSound() == null)
                move.setSound(false);

            if (move.getAccuracy() == null)
                move.setAccuracy(100);

            if (move.getCritical() == null)
                move.setCritical(12);

            if (move.getRange() == null)
                move.setRange(MoveRange.Front);

            if (move.getTargets() == null)
                move.setTargets(MoveTarget.Foes);
        }

        return super.handleAfterImport(object);
    }

    @Override
    protected MoveListModel handleBeforeExport(MoveListModel object) {

        object = object.copy();

        for (MoveModel move : object.moves) {
            if (move.isDealsDamage().equals(false))
                move.setDealsDamage(null);

            if (move.isGinsengable().equals(false))
                move.setGinsengable(null);

            if (move.isPiercesFreeze().equals(false))
                move.setPiercesFreeze(null);

            if (move.isReflectable().equals(false))
                move.setReflectable(null);

            if (move.isSnatchable().equals(false))
                move.setSnatchable(null);

            if (move.isSound().equals(false))
                move.setSound(null);

            if (move.getAccuracy().equals(100))
                move.setAccuracy(null);

            if (move.getCritical().equals(12))
                move.setCritical(null);

            if (move.getRange() == MoveRange.Front)
                move.setRange(null);

            if (move.getTargets() == MoveTarget.Foes)
                move.setTargets(null);
        }

        return super.handleAfterImport(object);
    }

}
