package com.darkxell.client.model.friendlocations;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.darkxell.common.util.XMLUtils.FreezoneInfoAdapter;
import com.darkxell.common.zones.FreezoneInfo;

@XmlRootElement(name = "friendarea")
@XmlAccessorType(XmlAccessType.FIELD)
public class FriendLocationsModel {

    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(FreezoneInfoAdapter.class)
    private FreezoneInfo targetFreezone;

    @XmlElement(name = "friend")
    private ArrayList<FriendLocationModel> locations;

    public FriendLocationsModel() {
    }

    public FriendLocationsModel(FreezoneInfo targetFreezone, ArrayList<FriendLocationModel> locations) {
        this.targetFreezone = targetFreezone;
        this.locations = locations;
    }

    public ArrayList<FriendLocationModel> getLocations() {
        return locations;
    }

    public FreezoneInfo getTargetFreezone() {
        return targetFreezone;
    }

    public void setLocations(ArrayList<FriendLocationModel> locations) {
        this.locations = locations;
    }

    public void setTargetFreezone(FreezoneInfo targetFreezone) {
        this.targetFreezone = targetFreezone;
    }

    public FriendLocationsModel copy() {
        ArrayList<FriendLocationModel> loc = new ArrayList<>();
        this.locations.forEach(l -> loc.add(l.copy()));
        return new FriendLocationsModel(this.targetFreezone, loc);
    }

    public FriendLocationModel findLocation(int id, boolean shiny) {
        for (FriendLocationModel location : this.locations) {
            if (location.getSpecies() == id && location.getShiny().equals(shiny))
                return location;
        }
        return null;
    }

}
