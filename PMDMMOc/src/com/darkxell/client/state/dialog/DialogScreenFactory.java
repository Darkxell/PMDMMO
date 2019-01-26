package com.darkxell.client.state.dialog;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait.PortraitEmotion;
import com.darkxell.client.state.dialog.PokemonDialogScreen.DialogPortraitLocation;
import com.darkxell.common.Registries;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class DialogScreenFactory {
    public static DialogScreen getScreen(Element el) {
        String type = el.getAttributeValue("type");
        switch (type == null ? "dialog" : type) {
            case "portrait":
            case "player":
            case "option":
                return createPortraitScreen(type, el);
            case "narrator":
            case "confirm":
            case "dialog":
            default:
                return createDefaultScreen(type, el);
        }
    }

    private static Message getMessage(Element el) {
        return new Message(XMLUtils.getAttribute(el, "message", "nothing"));
    }

    private static DialogScreen createDefaultScreen(String type, Element el) {
        Message message = getMessage(el);
        switch (type == null ? "dialog" : type) {
            case "narrator":
                return new NarratorDialogScreen(message);
            case "confirm":
                return new ConfirmDialogScreen(message);
            case "dialog":
            default:
                return new DialogScreen(message);
        }
    }

    private static PokemonSpecies getSpecies(Element el) {
        if (el.getAttribute("species") == null) {
            return null;
        }
        return Registries.species().find(XMLUtils.getAttribute(el, "species", 0));
    }

    private static PortraitEmotion getEmotion(Element el) {
        return PortraitEmotion.getByIndex(XMLUtils.getAttribute(el, "emotion", -1));
    }

    private static boolean getShiny(Element el) {
        return XMLUtils.getAttribute(el, "shiny", false);
    }

    private static Message getSpeakerName(PokemonSpecies species, Element el) {
        String speaker = el.getAttributeValue("speaker");
        Message speakerMessage;
        if (speaker == null) {
            speakerMessage = species.speciesName();
        } else if (speaker.equalsIgnoreCase("none")) {
            return null;
        } else {
            speakerMessage = new Message(speaker);
        }

        speakerMessage.addPrefix("<yellow>").addSuffix("</color>");
        return speakerMessage;
    }

    private static DialogPortraitLocation getPortraitLocation(Element el) {
        return DialogPortraitLocation.getByIndex(XMLUtils.getAttribute(el, "portrait", 0));
    }

    private static Message[] getOptions(Element el) {
        List<Message> messages = new ArrayList<>();
        for (Element messageEl : el.getChildren("message")) {
            messages.add(new Message(XMLUtils.getAttribute(messageEl, "id", "nothing")));
        }
        return messages.toArray(new Message[0]);
    }

    private static DialogScreen createPortraitScreen(String type, Element el) {
        PokemonSpecies species = getSpecies(el);
        Message message = getMessage(el);
        DialogPortraitLocation portrait = getPortraitLocation(el);

        switch (type) {
            case "portrait":
                return new PokemonDialogScreen(species, message, getEmotion(el), getShiny(el),
                        getSpeakerName(species, el), portrait);
            case "player":
                Pokemon player = Persistence.player.leaderPokemon;
                return new PokemonDialogScreen(player, message, getEmotion(el), portrait);
            case "option":
            default:
                return new OptionDialogScreen(species, message, portrait, getOptions(el));
        }
    }
}
