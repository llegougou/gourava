package fr.legouloan.gourava_backend.dto;

import fr.legouloan.gourava_backend.model.Criteria;
import java.util.List;

public class AddItemDto {
    private String title;
    private List<String> tags;
    private List<Criteria> criteriaRatings;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Criteria> getCriteriaRatings() {
        return criteriaRatings;
    }

    public void setCriteriaRatings(List<Criteria> criteriaRatings) {
        this.criteriaRatings = criteriaRatings;
    }
}
