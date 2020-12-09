package com.accessingmysql;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "PlacesComments")
public class PlacesComments {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true)
    private Long ID;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UsersID", nullable = false)
    private Users user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PlaceID", nullable = false)
    private Places place;
    private String Content;

    public Long getID() {
        return ID;
    }

    public Users getUser() {
        return user;
    }

    public Places getPlace() {
        return place;
    }

    public String getContent() {
        return Content;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setPlace(Places place) {
        this.place = place;
    }

    public void setContent(String content) {
        Content = content;
    }
}