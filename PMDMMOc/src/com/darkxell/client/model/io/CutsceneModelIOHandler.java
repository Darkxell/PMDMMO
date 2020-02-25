package com.darkxell.client.model.io;

import java.util.ArrayList;

import com.darkxell.client.mechanics.cutscene.event.RotateCutsceneEvent;
import com.darkxell.client.model.cutscene.CutsceneCreationModel;
import com.darkxell.client.model.cutscene.CutsceneEntityModel;
import com.darkxell.client.model.cutscene.CutsceneModel;
import com.darkxell.client.model.cutscene.CutscenePokemonModel;
import com.darkxell.client.model.cutscene.common.CutsceneDialogScreenModel;
import com.darkxell.client.model.cutscene.common.CutsceneEventType;
import com.darkxell.client.model.cutscene.common.MessageModel;
import com.darkxell.client.model.cutscene.end.CutsceneEndModel;
import com.darkxell.client.model.cutscene.end.LoadFreezoneCutsceneEndModel;
import com.darkxell.client.model.cutscene.event.*;
import com.darkxell.client.model.cutscene.event.AnimateCutsceneEventModel.AnimateCutsceneEventMode;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;
import com.darkxell.client.resources.image.pokemon.portrait.PortraitEmotion;
import com.darkxell.client.state.dialog.PokemonDialogScreen.DialogPortraitLocation;
import com.darkxell.common.model.io.ModelIOHandler;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;

public class CutsceneModelIOHandler extends ModelIOHandler<CutsceneModel> {

    private static Class<?>[] getClassesToBind() {
        Class<?>[] events = CutsceneEventModel.getXmlClassesToBind();
        Class<?>[] ends = CutsceneEndModel.getXmlClassesToBind();
        Class<?>[] entities = CutsceneEntityModel.getXmlClassesToBind();
        Class<?>[] all = new Class<?>[events.length + ends.length + entities.length];
        System.arraycopy(events, 0, all, 0, events.length);
        System.arraycopy(ends, 0, all, events.length, ends.length);
        System.arraycopy(entities, 0, all, events.length + ends.length, entities.length);
        return all;
    }

    public CutsceneModelIOHandler() {
        super(CutsceneModel.class, getClassesToBind());
    }

    @Override
    protected CutsceneModel handleAfterImport(CutsceneModel object) {

        this.handleCreationAfterImport(object.getCreation());
        object.getEvents().forEach(e -> this.handleEventAfterImport(e));
        this.handleEndAfterImport(object.getEnd());

        return super.handleAfterImport(object);
    }

    @Override
    protected CutsceneModel handleBeforeExport(CutsceneModel object) {

        object = object.copy();
        this.handleCreationBeforeExport(object.getCreation());
        object.getEvents().forEach(e -> this.handleEventBeforeExport(e));
        this.handleEndBeforeExport(object.getEnd());

        return super.handleBeforeExport(object);
    }

    private void handleCreationAfterImport(CutsceneCreationModel creation) {
        if (creation.isDrawmap() == null)
            creation.setDrawmap(true);
        if (creation.isFading() == null)
            creation.setFading(true);
        if (creation.getEntities() == null)
            creation.setEntities(new ArrayList<>());

        for (CutsceneEntityModel entity : creation.getEntities())
            this.handleEntityAfterImport(entity);
    }

    private void handleCreationBeforeExport(CutsceneCreationModel creation) {
        for (CutsceneEntityModel entity : creation.getEntities())
            this.handleEntityBeforeExport(entity);

        if (creation.isDrawmap().equals(true))
            creation.setDrawmap(null);
        if (creation.isFading().equals(true))
            creation.setFading(null);
        if (creation.getEntities().isEmpty())
            creation.setEntities(null);
    }

    private void handleEndAfterImport(CutsceneEndModel end) {
        if (end.isFadesOut() == null)
            end.setFadesOut(true);
        if (end instanceof LoadFreezoneCutsceneEndModel) {
            LoadFreezoneCutsceneEndModel load = (LoadFreezoneCutsceneEndModel) end;
            if (load.getXPos() == null)
                load.setXPos(-1);
            if (load.getYPos() == null)
                load.setYPos(-1);
            if (load.getDirection() == null)
                load.setDirection(Direction.SOUTH);
        }
    }

