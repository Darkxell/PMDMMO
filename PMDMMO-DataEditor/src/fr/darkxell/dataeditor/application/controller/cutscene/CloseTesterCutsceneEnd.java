package fr.darkxell.dataeditor.application.controller.cutscene;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;

public class CloseTesterCutsceneEnd extends CutsceneEnd
{

	public CloseTesterCutsceneEnd(Cutscene c)
	{
		super(c, (String) null);
	}

	@Override
	public void onCutsceneEnd()
	{
		Launcher.setProcessingProfile(Launcher.PROFILE_UNDEFINED);
		Persistance.soundmanager.setBackgroundMusic(null);
		Persistance.frame.dispose();
	}

	@Override
	protected String xmlName()
	{
		return "closetester";
	}

}
