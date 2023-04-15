package resource.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import resource.dto.CreateReviewDTO;
import resource.dto.ModerateReviewDTO;
import resource.dto.ReviewDTO;
import resource.dto.VoteReviewDTO;
import resource.service.ReviewService;
import resource.service.command_bus.ModerateReviewCommand;

import java.util.List;


@Tag(name = "Review", description = "Endpoints for managing Review")
@RestController
class ReviewController {

    @Autowired
    private ReviewService rService;

    @Operation(summary = "creates review")
    @PostMapping("/products/{sku}/reviews")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable(value = "sku") final String sku,
                                                  @RequestBody CreateReviewDTO createReviewDTO) {

        final var review = rService.create(createReviewDTO, sku);

        if(review == null){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @Operation(summary = "add vote")
    @PutMapping("/reviews/{reviewID}/vote")
    public ResponseEntity<Boolean> addVote(@PathVariable(value = "reviewID") final Long reviewID,
                                           @RequestBody VoteReviewDTO voteReviewDTO){

        boolean added = this.rService.addVoteToReview(reviewID, voteReviewDTO);

        if(!added){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<Boolean>(added, HttpStatus.CREATED);
    }

    @Operation(summary = "deletes review")
    @DeleteMapping("/reviews/{reviewID}")
    public ResponseEntity<Boolean> deleteReview(@PathVariable(value = "reviewID") final Long reviewID) {

        Boolean rev = rService.DeleteReview(reviewID);

        if (rev == null) return ResponseEntity.notFound().build();

        if (!rev) return ResponseEntity.unprocessableEntity().build();

        return ResponseEntity.ok().body(rev);
    }

    @Operation(summary = "Accept or reject review")
    @PutMapping("/reviews/acceptreject/{reviewID}")
    public ResponseEntity<ReviewDTO> putAcceptRejectReview(@PathVariable(value = "reviewID") final Long reviewID,
                                                           @RequestBody ModerateReviewDTO moderateReviewDTO){

        try {
            ReviewDTO rev = rService.moderateReview(reviewID, moderateReviewDTO.getApproved());

            return ResponseEntity.ok().body(rev);
        }
        catch( IllegalArgumentException e ) {
            return ResponseEntity.badRequest().build();
        }
        catch( ResourceNotFoundException e ) {
            return ResponseEntity.notFound().build();
        }
    }
}
