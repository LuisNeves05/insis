package resource.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity

public class User {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long userId;

    @Column(unique = true)
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> review = new ArrayList<>();

    protected User() {
    }

    public User(final String username) {
        this.username = username;
    }


}

