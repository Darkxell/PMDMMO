package com.darkxell.common.model.item;

public enum ItemCategory {
    BERRIES(4),
    DRINKS(5),
    EQUIPABLE(1),
    EVOLUTIONARY(12),
    FOOD(3),
    GUMMIS(6),
    HMS(11),
    KEY_ITEM(0),
    ORBS(9),
    OTHER_USABLES(8),
    OTHERS(13),
    SEEDS(7),
    THROWABLE(2),
    TMS(10);

    public final int order;

    ItemCategory(int order) {
        this.order = order;
    }
}