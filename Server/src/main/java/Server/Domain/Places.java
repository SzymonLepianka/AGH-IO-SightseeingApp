package Server.Domain;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "Places")
public class Places {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true)
    private Long ID;
    @NotNull
    @Column(unique = true)
    private String Name;
    private Boolean ValidPlace;
    private Float Latitude;
    private Float Longitude;
    private Long GoogleMapsID;
    private Long AccumulatedScore;
    private Long UsersVoted;
    private String Description;

    public Long getId() {
        return ID;
    }

    public void setId(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public Boolean getValidPlace() {
        return ValidPlace;
    }

    public void setValidPlace(Boolean ValidPlace) {
        this.ValidPlace = ValidPlace;
    }

    public Float getLatitude() {
        return Latitude;
    }

    public void setLatitude(Float Latitude) {
        this.Latitude = Latitude;
    }

    public Float getLongitude() {
        return Longitude;
    }

    public void setLongitude(Float Longitude) {
        this.Longitude = Longitude;
    }

    public Long getGoogleMapsID() {
        return GoogleMapsID;
    }

    public void setGoogleMapsID(Long GoogleMapsID) {
        this.GoogleMapsID = GoogleMapsID;
    }

    public Long getAccumulatedScore() {
        return AccumulatedScore;
    }

    public void setAccumulatedScore(Long AccumulatedScore) {
        this.AccumulatedScore = AccumulatedScore;
    }

    public Long getUsersVoted() {
        return UsersVoted;
    }

    public void setUsersVoted(Long UsersVoted) {
        this.UsersVoted = UsersVoted;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

}