package resource.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import resource.dto.VoteDTO;
import resource.dto.VoteDTOResponse;
import resource.service.VoteService;


@Tag(name = "Vote", description = "Endpoints for managing Votes")
@RestController
class VoteController {

    @Autowired
    private VoteService rService;

    @Operation(summary = "creates vote")
    @PostMapping("/create")
    public ResponseEntity<VoteDTOResponse> createVote(@RequestBody VoteDTO voteDTO) {

        final var vote = rService.create(voteDTO);

        if(vote == null){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(vote, HttpStatus.CREATED);
    }

    @Operation(summary = "deletes vote")
    @DeleteMapping("/delete/{voteID}")
    public ResponseEntity<Boolean> deleteVote(@PathVariable(value = "voteID") final Long voteID) {

        Boolean rev = rService.deleteVote(voteID);

        if (rev == null) return ResponseEntity.notFound().build();

        if (!rev) return ResponseEntity.unprocessableEntity().build();

        return ResponseEntity.ok().body(rev);
    }
}
