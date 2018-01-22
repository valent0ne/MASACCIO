package it.univaq.masaccio.rest.business.domain;

import javax.persistence.*;

public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "max_bound", nullable = false)
    private String maxBound;

    @Column(name = "min_bound", nullable = false)
    private String minBound;

    @Column(name = "latitude", nullable = false)
    private String latitude;

    @Column(name = "longitude", nullable = false)
    private String longitude;

    @Column(name = "elevation", nullable = false)
    private String elevation;

    @ManyToOne
    @JoinColumn(name = "area", nullable = false)
    private Area area;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaxBound() {
        return maxBound;
    }

    public void setMaxBound(String maxBound) {
        this.maxBound = maxBound;
    }

    public String getMinBound() {
        return minBound;
    }

    public void setMinBound(String minBound) {
        this.minBound = minBound;
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

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }
}
