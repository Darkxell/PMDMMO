package com.darkxell.client.state.menu.freezone;

import static com.darkxell.client.resources.image.pokemon.portrait.AbstractPortraitSpriteset.PORTRAIT_SIZE;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.launchable.messagehandlers.MonsterRequestHandler;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.image.hud.MenuStateHudSpriteset;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.freezone.FreezoneExploreState;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.components.FriendsWindow;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.dbobject.DatabaseIdentifier;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.JsonObject;

public class FriendSelectionState extends AbstractMenuState {

    public static class FriendMenuOption extends MenuOption {

        public final Pokemon pokemon;

        public FriendMenuOption(Pokemon pokemon) {
            super(pokemon.getNickname());
            this.pokemon = pokemon;
        }

    }

    public static final int COMTICK_MAX = 1000;
    public static final String GOTOMAP = "friendareas.gotomap", TITLE = "friendareas.title";
    public static final int LIST_FRIEND_WIDTH = 4, LIST_FRIEND_HEIGHT = 4,
            MAX_FRIEND_COUNT = LIST_FRIEND_WIDTH * LIST_FRIEND_HEIGHT;
    public static final int LIST_OFFSET = 5, FRIEND_SIZE = PORTRAIT_SIZE, FRIEND_OFFSET = 1,
            FRIEND_SLOT_WIDTH = FRIEND_SIZE + FRIEND_OFFSET * 2 - 1,
            FRIEND_SLOT_HEIGHT = FRIEND_SIZE + FRIEND_OFFSET * 2 - 1,
            FRIEND_NAME_OVERLAY_HEIGHT = TextRenderer.height() + 2;
    public static final int WIDTH = (FRIEND_SLOT_WIDTH + LIST_OFFSET) * LIST_FRIEND_WIDTH + LIST_OFFSET
            + MenuWindow.MARGIN_X, GOTOMAP_HEIGHT = 20,
            HEIGHT = (FRIEND_SLOT_HEIGHT + LIST_OFFSET) * LIST_FRIEND_HEIGHT + GOTOMAP_HEIGHT + LIST_OFFSET * 2
                    + MenuWindow.MARGIN_Y;

    private int comtick = 0;
    public ArrayList<Long> loadingPokemon;
    public MenuOption mapOption;
    private MenuWindow nameWindow;
    public final int startLoadingCount;
    private FriendsWindow window;

    public FriendSelectionState(AbstractState backgroundState) {
        super(backgroundState);
        this.loadingPokemon = new ArrayList<>(Persistence.player.pokemonInZones.keySet());
        this.loadingPokemon.removeIf(l -> Persistence.player.pokemonInZones.get(l) != null);
        this.startLoadingCount = this.loadingPokemon.size();
        if (this.isLoaded())
            this.createOptions();
        else
            this.askNextPokemon();
    }

    private void askNextPokemon() {
        this.comtick = 0;
        Persistence.socketendpoint.requestMonster(this.loadingPokemon.get(0));
        Persistence.isCommunicating = true;
    }

    @Override
    protected void createOptions() {
        this.window = null;
        ArrayList<DatabaseIdentifier> mons = Persistence.player.getData().pokemonsinzones;
        mons.sort((o1, o2) -> {
            PokemonSpecies s1 = Persistence.player.pokemonInZones.get(o1.id).species();
            PokemonSpecies s2 = Persistence.player.pokemonInZones.get(o2.id).species();
            PokemonSpecies p1 = s1.parent(), p2 = s2.parent();
            if (p1.id == p2.id)
                return Integer.compare(s1.formID, s2.formID);
            return Integer.compare(p1.id, p2.id);
        });
        this.mapOption = new MenuOption(GOTOMAP);
        MenuTab current = new MenuTab(TITLE);
        int indexInTab = 0, index = 0;
        for (int i = 0; i < mons.size(); ++i) {
            MenuOption option;
            if (indexInTab == 0) {
                option = this.mapOption;
                --i;
            } else {
                long id = mons.get(index++).id;
                option = new FriendMenuOption(Persistence.player.pokemonInZones.get(id));
            }
            current.addOption(option);
            ++indexInTab;

            if (indexInTab >= MAX_FRIEND_COUNT + 1) {
                indexInTab = 0;
                this.tabs.add(current);
                current = new MenuTab(TITLE);
            }
        }
        if (mons.size() == 0) {
            ++indexInTab;
            current.addOption(this.mapOption);
        }
        if (indexInTab != 0)
            this.tabs.add(current);

        for (int tab = 0; tab < this.tabs.size(); ++tab)
            tabs.get(tab).name.addSuffix(" (" + (tab + 1) + "/" + this.tabs.size() + ")");
    }

    public boolean isLoaded() {
        return this.loadingPokemon.isEmpty();
    }

    @Override
    protected Rectangle mainWindowDimensions() {
        Rectangle superRect = this.isLoaded() ? super.mainWindowDimensions() : new Rectangle(16, 32, WIDTH, HEIGHT);
        return new Rectangle(superRect.x, superRect.y, WIDTH, HEIGHT);
    }

    public MenuWindow nameWindow() {
        return this.nameWindow;
    }

    @Override
    protected void onExit() {
        Persistence.currentplayer.x += 1;
        Persistence.currentplayer.renderer().sprite().setFacingDirection(Direction.EAST);
        Persistence.stateManager.setState(new FreezoneExploreState());
    }

