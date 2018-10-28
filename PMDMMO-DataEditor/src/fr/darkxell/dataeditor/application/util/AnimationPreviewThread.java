package fr.darkxell.dataeditor.application.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.launchable.UpdaterAndRenderer;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.state.mainstates.PrincipalMainState;

import fr.darkxell.dataeditor.application.controller.animation.TestAnimationController;
import javafx.embed.swing.SwingFXUtils;

public class AnimationPreviewThread extends UpdaterAndRenderer implements AnimationEndListener
{
	public int cooldown = 50;
	private BufferedImage image;

	@Override
	public void onAnimationEnd(AbstractAnimation animation)
	{
		this.cooldown = 50;
		TestAnimationController.instance.updateProgressBar(true);
	}

	@Override
	protected void tickUpdate()
	{
		TestAnimationController.state.update();
		if (this.cooldown > 0)
		{
			--this.cooldown;
			if (this.cooldown == 0) TestAnimationController.instance.playAnimation(false);
		} else TestAnimationController.instance.updateProgressBar(false);

		int width = PrincipalMainState.displayWidth, height = PrincipalMainState.displayHeight;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		// g.scale(16, 16);
		// g.translate(-width * 3.5 / 8, -height * 3.5 / 8);

		TestAnimationController.state.render(g, width, height);

		// g.translate(width * 3.5 / 8, height * 3.5 / 8);
		// g.scale(1. / 16, 1. / 16);
		g.dispose();

		TestAnimationController.instance.imageView.setImage(SwingFXUtils.toFXImage(image, null));
	}

}
