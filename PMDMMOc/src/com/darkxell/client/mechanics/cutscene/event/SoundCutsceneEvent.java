package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.common.util.xml.XMLUtils;

public class SoundCutsceneEvent extends CutsceneEvent {

    public final boolean playOverMusic;
    public final String soundID;

    public SoundCutsceneEvent(Element xml, CutsceneContext context) {
        super(xml, CutsceneEventType.sound, context);
        this.soundID = XMLUtils.getAttribute(xml, "sound", null);
        this.playOverMusic = XMLUtils.getAttribute(xml, "overmusic", false);
    }

    public SoundCutsceneEvent(int id, String soundID, boolean playOverMusic) {
        super(id, CutsceneEventType.sound);
        this.soundID = soundID;
        this.playOverMusic = playOverMusic;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.soundID != null)
            if (this.playOverMusic)
                SoundManager.playSoundOverMusic(this.soundID);
            else
                SoundManager.playSound(this.soundID);
    }

    @Override
    public String toString() {
        return this.displayID() + "Play " + this.soundID + (this.playOverMusic ? " over music" : "");
    }

    @Override
    public Element toXML() {
        Element root = super.toXML();
        root.setAttribute("sound", this.soundID);
        XMLUtils.setAttribute(root, "overmusic", this.playOverMusic, false);
        return root;
    }

}
