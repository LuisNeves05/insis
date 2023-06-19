package resource.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class UserR {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long userId;

    @Column(unique = true)
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> review = new ArrayList<>();

    protected UserR() {
    }

    public UserR(final String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserR{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}

