package fr.darkxell.dataeditor.application.controller.cutscene;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.render.RenderProfile;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;

public class CloseTesterCutsceneEnd extends CutsceneEnd
{

	public CloseTesterCutsceneEnd(Cutscene c)
	{
		super(c, (String) null, false);
	}

	@Override
	public void onCutsceneEnd()
	{
<<<<<<< HEAD
		Launcher.setProcessingProfile(RenderProfile.PROFILE_UNDEFINED);
		Persistance.soundmanager.setBackgroundMusic(null);
		Persistance.frame.dispose();
=======
		Launcher.setProcessingProfile(Launcher.PROFILE_UNDEFINED);
		Persistence.soundmanager.setBackgroundMusic(null);
		Persistence.frame.dispose();
>>>>>>> bf4af816... PersistAnce refractor from chicken's branch
	}

	@Override
	protected String xmlName()
	{
		return "closetester";
	}

}