    @Override
    public void onKeyPressed(Key key) {
        if (this.tabs.size() != 0) {
            int max = this.currentTab().options().length - 1;
            if (key == Key.PAGE_LEFT && this.tab > 0)
                --this.tab;
            else if (key == Key.PAGE_RIGHT && this.tab < this.tabs.size() - 1)
                ++this.tab;
            else if (key == Key.LEFT)
                --this.selection;
            else if (key == Key.RIGHT) {
                if (this.selection == max)
                    this.selection = 0;
                else
                    ++this.selection;
            } else if (key == Key.UP) {
                if (this.selection == 0)
                    this.selection = max;
                else if (this.selection <= LIST_FRIEND_WIDTH && this.selection != 0)
                    this.selection = 0;
                else
                    this.selection -= LIST_FRIEND_WIDTH;
            } else if (key == Key.DOWN) {
                if (this.selection == 0)
                    this.selection = 1;
                else if (this.selection > (max / LIST_FRIEND_WIDTH) * LIST_FRIEND_WIDTH)
                    this.selection = 0;
                else
                    this.selection += LIST_FRIEND_WIDTH;
            } else if (key == Key.ATTACK) {
                this.onOptionSelected(this.currentOption());
                SoundManager.playSound("ui-select");
            }

            if (key == Key.PAGE_LEFT || key == Key.PAGE_RIGHT) {
                if (this.selection >= this.currentTab().options().length)
                    this.selection = this.currentTab().options().length - 1;
                this.onTabChanged(this.currentTab());
                SoundManager.playSound("ui-move");
            } else if (key == Key.UP || key == Key.DOWN || key == Key.LEFT || key == Key.RIGHT) {
                if (this.selection >= this.currentTab().options().length)
                    this.selection %= LIST_FRIEND_WIDTH;
                while (this.selection < 0)
                    this.selection += this.currentTab().options().length;
                while (this.selection >= this.currentTab().options().length)
                    --this.selection;
                this.onOptionChanged(this.currentOption());
                SoundManager.playSound("ui-move");
            }
        }
        if (key == Key.MENU || key == Key.RUN) {
            SoundManager.playSound("ui-back");
            this.onExit();
        }
    }

    @Override
    public void onMouseClick(int x, int y) {
        super.onMouseClick(x, y);
        if (!this.isLoaded())
            return;
        if (this.window != null) {
            int option = this.window.optionAt(x, y);
            if (option != -1) {
                this.selection = option;
                this.onOptionSelected(this.currentOption());
            } else if (!this.window.dimensions.contains(new Point(x, y))) {
                SoundManager.playSound("ui-back");
                this.onExit();
            }
        }
    }

    @Override
    public void onMouseMove(int x, int y) {
        super.onMouseMove(x, y);
        if (!this.isLoaded())
            return;
        if (this.window != null) {
            int option = this.window.optionAt(x, y);
            if (option != -1) {
                this.selection = option;
                this.onOptionChanged(this.currentOption());
            }
        }
    }

    @Override
    protected void onOptionChanged(MenuOption option) {
        super.onOptionChanged(option);
        this.updateNameWindow();
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        if (option == this.mapOption)
            Persistence.stateManager.setState(new FriendAreaSelectionMapState());
        else {
            FriendMenuOption o = (FriendMenuOption) option;
            Persistence.stateManager.setState(new FriendSelectionOptionState(this, o.pokemon));
        }
    }

    public void onPokemonReceived(JsonObject message) {
        Persistence.isCommunicating = false;
        Pokemon p = MonsterRequestHandler.readMonster(message);
        Persistence.player.addPokemonInZone(p);
        this.loadingPokemon.remove(p.id());
        if (this.isLoaded())
            this.createOptions();
        else
            this.askNextPokemon();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.selection == 0 && this.currentTab().options().length > 1)
            this.selection = 1;
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        super.render(g, width, height);

        if (this.window == null)
            this.window = new FriendsWindow(this, this.mainWindowDimensions());

        this.window.render(g, this.isLoaded() ? this.currentTab().name : new Message(TITLE), width, height);

        if (this.isLoaded()) {
            if (this.nameWindow == null)
                this.updateNameWindow();
            if (this.currentOption() != this.mapOption) {
                this.nameWindow.render(g, null, width, height);
                Message name = ((FriendMenuOption) this.currentOption()).pokemon.getNickname();
                Rectangle inside = this.nameWindow.inside();
                TextRenderer.render(g, name, this.nameWindow.inside().x + 5,
                        inside.y + inside.height / 2 - TextRenderer.height() / 2);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!this.isLoaded()) {
            ++this.comtick;
            if (this.comtick >= COMTICK_MAX)
                this.askNextPokemon();
        }
    }

    private void updateNameWindow() {
        int maxWidth = 0;
        for (int i = 0; i < this.currentTab().options().length; ++i)
            if (this.currentTab().options()[i] != this.mapOption)
                maxWidth = Math.max(maxWidth,
                        TextRenderer.width(((FriendMenuOption) this.currentTab().options()[i]).pokemon.getNickname()));
        Rectangle main = this.mainWindowDimensions();
        this.nameWindow = new MenuWindow(new Rectangle((int) main.getMaxX() + 5, (int) main.getMinY(),
                maxWidth + MenuWindow.MARGIN_X + MenuStateHudSpriteset.cornerSize.width,
                TextRenderer.height() + MenuWindow.MARGIN_Y + MenuStateHudSpriteset.cornerSize.height * 3 / 2));
    }

}
