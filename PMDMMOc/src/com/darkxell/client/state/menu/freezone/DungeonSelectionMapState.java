package com.darkxell.client.state.menu.freezone;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.image.Sprites.HudSprites;
import com.darkxell.client.resources.image.Sprites.MapSprites;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.freezone.FreezoneExploreState;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.mission.InvalidParammetersException;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;

public class DungeonSelectionMapState extends AbstractState {

    private int camerax = 0;
    private int cameray = 0;
    private ArrayList<String> currentMissions;
    private Message currentTitle;
    private int cursor = 0;
    private ArrayList<Dungeon> dungeonslist;
    private ArrayList<Mission> missions;
    private MenuWindow missionsWindow;

    public DungeonSelectionMapState() {
        super();
        this.dungeonslist = Persistence.player.getAccessibleDungeons();
        {
            if (!this.dungeonslist.isEmpty())
                this.camerax = this.dungeonslist.get(0).getMapX();
            this.cameray = this.dungeonslist.get(0).getMapY();
        }
        this.missions = new ArrayList<>();
        for (String m : Persistence.player.getMissions())
            try {
                this.missions.add(new Mission(m));
            } catch (InvalidParammetersException e) {
                e.printStackTrace();
            }

        this.onDungeonSelect();
    }

    private void onDungeonSelect() {
        this.currentTitle = new Message("mission.list.title").addReplacement("<dungeon>",
                this.dungeonslist.get(this.cursor).name());

        ArrayList<Mission> current = new ArrayList<>(this.missions);
        current.removeIf(m -> m.getDungeonid() != this.dungeonslist.get(this.cursor).getID());
        int maxWidth = 0;
        this.currentMissions = new ArrayList<>();
        for (Mission m : current) {
            String summary = m.summary();
            this.currentMissions.add(summary);
            maxWidth = Math.max(maxWidth, TextRenderer.width(summary));
        }

        this.missionsWindow = new MenuWindow(new Rectangle(10, 25, maxWidth + MenuWindow.MARGIN_X * 2,
                current.size() * (TextRenderer.height() + TextRenderer.lineSpacing()) + MenuWindow.MARGIN_Y * 2));
        this.missionsWindow.isOpaque = true;
    }

    public void onDungeonStart(int dungeon, long seed) {
        Persistence.isCommunicating = false;
        if (dungeon != this.dungeonslist.get(this.cursor).getID())
            Logger.w("Received dungeon ID to start is different than selected; starting cancelled");
        else
            StateManager.setDungeonState(this, dungeon, seed);
    }

    @Override
    public void onKeyPressed(Key key) {
        boolean cursorchanged = false;
        switch (key) {
        case RIGHT:
            if (cursor >= dungeonslist.size() - 1)
                cursor = 0;
            else
                ++cursor;
            cursorchanged = true;
            break;
        case LEFT:
            if (cursor <= 0)
                cursor = dungeonslist.size() - 1;
            else
                --cursor;
            cursorchanged = true;
            break;
        case UP:
            if (cursor < dungeonslist.size() - 10)
                cursor += 10;
            else
                cursor = 0;
            cursorchanged = true;
            break;
        case DOWN:
            if (cursor >= 10)
                cursor -= 10;
            else
                cursor = (dungeonslist.size() - 1);
            cursorchanged = true;
            break;
        case RUN:
            Persistence.isCommunicating = false;
            Persistence.currentplayer.y -= 1;
            Persistence.currentplayer.renderer().sprite().setFacingDirection(Direction.NORTH);
            Persistence.stateManager.setState(new FreezoneExploreState());
            break;
        case ATTACK:
            // Sending dungeonstart to server
            launchDungeon();
            break;
        default:
            break;
        }

        if (cursorchanged)
            this.onDungeonSelect();
    }

    private void launchDungeon() {
        int dungeon = this.dungeonslist.get(this.cursor).getID();
        // Determines if there is a cutscene before the dungeon and plays it
        switch (dungeon) {
        case 3:
            if (Persistence.player.getData().storyposition == 11) {
                CutsceneManager.playCutscene("skarmory/enter", true);
                return;
            }
        }
        // Enters the dungeon if there is no cutscene.
        if (Persistence.socketendpoint.connectionStatus() == GameSocketEndpoint.CONNECTED) {
            Persistence.isCommunicating = true;
            Persistence.socketendpoint.requestDungeonSeed(dungeon);
        } else {
            Logger.w("Entered a dungeon without server connection.");
            this.onDungeonStart(dungeon, new Random().nextLong());
        }
    }

    @Override
    public void onKeyReleased(Key key) {
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        // CALCULATES TRANSLATIONS
        int translateX = -camerax + (width / 2);
        int translateY = -cameray + (height / 2);

        g.translate(translateX, translateY);
        // DRAWS THE MAP
        g.drawImage(MapSprites.globalMap.image(), 0, 0, null);
        for (int i = 0; i < dungeonslist.size(); ++i)
            g.drawImage(i == cursor ? MapSprites.pins.blue() : MapSprites.pins.yellow(), dungeonslist.get(i).getMapX(),
                    dungeonslist.get(i).getMapY(), null);
        // TRANSLATES THE GRAPHICS BACK
        g.translate(-translateX, -translateY);

        int temp_width = width - 40;
        int temp_height = temp_width * HudSprites.textwindow.image().getHeight()
                / HudSprites.textwindow.image().getWidth();
        Rectangle box = new Rectangle(20, height - temp_height - 20, temp_width, temp_height);
        g.drawImage(HudSprites.textwindow.image(), box.x, box.y, box.width, box.height, null);
        g.drawImage(HudSprites.menuHud.tabLeft(), box.x + box.width - 45, box.y, null);
        g.drawImage(HudSprites.menuHud.tabRight(), box.x + box.width - 30, box.y, null);
        String dungeonsmarker = (this.cursor + 1) + " / " + this.dungeonslist.size();
        TextRenderer.render(g, dungeonsmarker, box.x + box.width - 55, box.y + 10);

        TextRenderer.render(g, dungeonslist.get(cursor).name(), box.x + 15, box.y + 10);

        // DRAWS THE HUD OVER THE MAP
        int textxpos = width - Math.max(TextRenderer.width(new Message("dungeonmap.select")),
                TextRenderer.width(new Message("dungeonmap.return"))) - 10;
        TextRenderer.render(g, new Message("dungeonmap.select"), textxpos, box.y - 40);
        TextRenderer.render(g, new Message("dungeonmap.return"), textxpos, box.y - 20);

        if (!this.currentMissions.isEmpty()) {
            this.missionsWindow.render(g, this.currentTitle, width, height);
            int x = this.missionsWindow.inside().x + TextRenderer.lineSpacing(),
                    y = this.missionsWindow.inside().y + TextRenderer.lineSpacing();
            for (String mission : this.currentMissions) {
                TextRenderer.render(g, mission, x, y);
                y += TextRenderer.height() + TextRenderer.lineSpacing();
            }
        }
    }

    @Override
    public void update() {
        if (camerax < dungeonslist.get(cursor).getMapX() - 6)
            camerax += 5;
        else if (camerax > dungeonslist.get(cursor).getMapX() + 6)
            camerax -= 5;
        if (cameray < dungeonslist.get(cursor).getMapY() - 6)
            cameray += 5;
        else if (cameray > dungeonslist.get(cursor).getMapY() + 6)
            cameray -= 5;
    }

}
