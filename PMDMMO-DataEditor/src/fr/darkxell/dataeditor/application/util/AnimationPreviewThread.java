package fr.darkxell.dataeditor.application.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.launchable.UpdaterAndRenderer;
import com.darkxell.client.state.mainstates.PrincipalMainState;

import fr.darkxell.dataeditor.application.controller.animation.EditAnimationController;
import javafx.embed.swing.SwingFXUtils;

public class AnimationPreviewThread extends UpdaterAndRenderer
{
	private BufferedImage image;

	@Override
	protected void tickUpdate()
	{
		EditAnimationController.state.update();

		int width = PrincipalMainState.displayWidth, height = PrincipalMainState.displayHeight;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		// g.scale(16, 16);
		// g.translate(-width * 3.5 / 8, -height * 3.5 / 8);

		EditAnimationController.state.render(g, width, height);

		// g.translate(width * 3.5 / 8, height * 3.5 / 8);
		// g.scale(1. / 16, 1. / 16);
		g.dispose();

		EditAnimationController.instance.imageView.setImage(SwingFXUtils.toFXImage(image, null));
	}

}
