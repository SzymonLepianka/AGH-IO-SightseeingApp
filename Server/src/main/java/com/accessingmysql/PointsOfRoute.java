package com.accessingmysql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "PointsOfRoute")
public class PointsOfRoute {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true)
    @OneToMany(mappedBy = "PointsOfRoute", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Long PointNumber;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Routes_ID", nullable = false)
    private Long RouteID;
    @JoinColumn(name = "Places_ID", nullable = false)
    private Long PlaceID;

    public Long getId() {
        return ID;
    }

    public void setId(Long ID) {
        this.ID = ID;
    }

    public String getRouteID() {
        return RouteID;
    }

    public String getPlaceID() {
        return PlaceID;
    }

}