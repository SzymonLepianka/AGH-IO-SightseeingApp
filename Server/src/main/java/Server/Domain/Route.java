package Server.Domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "Routes")
public class Route {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true, name = "route_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Long accumulatedScore;
    private Long usersVoted;
    private Boolean isPublic;

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PointOfRoute> pointsOfRoutes;

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RouteComment> routeComments;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getAccumulatedScore() {
        return accumulatedScore;
    }

    public void setAccumulatedScore(Long accumulatedScore) {
        this.accumulatedScore = accumulatedScore;
    }

    public Long getUsersVoted() {
        return usersVoted;
    }

    public void setUsersVoted(Long usersVoted) {
        this.usersVoted = usersVoted;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Set<PointOfRoute> getPointsOfRoutes() {
        return pointsOfRoutes;
    }

    public void setPointsOfRoutes(Set<PointOfRoute> pointsOfRoutes) {
        this.pointsOfRoutes = pointsOfRoutes;
    }

    public Set<RouteComment> getRouteComments() {
        return routeComments;
    }

    public void setRouteComments(Set<RouteComment> routeComments) {
        this.routeComments = routeComments;
    }
}