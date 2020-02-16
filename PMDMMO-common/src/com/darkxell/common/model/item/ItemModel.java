package com.darkxell.common.model.item;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.common.util.language.StringUtil;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemModel {

    /** This Item's ID. */
    @XmlAttribute
    private int id;

    /** This Item's Category. */
    @XmlAttribute
    private ItemCategory category;

    /** This Item's effect ID. */
    @XmlAttribute(name = "effect")
    private int effectID;

    /** This Item's price to buy. */
    @XmlAttribute
    private int price;

    /** This Item's price to sell. */
    @XmlAttribute
    private int sell;

    /** The ID of the Item's sprite. */
    @XmlAttribute(name = "sprite")
    private int spriteID;

    /** True if this Item can be stacked. */
    @XmlAttribute(name = "stackable")
    private Boolean isStackable;

    /** True if this Item is Rare, and thus can't be trashed, sold or thrown. */
    @XmlAttribute(name = "rare")
    private Boolean isRare;

    /** Extra data for the item. */
    @XmlAttribute
    private String extra;

    public ItemModel() {
    }

    public ItemModel(int id, ItemCategory category, int price, int sell, int effectID, int spriteID, Boolean stackable,
            Boolean rare, String extra) {
        this.id = id;
        this.category = category;
        this.price = price;
        this.sell = sell;
        this.spriteID = spriteID;
        this.effectID = effectID;
        this.isStackable = stackable;
        this.isRare = rare;
        this.extra = extra;
    }

    public ItemModel copy() {
        return new ItemModel(id, category, price, sell, effectID, spriteID, isStackable, isRare, extra);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemModel)) {
            return false;
        }
        ItemModel o = (ItemModel) obj;
        return this.category == o.category && this.effectID == o.effectID && this.id == o.id
                && this.isRare.equals(o.isRare) && this.isStackable.equals(o.isStackable) && this.price == o.price
                && this.sell == o.sell && this.spriteID == o.spriteID && StringUtil.equals(this.extra, o.extra);
    }

    public ItemCategory getCategory() {
        return category;
    }

    public int getEffectID() {
        return effectID;
    }

    public String getExtra() {
        return extra;
    }

    public int getID() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getSell() {
        return sell;
    }

    public int getSpriteID() {
        return spriteID;
    }

    public Boolean isRare() {
        return isRare;
    }

    public Boolean isStackable() {
        return isStackable;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public void setEffectID(int effectID) {
        this.effectID = effectID;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setRare(Boolean isRare) {
        this.isRare = isRare;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public void setSpriteID(int spriteID) {
        this.spriteID = spriteID;
    }

    public void setStackable(Boolean isStackable) {
        this.isStackable = isStackable;
    }

}
