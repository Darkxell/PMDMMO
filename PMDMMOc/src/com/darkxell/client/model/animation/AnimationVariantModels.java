package com.darkxell.client.model.animation;

import javax.xml.bind.annotation.XmlRootElement;

public class AnimationVariantModels {

    @XmlRootElement(name = "default")
    public static class DefaultVariant extends AnimationVariantModel {
        @Override
        protected AnimationVariantModel createCopy() {
            return new DefaultVariant();
        }
    }

    @XmlRootElement(name = "north")
    public static class NorthVariant extends AnimationVariantModel {
        @Override
        protected AnimationVariantModel createCopy() {
            return new NorthVariant();
        }
    }

    @XmlRootElement(name = "northeast")
    public static class NorthEastVariant extends AnimationVariantModel {
        @Override
        protected AnimationVariantModel createCopy() {
            return new NorthEastVariant();
        }
    }

    @XmlRootElement(name = "east")
    public static class EastVariant extends AnimationVariantModel {
        @Override
        protected AnimationVariantModel createCopy() {
            return new EastVariant();
        }
    }

    @XmlRootElement(name = "southeast")
    public static class SouthEastVariant extends AnimationVariantModel {
        @Override
        protected AnimationVariantModel createCopy() {
            return new SouthEastVariant();
        }
    }

    @XmlRootElement(name = "south")
    public static class SouthVariant extends AnimationVariantModel {
        @Override
        protected AnimationVariantModel createCopy() {
            return new SouthVariant();
        }
    }

    @XmlRootElement(name = "southwest")
    public static class SouthWestVariant extends AnimationVariantModel {
        @Override
        protected AnimationVariantModel createCopy() {
            return new SouthWestVariant();
        }
    }

    @XmlRootElement(name = "west")
    public static class WestVariant extends AnimationVariantModel {
        @Override
        protected AnimationVariantModel createCopy() {
            return new WestVariant();
        }
    }

    @XmlRootElement(name = "northwest")
    public static class NorthWestVariant extends AnimationVariantModel {
        @Override
        protected AnimationVariantModel createCopy() {
            return new NorthWestVariant();
        }
    }

}
