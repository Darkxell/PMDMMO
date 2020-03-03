package com.darkxell.client.state.menu.freezone;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.entities.FriendPokemonEntity;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.ConfirmDialogScreen;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.client.state.dialog.TextinputState;
import com.darkxell.client.state.menu.MenuOption;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.state.menu.menus.MovesMenuState;
import com.darkxell.client.state.menu.menus.TeamMenuState;
import com.darkxell.common.util.Callbackable;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.JsonObject;

public class FriendActionSelectionState extends OptionSelectionMenuState<MenuOption> implements Callbackable {

    public FriendPokemonEntity friendPokemonEntity;
    private MenuOption join, leave, farewell, summary, moves, rename, back;
    public final AbstractState parent;
    private MenuWindow summaryWindow;

    public FriendActionSelectionState(AbstractState parent, FriendPokemonEntity friendPokemonEntity) {
        super(parent, true);
        this.friendPokemonEntity = friendPokemonEntity;
        this.parent = parent;
        this.createOptions();
    }

    @Override
    public void callback(String s) {
        if (Persistence.socketendpoint.connectionStatus() == GameSocketEndpoint.CONNECTED) {
            JsonObject message = new JsonObject().add("action", "nickname")
                    .add("pokemonid", this.friendPokemonEntity.pokemon.id()).add("nickname", s);
            Persistence.socketendpoint.sendMessage(message.toString());
        }
        this.friendPokemonEntity.pokemon.setNickname(s);
        Persistence.stateManager.setState(new FriendActionSelectionState(this.parent, this.friendPokemonEntity));
    }

    @Override
    protected void createOptions() {
        MenuTab<MenuOption> tab = new MenuTab<>();
        if (Persistence.player.positionInTeam(this.friendPokemonEntity.pokemon) == -1) {
            tab.addOption(this.join = new MenuOption("ui.friend.join"));
            tab.addOption(this.farewell = new MenuOption("ui.friend.farewell"));
        } else
            tab.addOption(this.leave = new MenuOption("ui.friend.leave"));
        tab.addOption(this.summary = new MenuOption("friendareas.summary"));
        tab.addOption(this.moves = new MenuOption("menu.moves"));
        tab.addOption(this.rename = new MenuOption("ui.friend.rename"));
        tab.addOption(this.back = new MenuOption("general.back"));
        this.tabs.add(tab);
    }

    public void handleMessage(JsonObject message) {
        Persistence.isCommunicating = false;
        if (message.getString("action", "nothing").equals("addtoteam")) {
            if (message.getString("result", "error").equals("success"))
                this.onJoinSuccess();
            else if (message.getString("result", "error").equals("team_full"))
                Persistence.stateManager
                        .setState(new DialogState(this.background, finish -> Persistence.stateManager.setState(this),
                                new DialogScreen(new Message("ui.friend.join.full"))).setOpaque(this.isOpaque()));
            else {
                Logger.e("Client/Server desync. Please reboot game.");
                Persistence.stateManager
                        .setState(new DialogState(this.background, finish -> Persistence.stateManager.setState(this),
                                new DialogScreen(new Message("ui.desync"))).setOpaque(this.isOpaque()));
            }
        } else if (message.getString("action", "nothing").equals("removefromteam")) {
            if (message.getString("result", "error").equals("success"))
                this.onLeaveSuccess();
            else {
                Logger.e("Client/Server desync. Please reboot game.");
                Persistence.stateManager
                        .setState(new DialogState(this.background, finish -> Persistence.stateManager.setState(this),
                                new DialogScreen(new Message("ui.desync"))).setOpaque(this.isOpaque()));
            }
        } else if (message.getString("action", "nothing").equals("deletefriend")) {
            if (message.getString("result", "error").equals("success"))
                this.onFarewellSuccess();
            else {
                Logger.e("Client/Server desync. Please reboot game.");
                Persistence.stateManager
                        .setState(new DialogState(this.background, finish -> Persistence.stateManager.setState(this),
                                new DialogScreen(new Message("ui.desync"))).setOpaque(this.isOpaque()));
            }
        } else if (message.getString("action", "nothing").equals("nickname")) {
            if (message.getString("result", "error").equals("success"))
                this.onFarewellSuccess();
            else {
                Logger.e("Client/Server desync. Please reboot game.");
                Persistence.stateManager
                        .setState(new DialogState(this.background, finish -> Persistence.stateManager.setState(this),
                                new DialogScreen(new Message("ui.desync"))).setOpaque(this.isOpaque()));
            }
        }
    }

    @Override
    protected void onExit() {
        Persistence.stateManager.setState(this.parent);
        this.friendPokemonEntity.release();
    }

