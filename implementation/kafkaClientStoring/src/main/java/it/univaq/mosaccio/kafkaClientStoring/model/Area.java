package it.univaq.mosaccio.kafkaClientStoring.model;

import it.univaq.mosaccio.kafkaClientStoring.dao.data.DaoData;
import org.apache.commons.dbcp2.BasicDataSource;

public class Area {

    private Integer id;
    private String description;
    private String latitude;
    private String longitude;

    /**
     * Area constructor
     * @param d DaoData; it allows the objects of this class to be instantiated only by DaoData objects
     */
    public Area (DaoData d){
        id = null;
        description = null;
        latitude = null;
        longitude = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
