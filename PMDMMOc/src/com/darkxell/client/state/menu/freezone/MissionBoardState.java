package com.darkxell.client.state.menu.freezone;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.Sprite;
import com.darkxell.client.resources.images.Sprites.Res_Hud;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.Registries;
import com.darkxell.common.mission.InvalidParammetersException;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class MissionBoardState extends AbstractState {

    private AbstractState exploresource;
    private ArrayList<Mission> missions = new ArrayList<>();
    Sprite billboard = new Sprite("/hud/billboard_list.png");
    private int currentpage = 1;

    private int selectedmissionpos = 0;

    public MissionBoardState(AbstractState exploresource) {
        this.exploresource = exploresource;
    }

    @Override
    public void onKeyPressed(Key key) {
        switch (key) {
        case RUN:
            Persistence.stateManager.setState(new MissionBoardSelectionState(this.exploresource));
            break;
        case ATTACK:
            if (this.missions.size() > 0)
                Persistence.stateManager.setState(
                        new MissionDetailsState(this.exploresource, this.missions.get(selectedmissionpos), this));
            break;
        case UP:
            if (selectedmissionpos > 0 && selectedmissionpos % 4 != 0)
                selectedmissionpos--;
            break;
        case DOWN:
            if (selectedmissionpos < missions.size() - 1 && selectedmissionpos % 4 != 3)
                selectedmissionpos++;
            break;
        case RIGHT:
            if (currentpage < pages()) {
                currentpage++;
                selectedmissionpos += 4;
            }
            break;
        case LEFT:
            if (currentpage > 1) {
                currentpage--;
                selectedmissionpos -= 4;
            }
            break;
        default:
            break;
        }
    }

    @Override
    public void onKeyReleased(Key key) {

    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        this.exploresource.render(g, width, height);
        g.drawImage(billboard.image(), 0, 0, null);

        if (currentpage != 1)
            g.drawImage(Res_Hud.menuHud.tabLeft(), 240, 40, null);
        TextRenderer.render(g, currentpage + "/" + pages(), 250, 40);
        if (currentpage != pages())
            g.drawImage(Res_Hud.menuHud.tabRight(), 270, 40, null);

        int offsetx = 55, offsety = -6;
        int baseheight = 30, basewidth = width - (2 * offsetx);
        for (int i = (currentpage - 1) * 4; i < (currentpage - 1) * 4 + 4 && i < this.missions.size(); i++) {
            offsety += (baseheight + 28);
            g.translate(offsetx, offsety);
            drawMission(g, basewidth, baseheight, missions.get(i));
            if (i == selectedmissionpos) {
                g.setColor(Color.CYAN);
                g.drawRect(0, 0, basewidth, baseheight);
            }
            g.translate(-offsetx, -offsety);
        }
    }

    int requesttimer = 10;

    @Override
    public void update() {
        this.exploresource.update();
        if (selectedmissionpos >= missions.size())
            selectedmissionpos = missions.size() - 1;
        if (selectedmissionpos < 0 && missions.size() > 0)
            selectedmissionpos = 0;
        if (missions.size() <= 0)
            requesttimer--;
        if (requesttimer <= 0) {
            requesttimer = 240;
            JsonObject message = Json.object();
            message.add("action", "getmissions");
            Persistence.socketendpoint.sendMessage(message.toString());
        }
    }

    private void drawMission(Graphics2D g, int width, int height, Mission mission) {
        g.setColor(new Color(150, 150, 150, 100));
        g.fillRect(0, 0, width, height);
        // 1st line
        Message m = new Message(mission.getMissionFlavor().summaryid);
        m.addReplacement("<pokemon>", Registries.species().find(mission.getPokemonid2()).speciesName());
        m.addReplacement("<item>", Registries.items().find(mission.getItemid()).name());
        TextRenderer.render(g, m, 5, 5);
        TextRenderer.render(g, mission.getDifficulty(), width - 10, 5);
        // 2nd line
        int dungeontextlength = TextRenderer.width(Registries.dungeons().find(mission.getDungeonid()).name());
        TextRenderer.setColor(Color.YELLOW);
        TextRenderer.render(g, Registries.dungeons().find(mission.getDungeonid()).name(), 5, 15);
        TextRenderer.render(g, new Message("mission.floor") + " <blue>" + mission.getFloor() + "</color>",
                15 + dungeontextlength, 15);
    }

    /** Returns the ammount of pages the billboard has */
    private int pages() {
        if (missions.size() <= 4)
            return 1;
        return (missions.size() + 1) / 4 + 1;
    }

    /** Method called when the board recieves the data from the server. */
    public void recieveMissions(JsonObject message) {
        JsonArray arr = message.get("missions").asArray();
        for (int i = 0; i < arr.size(); i++)
            try {
                this.missions.add(new Mission(arr.get(i).asString()));
            } catch (InvalidParammetersException e) {
                Logger.w("Could not load mission " + arr.get(i) + " to billboard.");
            }
    }

}
