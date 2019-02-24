package com.darkxell.client.state.menu.menus;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.darkxell.client.graphics.AbstractGraphicsLayer;
import com.darkxell.client.graphics.TextRenderer;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.resources.images.hud.MenuStateHudSpriteset;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.components.MoveSelectionWindow;
import com.darkxell.client.state.menu.components.OptionSelectionWindow;
import com.darkxell.client.state.menu.components.TextWindow;
import com.darkxell.client.state.menu.dungeon.MoveInfoState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.event.move.MoveEnabledEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveSwitchedEvent;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class MovesMenuState extends OptionSelectionMenuState {

    public static class MoveMenuOption extends MenuOption {

        public final LearnedMove move;

        public MoveMenuOption(LearnedMove move, boolean isMain) {
            super((move == null ? new Message("", false)
                    : move.move().name().addPrefix(isMain || !move.isEnabled() ? "  " : "<star> ")));
            this.move = move;
        }
    }

    /** True if moves can be ordered in this State. */
    protected boolean canOrder = true;
    private Pokemon[] pokemon;
    private MoveSelectionWindow window;
    private TextWindow windowInfo;
    public final boolean inDungeon;
    public final AbstractState parent;

    public MovesMenuState(AbstractState parent, AbstractGraphicsLayer background, boolean inDungeon,
            Pokemon... pokemon) {
        super(background);
        this.parent = parent;
        this.inDungeon = inDungeon;
        this.pokemon = pokemon;
        this.createOptions();
        this.createWindows();
    }

    @Override
    protected void createOptions() {
        for (Pokemon pokemon : this.pokemon) {
            MenuTab moves = new MenuTab(new Message("moves.title").addReplacement("<pokemon>", pokemon.getNickname()));
            boolean isStruggling = true;
            for (int i = 0; i < 4; ++i)
                if (pokemon.move(i) != null) {
                    if (pokemon.move(i).pp() > 0) isStruggling = false;
                    moves.addOption(new MoveMenuOption(pokemon.move(i), pokemon == Persistence.player.getTeamLeader()));
                }
            if (pokemon.player().getTeamLeader() == pokemon && isStruggling)
                moves.addOption(new MoveMenuOption(new LearnedMove(MoveRegistry.STRUGGLE.id),
                        pokemon == Persistence.player.getTeamLeader()));
            this.tabs.add(moves);
        }
    }

    private void createWindows() {
        this.window = new MoveSelectionWindow(this, this.mainWindowDimensions());
        Rectangle r = new Rectangle(this.window.dimensions.x, (int) (this.window.dimensions.getMaxY() + 20),
                PrincipalMainState.displayWidth - 40, MenuStateHudSpriteset.cornerSize.height * 2
                        + TextRenderer.height() * 4 + TextRenderer.lineSpacing() * 2);
        this.windowInfo = new TextWindow(r, this.infoText(), false);
    }

    @Override
    public OptionSelectionWindow getMainWindow() {
        return this.window;
    }

    protected Message infoText() {
        return new Message(
                !this.inDungeon ? "moves.info.freezone" : this.isMainSelected() ? "moves.info.main" : "moves.info.ally")
                        .addReplacement("<key-ok>", KeyEvent.getKeyText(Key.ATTACK.keyValue()))
                        .addReplacement("<key-info>", KeyEvent.getKeyText(Key.ROTATE.keyValue()))
                        .addReplacement("<key-shift>", KeyEvent.getKeyText(Key.DIAGONAL.keyValue()));
    }

    private boolean isMainSelected() {
        return this.selectedPokemon() == Persistence.player.getTeamLeader();
    }

    @Override
    protected Rectangle mainWindowDimensions() {
        Rectangle r = super.mainWindowDimensions();
        return new Rectangle(r.x, r.y, r.width + MoveSelectionWindow.ppLength, r.height);
    }

    @Override
    protected void onExit() {
        Persistence.stateManager.setState(this.parent);
    }

    @Override
    public void onKeyPressed(Key key) {
        if (!this.canOrder || !Key.DIAGONAL.isPressed()) {
            if (this.tabs.size() != 0) {
                if (key == Key.LEFT && this.tab > 0) --this.tab;
                else if (key == Key.RIGHT && this.tab < this.tabs.size() - 1) ++this.tab;
                else if (key == Key.UP) --this.selection;
                else if (key == Key.DOWN) ++this.selection;
                else if (key == Key.ATTACK) this.onOptionSelected(this.currentOption());
                else if (key == Key.ROTATE) {
                    this.onOptionInfo(this.currentOption());
                    SoundManager.playSound("ui-select");
                }

                if (key == Key.LEFT || key == Key.RIGHT) {
                    if (this.selection >= this.currentTab().options().length)
                        this.selection = this.currentTab().options().length - 1;
                    this.onTabChanged(this.currentTab());
                    SoundManager.playSound("ui-move");
                } else if (key == Key.UP || key == Key.DOWN) {
                    if (this.selection == -1) this.selection = this.currentTab().options().length - 1;
                    else if (this.selection == this.currentTab().options().length) this.selection = 0;
                    this.onOptionChanged(this.currentOption());
                    SoundManager.playSound("ui-move");
                }
            }
            if (key == Key.MENU || key == Key.RUN) {
                this.onExit();
                SoundManager.playSound("ui-back");
            }
        } else {
            int from = -1, to = -1;

            boolean success = false;
            if (key == Key.UP && this.selection > 0) {
                from = this.selection;
                to = this.selection - 1;
                --this.selection;
                success = true;
                SoundManager.playSound("ui-sort");
            } else if (key == Key.DOWN && this.selection < this.currentTab().options().length - 1) {
                from = this.selection;
                to = this.selection + 1;
                ++this.selection;
                success = true;
                SoundManager.playSound("ui-sort");
            }

            if (success) {
                Persistence.eventProcessor().processEvent(new MoveSwitchedEvent(Persistence.floor,
                        DungeonEventSource.PLAYER_ACTION, this.selectedPokemon(), from, to).setPAE());

                MovesMenuState s = new MovesMenuState(this.parent, this.background, this.inDungeon, this.pokemon);
                s.selection = this.selection;
                s.tab = this.tab;
                Persistence.stateManager.setState(s);
            }
        }
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        super.onMouseRightClick(x, y);
        if (this.getHoveredOption() != null) this.onOptionInfo(this.getHoveredOption());
    }

    private void onOptionInfo(MenuOption option) {
        Persistence.stateManager
                .setState(new MoveInfoState(((MoveMenuOption) option).move.move(), this.background, this));
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        if (!this.inDungeon) {
            this.onOptionInfo(option);
            return;
        }

        DungeonState s = Persistence.dungeonState;
        LearnedMove move = ((MoveMenuOption) option).move;

        if (this.isMainSelected()) {
            if (move != null) {
                Persistence.stateManager.setState(s);
                if (move.pp() == 0)
                    s.logger.showMessage(new Message("moves.no_pp").addReplacement("<move>", move.move().name()));
                else if (!Persistence.player.getDungeonLeader().canUse(move, Persistence.floor))
                    s.logger.showMessage(new Message("moves.cant_use").addReplacement("<move>", move.move().name()));
                else Persistence.eventProcessor().processEvent(new MoveSelectionEvent(Persistence.floor,
                        DungeonEventSource.PLAYER_ACTION, move, Persistence.player.getDungeonLeader()).setPAE());
            }
        } else {
            Persistence.eventProcessor().processEvent(
                    new MoveEnabledEvent(Persistence.floor, DungeonEventSource.PLAYER_ACTION, move, !move.isEnabled())
                            .setPAE());
            MovesMenuState state = new MovesMenuState(s, s, true, this.pokemon);
            state.tab = this.tab;
            state.selection = this.selection;
            Persistence.stateManager.setState(state);
        }
    }

    @Override
    protected void onTabChanged(MenuTab tab) {
        super.onTabChanged(tab);
        this.createWindows();
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        super.render(g, width, height);
        this.windowInfo.render(g, null, width, height);
    }

    private Pokemon selectedPokemon() {
        return this.pokemon[this.tabIndex()];
    }

}
