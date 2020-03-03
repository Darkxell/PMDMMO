package com.darkxell.client.state.menu.freezone;

import java.awt.Rectangle;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.menu.MenuOption;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.menus.MovesMenuState;
import com.darkxell.client.state.menu.menus.TeamMenuState;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.JsonObject;

public class TeamMemberActionSelectionState extends OptionSelectionMenuState<MenuOption> {

    public final TeamMenuState parent;
    public final Pokemon pokemon;
    private MenuOption summary, moves, leave, back;
    public final FreezoneMenuState superParent;

    public TeamMemberActionSelectionState(FreezoneMenuState superParent, TeamMenuState parent, Pokemon pokemon) {
        super(parent);
        this.parent = parent;
        this.superParent = superParent;
        this.pokemon = pokemon;
        this.createOptions();
    }

    @Override
    protected void createOptions() {
        MenuTab<MenuOption> tab = new MenuTab<>();
        tab.addOption(this.summary = new MenuOption("friendareas.summary"));
        tab.addOption(this.moves = new MenuOption("menu.moves"));
        if (Persistence.player.allies.indexOf(this.pokemon) > 0)
            tab.addOption(this.leave = new MenuOption("ui.friend.leave"));
        tab.addOption(this.back = new MenuOption("general.back"));
        this.tabs.add(tab);
    }

    public void handleMessage(JsonObject message) {
        Persistence.isCommunicating = false;
        if (message.getString("result", "error").equals("success"))
            this.onLeaveSuccess();
        else {
            Logger.e("Client/Server desync. Please reboot game.");
            Persistence.stateManager
                    .setState(new DialogState(this.background, finish -> Persistence.stateManager.setState(this),
                            new DialogScreen(new Message("ui.desync"))).setOpaque(this.isOpaque()));
        }
    }

    @Override
    protected Rectangle mainWindowDimensions() {
        Rectangle r = super.mainWindowDimensions();
        Rectangle parent = this.parent.getMainWindow().dimensions;
        return new Rectangle((int) (parent.getMaxX() + 10), parent.y, r.width, r.height);
    }

    @Override
    protected void onExit() {
        Persistence.stateManager.setState(this.parent);
    }

    private void onLeaveSuccess() {
        Persistence.player.removeAlly(this.pokemon);
        Persistence.player.addPokemonInZone(this.pokemon);
        Persistence.stateManager.setState(this.parent.recreate());
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        if (option == this.summary)
            Persistence.stateManager.setState(TeamMenuState
                    .createSummaryState(this.parent.background, this, null, this.pokemon).setOpaque(this.isOpaque()));
        else if (option == this.moves)
            Persistence.stateManager.setState(
                    new MovesMenuState(this, this.parent.background, false, this.pokemon).setOpaque(this.isOpaque()));
        else if (option == this.leave) {
            if (Persistence.socketendpoint.connectionStatus() == GameSocketEndpoint.CONNECTED) {
                Persistence.isCommunicating = true;
                JsonObject message = new JsonObject().add("action", "removefromteam").add("pokemonid",
                        this.pokemon.id());
                Persistence.socketendpoint.sendMessage(message.toString());
            } else
                this.onLeaveSuccess();
        } else if (option == this.back)
            this.onExit();
    }

}
