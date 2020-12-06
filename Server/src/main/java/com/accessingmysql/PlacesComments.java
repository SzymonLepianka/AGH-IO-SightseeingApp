package com.accessingmysql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "PlacesComments")
public class PlacesComments {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true)
    @OneToMany(mappedBy = "PlacesComments", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Long ID;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Users_ID", nullable = false)
    private Long UserID;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Places_ID", nullable = false)
    private Long PlaceID;
    private String Content;

    public Long getId() {
        return ID;
    }

    public void setId(Long ID) {
        this.ID = ID;
    }

    public Long getUserID() {
        return UserID;
    }

    public Long getPlaceID() {
        return PlaceID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }
}