package com.darkxell.client.state.dialog;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.resources.images.Sprite;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait.PortraitEmotion;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;
import com.sun.javafx.geom.Point2D;

public class PokemonDialogScreen extends DialogScreen {
    public enum DialogPortraitLocation {
        BOTTOM_LEFT(true),
        BOTTOM_RIGHT(false),
        TOP_LEFT(true),
        TOP_RIGHT(false);

        private static DialogPortraitLocation[] values = DialogPortraitLocation.values();

        public static DialogPortraitLocation getByIndex(int i) {
            if (i < 0 || i > values.length)
                return null;

            return values[i];
        }

        public final boolean flip;

        DialogPortraitLocation(boolean flip) {
            this.flip = flip;
        }

        public Point2D locate(Rectangle dialogBox, Sprite portrait) {
            switch (this) {
            case TOP_LEFT:
                return new Point2D(dialogBox.x + 5, 5);

            case TOP_RIGHT:
                return new Point2D((int) dialogBox.getMaxX() - portrait.image().getWidth() - 5, 5);

            case BOTTOM_LEFT:
                return new Point2D(dialogBox.x + 5, dialogBox.y - Sprites.Res_Hud.portrait.image().getHeight() - 5);

            case BOTTOM_RIGHT:
                return new Point2D((int) dialogBox.getMaxX() - portrait.image().getWidth() - 5,
                        dialogBox.y - Sprites.Res_Hud.portrait.image().getHeight() - 5);
            }
            return new Point2D(0, 0);
        }
    }

    /**
     * The emotion of the Pokemon. Unused for now.
     */
    public PortraitEmotion emotion;

    /**
     * The Pokemon species talking. null if not a Pokemon.
     */
    public final PokemonSpecies pokemon;

    /**
     * Where to draw the portrait, if any. May be <code>null</code> and has no effect if pokemon is null.
     */
    public DialogPortraitLocation portraitLocation;

    /**
     * True if the talking Pokemon is shiny. Has no effect if pokemon is null.
     */
    public final boolean shiny;

    /**
     * If not null, the name of the speaker to appear in front of the text.
     */
    public final Message speakerName;

    public PokemonDialogScreen(PokemonSpecies pokemon, Message message) {
        this(pokemon, message, pokemon == null ? null : pokemon.speciesName());
    }

    public PokemonDialogScreen(PokemonSpecies pokemon, Message message, Message speakerName) {
        this(pokemon, message, PortraitEmotion.Normal, false, speakerName, DialogPortraitLocation.BOTTOM_LEFT);
    }

    public PokemonDialogScreen(Pokemon pokemon, Message message, DialogPortraitLocation portraitLocation) {
        this(pokemon, message, PortraitEmotion.Normal, portraitLocation);
    }

    public PokemonDialogScreen(Pokemon pokemon, Message message, PortraitEmotion emotion,
            DialogPortraitLocation portraitLocation) {
        this(pokemon == null ? null : pokemon.species(), message, emotion, pokemon != null && pokemon.isShiny(),
                pokemon == null ? null : pokemon.getNickname(), portraitLocation);
    }

    public PokemonDialogScreen(PokemonSpecies species, Message message, PortraitEmotion emotion, boolean shiny,
            Message speakerName, DialogPortraitLocation portraitLocation) {
        super(message);

        this.pokemon = species;
        this.speakerName = speakerName;
        this.emotion = emotion;
        this.shiny = shiny;
        this.portraitLocation = portraitLocation;

        if (this.speakerName != null)
            this.message.addPrefix(new Message(": ", false)).addPrefix(this.speakerName);
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        super.render(g, width, height);

        if (this.pokemon != null) {
            Rectangle dialogBox = this.parentState.dialogBox();
            Point2D portraitL = this.portraitLocation.locate(dialogBox, Sprites.Res_Hud.portrait);
            PokemonPortrait.drawPortrait(g, this.pokemon, this.emotion, this.shiny, (int) portraitL.x,
                    (int) portraitL.y, this.portraitLocation.flip);
        }
    }

    @Override
    protected boolean switchAnimation() {
        if (!super.switchAnimation())
            return false;
        if (this.parentState.nextScreen() instanceof PokemonDialogScreen) {
            PokemonDialogScreen next = (PokemonDialogScreen) this.parentState.nextScreen();
            if (this.pokemon == null)
                return next.pokemon == null;
            return this.pokemon.equals(next.pokemon) && this.emotion == next.emotion;
        }
        return true;
    }
}