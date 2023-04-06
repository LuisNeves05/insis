package resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import resource.model.ProdImage;
import resource.model.Product;
import resource.repository.ImageRepository;
import resource.repository.ProductRepository;


@RestController
public class FileController {
    @Autowired
    private ImageRepository iRepo;

    @Autowired
    private ProductRepository pRepo;


    @GetMapping(value = "/fileid/{id}")
    public ResponseEntity<ProdImage> findById(@PathVariable("id") final Long id){

        final var prodImage = iRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found"));


        return  ResponseEntity.ok().body(prodImage);
    }

    @GetMapping(value = "/ID/{productID}")
    public ResponseEntity<Product> findByID(@PathVariable("productID") final Long productID){
        final var product = pRepo.findById(productID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));

        return ResponseEntity.ok().body(product);
    }




}