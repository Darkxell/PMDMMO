package com.darkxell.client.state.menu.dungeon;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.state.menu.item.ItemContainersMenuState;
import com.darkxell.client.state.menu.menus.MovesMenuState;
import com.darkxell.client.state.menu.menus.SettingsMenuState;
import com.darkxell.client.state.menu.menus.TeamMenuState;
import com.darkxell.common.dungeon.data.Dungeon.DungeonDirection;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class DungeonMenuState extends OptionSelectionMenuState {

    private Message floorMessage;
    private MenuWindow infoWindow;
    private MenuOption moves, items, team, settings, ground;

    public DungeonMenuState(AbstractGraphiclayer background) {
        super(background);
        this.floorMessage = new Message(
                "stairs.floor." + (Persistence.dungeon.dungeon().direction == DungeonDirection.UP ? "up" : "down"))
                        .addReplacement("<floor>", Integer.toString(Persistence.dungeon.currentFloor().id));
        this.createOptions();
    }

    public ItemContainersMenuState createInventoryState() {
        DungeonState s = Persistence.dungeonState;
        ArrayList<ItemContainer> containers = new ArrayList<ItemContainer>();
        containers.add(Persistence.player.inventory());
        containers.add(Persistence.player.getDungeonLeader().tile());
        for (Pokemon pokemon : Persistence.player.getTeam())
            containers.add(pokemon);

        boolean found = false;
        for (ItemContainer container : containers)
            if (container.size() != 0) {
                found = true;
                break;
            }

        if (!found) {
            this.onExit();
            s.logger.showMessage(new Message("inventory.empty"));
            return null;
        } else {
            ItemContainersMenuState i = new ItemContainersMenuState(this, s, true,
                    containers.toArray(new ItemContainer[containers.size()]));
            i.isOpaque = this.isOpaque;
            return i;
        }
    }

    @Override
    protected void createOptions() {
        MenuTab tab = new MenuTab();
        tab.addOption((this.moves = new MenuOption("menu.moves")));
        tab.addOption((this.items = new MenuOption("menu.items")));
        tab.addOption((this.team = new MenuOption("menu.team")));
        tab.addOption((this.settings = new MenuOption("menu.settings")));
        tab.addOption((this.ground = new MenuOption("menu.ground")));
        this.tabs.add(tab);
    }

    public TeamMenuState createPartyState() {
        return new TeamMenuState(this, Persistence.dungeonState);
    }

    private Rectangle infoWindowDimensions() {
        Rectangle main = this.mainWindowDimensions();
        return new Rectangle((int) (main.getMaxX() + 10), main.y,
                PrincipalMainState.displayWidth - 16 * 2 - 10 - main.width, 122);
    }

    @Override
    protected void onExit() {
        Persistence.stateManager.setState((AbstractState) this.background);
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        DungeonState s = Persistence.dungeonState;
        if (option == this.moves)
            Persistence.stateManager.setState(new MovesMenuState(this, s, true, Persistence.player.getTeam()));
        else if (option == this.items)
            Persistence.stateManager.setState(this.createInventoryState());
        else if (option == this.team)
            Persistence.stateManager.setState(this.createPartyState());
        else if (option == this.settings)
            Persistence.stateManager.setState(new SettingsMenuState(this, this.background));
        else if (option == this.ground) {
            this.onExit();
            if (Persistence.player.getDungeonLeader().tile().type() == TileType.STAIR)
                Persistence.stateManager.setState(new StairMenuState());
            else if (Persistence.player.getDungeonLeader().tile().getItem() == null)
                s.logger.showMessage(new Message("ground.empty"));
            else {
                Persistence.stateManager.setState(
                        new ItemContainersMenuState(this, s, true, Persistence.player.getDungeonLeader().tile()));
            }
        }
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        super.render(g, width, height);

        if (this.infoWindow == null)
            this.infoWindow = new MenuWindow(this.infoWindowDimensions());

        this.infoWindow.render(g, null, width, height);
        int x = this.infoWindow.inside().x + 5, y = this.infoWindow.inside().y + 5;
        TextRenderer.render(g, Persistence.dungeon.dungeon().name().addPrefix("<yellow>").addSuffix("</color>"), x, y);
        y += TextRenderer.lineSpacing() + TextRenderer.height();
        TextRenderer.render(g, this.floorMessage, x, y);
        y += 3 * (TextRenderer.lineSpacing() + TextRenderer.height());
        TextRenderer.render(g,
                new Message("menu.belly")
                        .addReplacement("<current>",
                                String.valueOf(Math.round(Persistence.player.getDungeonLeader().getBelly())))
                        .addReplacement("<max>", String.valueOf(Persistence.player.getDungeonLeader().getBellySize())),
                x, y);
        y += TextRenderer.lineSpacing() + TextRenderer.height();
        TextRenderer.render(g,
                new Message("menu.money").addReplacement("<money>", String.valueOf(Persistence.player.moneyInBag())), x,
                y);
        y += TextRenderer.lineSpacing() + TextRenderer.height();
        TextRenderer.render(g, new Message("menu.weather").addReplacement("<weather>",
                String.valueOf(Persistence.floor.currentWeather().weather.name())), x, y);
    }
}