    private void onFarewellSuccess() {
        Persistence.player.removePokemonInZone(this.friendPokemonEntity.pokemon);

        DialogEndListener finish = f -> {
            this.onExit();
            Persistence.currentmap.removeEntity(this.friendPokemonEntity);
        };

        Persistence.stateManager.setState(
                new DialogState(this.background, finish, new DialogScreen(new Message("ui.friend.farewell.success")
                        .addReplacement("<pokemon>", this.friendPokemonEntity.pokemon.getNickname())))
                                .setOpaque(this.isOpaque()));
    }

    private void onJoinSuccess() {
        Persistence.player.removePokemonInZone(this.friendPokemonEntity.pokemon);
        Persistence.player.addAlly(this.friendPokemonEntity.pokemon);
        this.onExit();
    }

    private void onLeaveSuccess() {
        Persistence.player.removeAlly(this.friendPokemonEntity.pokemon);
        Persistence.player.addPokemonInZone(this.friendPokemonEntity.pokemon);
        this.onExit();
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        if (option == this.summary)
            Persistence.stateManager.setState(TeamMenuState
                    .createSummaryState(this.background, this, null, this.friendPokemonEntity.pokemon).setOpaque(true));
        else if (option == this.moves)
            Persistence.stateManager.setState(
                    new MovesMenuState(this, this.background, false, this.friendPokemonEntity.pokemon).setOpaque(true));
        else if (option == this.join) {
            if (Persistence.socketendpoint.connectionStatus() == GameSocketEndpoint.CONNECTED) {
                Persistence.isCommunicating = true;
                JsonObject message = new JsonObject().add("action", "addtoteam").add("pokemonid",
                        this.friendPokemonEntity.pokemon.id());
                Persistence.socketendpoint.sendMessage(message.toString());
            } else
                this.onJoinSuccess();
        } else if (option == this.leave) {
            if (Persistence.socketendpoint.connectionStatus() == GameSocketEndpoint.CONNECTED) {
                Persistence.isCommunicating = true;
                JsonObject message = new JsonObject().add("action", "removefromteam").add("pokemonid",
                        this.friendPokemonEntity.pokemon.id());
                Persistence.socketendpoint.sendMessage(message.toString());
            } else
                this.onLeaveSuccess();
        } else if (option == this.farewell) {

            DialogEndListener finish = f -> {

                if (!((ConfirmDialogScreen) f.getScreen(0)).hasConfirmed()) {
                    Persistence.stateManager.setState(this);
                    return;
                }

                Persistence.stateManager.setState(this);

                if (Persistence.socketendpoint.connectionStatus() == GameSocketEndpoint.CONNECTED) {
                    Persistence.isCommunicating = true;
                    JsonObject message = new JsonObject().add("action", "deletefriend").add("pokemonid",
                            this.friendPokemonEntity.pokemon.id());
                    Persistence.socketendpoint.sendMessage(message.toString());
                } else
                    this.onFarewellSuccess();
            };

            Persistence.stateManager
                    .setState(new DialogState(this.background, finish,
                            new ConfirmDialogScreen(new Message("ui.friend.farewell.confirm")
                                    .addReplacement("<pokemon>", this.friendPokemonEntity.pokemon.getNickname())))
                                            .setOpaque(this.isOpaque()));
        } else if (option == this.rename) {
            TextinputState state = new TextinputState(this.background, new Message("ui.friend.rename.title")
                    .addReplacement("<pokemon>", this.friendPokemonEntity.pokemon.getNickname()), this);
            state.content = this.friendPokemonEntity.pokemon.getNickname().asText();
            Persistence.stateManager.setState(state);
        } else if (option == this.back)
            this.onExit();
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        super.render(g, width, height);

        {
            if (this.summaryWindow == null) {
                Rectangle r = this.mainWindowDimensions();
                this.summaryWindow = new MenuWindow(
                        new Rectangle((int) (r.getMaxX() + 10), r.y, (int) (width - r.getMaxX() - 20), r.height));
                this.summaryWindow.isOpaque = this.isOpaque();
            }
            this.summaryWindow.render(g, null, width, height);

            int x = this.summaryWindow.inside().x + 10, y = this.summaryWindow.inside().y + 5;
            TextRenderer.render(g, this.friendPokemonEntity.pokemon.getNickname(), x, y);
            y += TextRenderer.height() + TextRenderer.lineSpacing();
            TextRenderer.render(g, new Message("team.level").addReplacement("<lvl>",
                    String.valueOf(this.friendPokemonEntity.pokemon.level())), x, y);

            if (this.friendPokemonEntity.pokemon.hasItem()) {
                y += TextRenderer.height() + TextRenderer.lineSpacing();
                TextRenderer.render(g, new Message("ui.friend.item").addReplacement("<item>",
                        this.friendPokemonEntity.pokemon.getItem().name()), x, y);
            }
        }
    }

}
