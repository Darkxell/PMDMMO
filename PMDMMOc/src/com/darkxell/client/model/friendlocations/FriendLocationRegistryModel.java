package com.darkxell.client.model.friendlocations;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "locations")
@XmlAccessorType(XmlAccessType.FIELD)
public class FriendLocationRegistryModel {

    @XmlElement(name = "friendarea")
    private ArrayList<FriendLocationsModel> locations;

    public FriendLocationRegistryModel() {
    }

    public FriendLocationRegistryModel(ArrayList<FriendLocationsModel> locations) {
        this.locations = locations;
    }

    public ArrayList<FriendLocationsModel> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<FriendLocationsModel> locations) {
        this.locations = locations;
    }

    public FriendLocationRegistryModel copy() {
        ArrayList<FriendLocationsModel> loc = new ArrayList<>();
        this.locations.forEach(l -> loc.add(l.copy()));
        return new FriendLocationRegistryModel(loc);
    }

}
