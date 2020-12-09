package com.accessingmysql;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "RouteComments")
public class RouteComments {
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
    @JoinColumn(name = "RouteID", nullable = false)
    private Routes route;
    private String Content;

    public Long getId() {
        return  ID;
    }

    public void setId(Long ID) {
        this.ID = ID;
    }

    public Users getUser() {
        return user;
    }

    public Routes getRoute() {
        return route;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }
}
