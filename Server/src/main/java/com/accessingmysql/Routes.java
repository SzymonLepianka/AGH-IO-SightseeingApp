package com.accessingmysql;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "Routes")
public class Routes {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true)
    private Long ID;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UsersID", nullable = false)
    private Users user;
    private Long AccumulatedScore;
    private Long UsersVoted;
    private Boolean Public;

    public Long getID() {
        return ID;
    }

    public void setId(Long ID) {
        this.ID = ID;
    }

    public Users getUser() {
        return user;
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

    public void setPublic(Boolean Public) {
        this.Public = Public;
    }
}