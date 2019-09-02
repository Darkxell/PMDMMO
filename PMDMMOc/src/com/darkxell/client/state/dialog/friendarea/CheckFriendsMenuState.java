package com.darkxell.client.state.dialog.friendarea;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.components.TextWindow;
import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class CheckFriendsMenuState extends OptionSelectionMenuState {

    private TextWindow detailsWindow;
    public final FriendAreaShopDialog dialog;

    public CheckFriendsMenuState(AbstractGraphiclayer background, FriendAreaShopDialog dialog, boolean isOpaque) {
        super(background, isOpaque);
        this.dialog = dialog;
        this.createOptions();
        this.onOptionChanged(this.currentOption());
    }

    @Override
    protected void createOptions() {
        ArrayList<PokemonSpecies> species = Persistence.player.getKnownPokemon();
        int count = 0;
        MenuTab tab = new MenuTab("general.pokemon");
        for (PokemonSpecies s : species) {
            tab.addOption(new CheckFriendMenuOption(s));
            ++count;
            if (count >= 15) {
                this.tabs.add(tab);
                tab = new MenuTab("general.pokemon");
                count = 0;
            }
        }
        if (count > 0)
            this.tabs.add(tab);
    }

    private Rectangle detailsWindowDimensions() {
        Rectangle r = this.mainWindowDimensions();
        return new Rectangle((int) (r.getMaxX() + 10), r.y, PrincipalMainState.displayWidth - 30 - r.width, r.height);
    }

    private Message detailsWindowMessage() {
        PokemonSpecies species = ((CheckFriendMenuOption) this.currentOption()).species;
        Message m = new Message("dialog.friendareas.check.details");
        m.addReplacement("<pokemon>", CheckFriendMenuOption.createOption(species));
        m.addReplacement("<area>", species.friendArea().getName());

        Message areaStatus = new Message("dialog.friendareas.check.not_sold");
        if (Persistence.player.friendAreas.contains(species.friendArea()))
            areaStatus = new Message("dialog.friendareas.check.accessible");
        else if (BuyFriendAreaMenuState.getBuyableFriendAreas().contains(species.friendArea()))
            areaStatus = new Message("dialog.friendareas.check.not_accessible");
        m.addReplacement("<areastatus>", areaStatus);

        Message dungeons = null;
        for (Dungeon dungeon : Persistence.player.getAccessibleDungeons())
            if (dungeon.isRecruitable(species)) {
                if (dungeons == null)
                    dungeons = dungeon.name();
                else
                    dungeons.addSuffix(", ").addSuffix(dungeon.name());
            }
        if (dungeons == null)
            dungeons = new Message("???");
        m.addReplacement("<dungeons>", dungeons);

        return m;
    }

    @Override
    protected void onExit() {
        this.dialog.unpause();
    }

    @Override
    protected void onOptionChanged(MenuOption option) {
        super.onOptionChanged(option);

        if (option == null)
            this.detailsWindow = null;
        else {
            this.detailsWindow = new TextWindow(this.detailsWindowDimensions(), this.detailsWindowMessage(), false);
            this.detailsWindow.isOpaque = this.isOpaque();
        }
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        super.render(g, width, height);

        if (this.detailsWindow != null)
            this.detailsWindow.render(g, null, width, height);
    }

}
