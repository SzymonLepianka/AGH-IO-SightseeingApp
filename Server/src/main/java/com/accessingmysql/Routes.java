package com.accessingmysql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "Routes")
public class Routes {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true)
    @OneToMany(mappedBy = "Routes", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Long ID;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Users_ID", nullable = false)
    private Long UserID;
    private Long AccumulatedScore;
    private Long UsersVoted;
    private Boolean Public;

    public Long getId() {
        return ID;
    }

    public void setId(Long ID) {
        this.ID = ID;
    }

    public Long getUserID() {
        return UserID;
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
    public Boolean getPublic() {
        return Public;
    }

    public void setPublic(Boolean) {
        this.Public = Public;
    }
}