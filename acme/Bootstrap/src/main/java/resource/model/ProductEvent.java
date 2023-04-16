package resource.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class ProductEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private byte[] event;

    @Column(nullable = false, unique = true)
    private Long seq;

    protected ProductEvent() {
    }

    public ProductEvent(final byte[] event) {
        this.event = event;
    }

    public ProductEvent(final byte[] event, Long seq) {
        this.event = event;
        this.seq = seq;
    }

}