    private void handleEndBeforeExport(CutsceneEndModel end) {
        if (end.isFadesOut().equals(true))
            end.setFadesOut(null);
        if (end instanceof LoadFreezoneCutsceneEndModel) {
            LoadFreezoneCutsceneEndModel load = (LoadFreezoneCutsceneEndModel) end;
            if (load.getXPos().equals(-1))
                load.setXPos(null);
            if (load.getYPos().equals(-1))
                load.setYPos(null);
            if (load.getDirection() == Direction.SOUTH)
                load.setDirection(null);
        }
    }

    private void handleEntityAfterImport(CutsceneEntityModel entity) {
        if (entity.getCutsceneID() == null)
            entity.setCutsceneID(-1);
        if (entity.getXPos() == null)
            entity.setXPos(0.);
        if (entity.getYPos() == null)
            entity.setYPos(0.);

        if (entity instanceof CutscenePokemonModel)
            this.handlePokemonAfterImport((CutscenePokemonModel) entity);
    }

    private void handleEntityBeforeExport(CutsceneEntityModel entity) {
        if (entity.getCutsceneID().equals(-1))
            entity.setCutsceneID(null);
        if (entity.getXPos().equals(0.))
            entity.setXPos(null);
        if (entity.getYPos().equals(0.))
            entity.setYPos(null);

        if (entity instanceof CutscenePokemonModel)
            this.handlePokemonBeforeExport((CutscenePokemonModel) entity);
    }

    private void handleEventAfterImport(CutsceneEventModel event) {
        if (event.type == CutsceneEventType.animate) {
            AnimateCutsceneEventModel animate = (AnimateCutsceneEventModel) event;
            if (animate.getMode() == null)
                animate.setMode(AnimateCutsceneEventMode.PLAY);
        } else if (event.type == CutsceneEventType.camera) {
            CameraCutsceneEventModel camera = (CameraCutsceneEventModel) event;
            if (camera.getSpeed() == null)
                camera.setSpeed(1.);
        } else if (event.type == CutsceneEventType.dialog) {
            DialogCutsceneEventModel drawmap = (DialogCutsceneEventModel) event;
            if (drawmap.getIsNarratorDialog() == null)
                drawmap.setIsNarratorDialog(false);
            for (CutsceneDialogScreenModel screen : drawmap.getScreens()) {
                this.handleScreenAfterImport(screen);
            }
        } else if (event.type == CutsceneEventType.drawmap) {
            DrawMapCutsceneEventModel drawmap = (DrawMapCutsceneEventModel) event;
            if (drawmap.getDrawMap() == null)
                drawmap.setDrawMap(true);
        } else if (event.type == CutsceneEventType.move) {
            MoveCutsceneEventModel move = (MoveCutsceneEventModel) event;
            if (move.getSpeed() == null)
                move.setSpeed(1.);
        } else if (event.type == CutsceneEventType.option) {
            OptionDialogCutsceneEventModel drawmap = (OptionDialogCutsceneEventModel) event;
            this.handleScreenAfterImport(drawmap.getQuestion());
            for (MessageModel message : drawmap.getOptions()) {
                if (message.getTranslate() == null)
                    message.setTranslate(true);
            }
        } else if (event.type == CutsceneEventType.rotate) {
            RotateCutsceneEventModel move = (RotateCutsceneEventModel) event;
            if (move.getSpeed() == null)
                move.setSpeed(RotateCutsceneEvent.DEFAULT_SPEED);
        } else if (event.type == CutsceneEventType.sound) {
            SoundCutsceneEventModel move = (SoundCutsceneEventModel) event;
            if (move.getPlayOverMusic() == null)
                move.setPlayOverMusic(false);
        } else if (event.type == CutsceneEventType.wait) {
            WaitCutsceneEventModel move = (WaitCutsceneEventModel) event;
            if (move.getEvents() == null)
                move.setEvents(new ArrayList<>());
        }
    }

