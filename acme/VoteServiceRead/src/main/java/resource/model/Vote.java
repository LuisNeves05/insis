package resource.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Vote {
    @Id
    @GeneratedValue
    private Long voteId;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private Long reviewId;


    protected Vote() {

    }
    public Vote(Long voteId, String username, String status, Long reviewId) {
        this.voteId = voteId;
        this.username = username;
        this.status = status;
        this.reviewId = reviewId;
    }

    public Vote(String username, String status, Long reviewId) {
        this.username = username;
        this.status = status;
        this.reviewId = reviewId;
    }
}
