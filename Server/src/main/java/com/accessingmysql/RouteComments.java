package com.accessingmysql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "RouteComments")
public class RouteComments {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true)
    @OneToMany(mappedBy = "RouteComments", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Users_ID", nullable = false)
    private Long UserID;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Routes_ID", nullable = false)
    private Long RouteID;
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

    public Long getRouteID() {
        return RouteID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }
}