    private void handleEventBeforeExport(CutsceneEventModel event) {
        if (event.type == CutsceneEventType.animate) {
            AnimateCutsceneEventModel animate = (AnimateCutsceneEventModel) event;
            if (animate.getMode() == AnimateCutsceneEventMode.PLAY)
                animate.setMode(null);
        } else if (event.type == CutsceneEventType.camera) {
            CameraCutsceneEventModel camera = (CameraCutsceneEventModel) event;
            if (camera.getSpeed().equals(1.))
                camera.setSpeed(null);
        } else if (event.type == CutsceneEventType.dialog) {
            DialogCutsceneEventModel drawmap = (DialogCutsceneEventModel) event;
            if (drawmap.getIsNarratorDialog().equals(false))
                drawmap.setIsNarratorDialog(null);
            for (CutsceneDialogScreenModel screen : drawmap.getScreens()) {
                this.handleScreenBeforeExport(screen);
            }
        } else if (event.type == CutsceneEventType.drawmap) {
            DrawMapCutsceneEventModel drawmap = (DrawMapCutsceneEventModel) event;
            if (drawmap.getDrawMap().equals(true))
                drawmap.setDrawMap(null);
        } else if (event.type == CutsceneEventType.move) {
            MoveCutsceneEventModel move = (MoveCutsceneEventModel) event;
            if (move.getSpeed().equals(1.))
                move.setSpeed(null);
        } else if (event.type == CutsceneEventType.option) {
            OptionDialogCutsceneEventModel drawmap = (OptionDialogCutsceneEventModel) event;
            this.handleScreenBeforeExport(drawmap.getQuestion());
            for (MessageModel message : drawmap.getOptions()) {
                if (message.getTranslate().equals(true))
                    message.setTranslate(null);
            }
        } else if (event.type == CutsceneEventType.rotate) {
            RotateCutsceneEventModel move = (RotateCutsceneEventModel) event;
            if (move.getSpeed().equals(RotateCutsceneEvent.DEFAULT_SPEED))
                move.setSpeed(null);
        } else if (event.type == CutsceneEventType.sound) {
            SoundCutsceneEventModel move = (SoundCutsceneEventModel) event;
            if (move.getPlayOverMusic().equals(false))
                move.setPlayOverMusic(null);
        } else if (event.type == CutsceneEventType.wait) {
            WaitCutsceneEventModel move = (WaitCutsceneEventModel) event;
            if (move.getEvents().isEmpty())
                move.setEvents(null);
        }
    }

    private void handlePokemonAfterImport(CutscenePokemonModel pokemon) {
        if (pokemon.getAnimated() == null)
            pokemon.setAnimated(true);
        if (pokemon.getFacing() == null)
            pokemon.setFacing(Direction.SOUTH);
        if (pokemon.getState() == null)
            pokemon.setState(PokemonSpriteState.IDLE);

        if (pokemon.getPokemonID() == null && pokemon.getTeamMember() == null)
            Logger.e("Cutscene pokemon " + pokemon.getCutsceneID() + " has no ID or team member reference!");
    }

    private void handlePokemonBeforeExport(CutscenePokemonModel pokemon) {
        if (pokemon.getAnimated().equals(true))
            pokemon.setAnimated(null);
        if (pokemon.getFacing() == Direction.SOUTH)
            pokemon.setFacing(null);
        if (pokemon.getState() == PokemonSpriteState.IDLE)
            pokemon.setState(null);
    }

    private void handleScreenAfterImport(CutsceneDialogScreenModel screen) {
        if (screen.getTranslate() == null)
            screen.setTranslate(true);
        if (screen.getEmotion() == null)
            screen.setEmotion(PortraitEmotion.Normal);
        if (screen.getPortraitLocation() == null)
            screen.setPortraitLocation(DialogPortraitLocation.BOTTOM_LEFT);
    }

    private void handleScreenBeforeExport(CutsceneDialogScreenModel screen) {
        if (screen.getTranslate().equals(true))
            screen.setTranslate(null);
        if (screen.getEmotion() == PortraitEmotion.Normal)
            screen.setEmotion(null);
        if (screen.getPortraitLocation() == DialogPortraitLocation.BOTTOM_LEFT)
            screen.setPortraitLocation(null);
    }

}
