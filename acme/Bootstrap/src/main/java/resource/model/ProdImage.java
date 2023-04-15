package resource.model;

import javax.annotation.Resource;
import javax.persistence.*;

@Entity
public class ProdImage {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Lob
    private Resource image;


    public ProdImage(Product product, Resource image) {
        setProduct(product);
        //addImage(image);;

    }


    public ProdImage() {

    }

    private void setProduct(Product product) {
    }


/*
    public ImageService addImage( Resource image){

        return new AddImage (this.image);
    }
*/
}

