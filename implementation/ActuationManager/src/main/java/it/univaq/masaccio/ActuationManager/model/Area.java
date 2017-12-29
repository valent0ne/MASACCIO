package it.univaq.masaccio.ActuationManager.model;

import it.univaq.masaccio.ActuationManager.dao.data.DaoData;

public class Area {

    private String id;
    private String name;
    private String description;
    private String latitude;
    private String longitude;

    /**
     * Area constructor
     * @param d DaoData; it allows the objects of this class to be instantiated only by DaoData objects
     */
    public Area (DaoData d){
        id = null;
        name = null;
        description = null;
        latitude = null;
        longitude = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
