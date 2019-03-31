package com.darkxell.common.zones;

interface FriendAreaAcquisition {

    /** This Friend Area is unlocked automatically when player reaches a story position. */
    public class AutoStoryFriendArea implements FriendAreaAcquisition {

        public final int storypos;

        public AutoStoryFriendArea(int storypos) {
            this.storypos = storypos;
        }
    }

    public enum BaseFriendAreaAcquisition implements FriendAreaAcquisition {
        /** This Friend Area isn't required to recruit the Pokemon, and will be unlocked on recruiting that Pokemon. */
        ON_RECRUIT,
        /** This Friend Area isn't acquired by regular means. Usually a reward for an event or mission. */
        OTHER;
    }

    /** This Friend area can be bought from the minStorypos story position and for a certain price, once Wigglytuff is enabled. */
    public class BuyableFriendArea implements FriendAreaAcquisition {

        public final int minStorypos;
        public final int price;

        public BuyableFriendArea(int price) {
            this(0, price);
        }

        public BuyableFriendArea(int minStorypos, int price) {
            this.minStorypos = minStorypos;
            this.price = price;
        }
    }

}
