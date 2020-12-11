package Server.Domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "PointsOfRoute")
public class PointsOfRoute {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true)
    private Long PointNumber;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "RouteID", nullable = false)
    private Routes route;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PlaceID", nullable = false)
    private Places place;

    public Long getPointNumber() {
        return PointNumber;
    }

    public Routes getRoute() {
        return route;
    }

    public Places getPlace() {
        return place;
    }

    public void setPointNumber(Long pointNumber) {
        PointNumber = pointNumber;
    }

    public void setRoute(Routes route) {
        this.route = route;
    }

    public void setPlace(Places place) {
        this.place = place;
    }
}