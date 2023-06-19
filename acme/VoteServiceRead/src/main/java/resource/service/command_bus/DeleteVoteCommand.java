package resource.service.command_bus;


import java.io.Serializable;

public record DeleteVoteCommand(Long voteId) implements Serializable {


}
