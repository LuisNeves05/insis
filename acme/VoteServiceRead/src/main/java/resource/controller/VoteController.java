package resource.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import resource.dto.VoteDTOResponse;
import resource.service.VoteService;

import java.util.List;


@Tag(name = "Vote", description = "Endpoints for managing Votes")
@RestController
class VoteController {

    @Autowired
    private VoteService rService;

    @Operation(summary = "gets all votes")
    @GetMapping("/votes")
    public ResponseEntity<List<VoteDTOResponse>> getAllReviews(){

        List<VoteDTOResponse> r = rService.findAllVotes();

        return ResponseEntity.ok().body(r);
    }
}
