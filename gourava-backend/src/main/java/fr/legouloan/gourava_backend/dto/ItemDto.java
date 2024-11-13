package fr.legouloan.gourava_backend.dto;

import fr.legouloan.gourava_backend.model.Criteria;
import fr.legouloan.gourava_backend.model.Tag;
import java.util.List;

public class ItemDto {
    private String title;
    private List<Tag> tags;
    private List<Criteria> criterias;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Criteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<Criteria> criterias) {
        this.criterias = criterias;
    }
}
