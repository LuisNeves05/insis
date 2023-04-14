package resource.dto;

public class ModerateReviewDTO {

    private Long reviewId;
    private String approved;

    public ModerateReviewDTO(Long reviewId, String approved) {
        this.reviewId = reviewId;
        this.approved = approved;
    }

    public ModerateReviewDTO() {
        // default constructor
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public String getApproved() {
        return approved;
    }
}
