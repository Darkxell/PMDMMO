package fr.darkxell.dataeditor.application.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;

import com.darkxell.client.launchable.render.UpdaterAndRenderer;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import fr.darkxell.dataeditor.application.controller.animation.TestAnimationController;

public class AnimationPreviewThread extends UpdaterAndRenderer implements AnimationEndListener {
    public TestAnimationController controller;
    public int cooldown = 50;
    private BufferedImage image;
    private boolean shouldStop = false;

    public AnimationPreviewThread(TestAnimationController controller) {
        this.controller = controller;
    }

    public void exit() {
        this.shouldStop = true;
    }

    @Override
    protected boolean keepRunning() {
        return super.keepRunning() && !this.shouldStop;
    }

    @Override
    public void onAnimationEnd(AbstractAnimation animation) {
        this.cooldown = 50;
        this.controller.updateProgressBar(true);
    }

    @Override
    protected void tick() {
        TestAnimationController.state.update();
        if (this.cooldown > 0) {
            --this.cooldown;
            if (this.cooldown == 0)
                this.controller.playAnimation(false);
        } else
            this.controller.updateProgressBar(false);

        int width = PrincipalMainState.displayWidth / 2, height = PrincipalMainState.displayHeight / 2;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        // g.scale(16, 16);
        // g.translate(-width * 3.5 / 8, -height * 3.5 / 8);

        TestAnimationController.state.render(g, width, height);

        // g.translate(width * 3.5 / 8, height * 3.5 / 8);
        // g.scale(1. / 16, 1. / 16);
        g.dispose();

        this.controller.imageView.setImage(SwingFXUtils.toFXImage(image, null));
    }

}
