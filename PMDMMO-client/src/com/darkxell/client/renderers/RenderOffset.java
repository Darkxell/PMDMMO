package com.darkxell.client.renderers;

public interface RenderOffset {

    public static class SimpleRenderOffset implements RenderOffset {
        public final double x, y;

        public SimpleRenderOffset(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double xOffset() {
            return this.x;
        }

        @Override
        public double yOffset() {
            return this.y;
        }
    }

    public double xOffset();

    public double yOffset();

}
