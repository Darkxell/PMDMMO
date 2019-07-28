package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.SpritesetAnimation;
import com.darkxell.client.mechanics.animation.travel.ArcAnimation;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;
import com.darkxell.client.resources.image.dungeon.AbstractDungeonTileset;
import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.floor.Tile;

public class ProjectileAnimationState extends AnimationState {
    public enum ProjectileMovement {
        ARC,
        STRAIGHT
    }

    public static final double speed = 0.2;

    private int duration;
    public ProjectileMovement movement;
    public boolean shouldBounce = false;
    public final Tile start, end;
    private int tick = 0;
    private TravelAnimation travel;

    public ProjectileAnimationState(DungeonState parent, Tile start, Tile end) {
        super(parent);
        this.start = start;
        this.end = end;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.travel = new TravelAnimation(this.start, this.end);
        this.duration = (int) Math.floor(this.travel.distance() / speed);

        if (this.movement == ProjectileMovement.ARC) {
            this.duration *= 1.5;
            this.travel = new ArcAnimation(this.start, this.end);
        }
    }

    @Override
    public void prerender(Graphics2D g, int width, int height) {
        super.prerender(g, width, height);
        this.animation.render(g, width, height);
    }

    @Override
    public void update() {
        super.update();
        ++this.tick;
        this.travel.update(this.tick * 1d / this.duration);
        ((SpritesetAnimation) this.animation).setXY(this.travel.current().getX() * AbstractDungeonTileset.TILE_SIZE,
                this.travel.current().getY() * AbstractDungeonTileset.TILE_SIZE);
        if (this.tick == this.duration)
            if (this.shouldBounce) {
                this.shouldBounce = false;
                this.travel = new ArcAnimation(this.end,
                        this.end.adjacentTile(AIUtils.generalDirection(this.end, this.start)));
                this.tick = 0;
                this.duration = 15;
            } else {
                Persistence.dungeonState.setDefaultState();
                this.animation.stop();
            }
    }

}
