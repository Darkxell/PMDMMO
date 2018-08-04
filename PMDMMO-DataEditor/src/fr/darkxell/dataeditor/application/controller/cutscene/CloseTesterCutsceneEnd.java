package fr.darkxell.dataeditor.application.controller.cutscene;

import org.jdom2.Element;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;

public class CloseTesterCutsceneEnd extends CutsceneEnd
{

	public CloseTesterCutsceneEnd(Cutscene c)
	{
		super(c);
	}

	@Override
	public Element toXML()
	{
		return new Element("closetester");
	}

	@Override
	public void onCutsceneEnd()
	{
		Launcher.setProcessingProfile(Launcher.PROFILE_UNDEFINED);
		Persistance.frame.dispose();
	}

}
