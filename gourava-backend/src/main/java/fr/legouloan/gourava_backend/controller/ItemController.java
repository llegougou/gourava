package fr.legouloan.gourava_backend.controller;

import fr.legouloan.gourava_backend.dto.AddItemDto;
import fr.legouloan.gourava_backend.model.Criteria;
import fr.legouloan.gourava_backend.model.Item;
import fr.legouloan.gourava_backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/gouravaApi")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/addItem")
    public Item addItem(@RequestBody AddItemDto addItemDto) {
        return itemService.addItem(
                addItemDto.getTitle(),
                addItemDto.getTags(),
                addItemDto.getCriteriaRatings());
    }

    @GetMapping("/items")
    public List<Item> getItems(@RequestParam(value = "limit", defaultValue = "0") int limit) {
        return itemService.getItems(limit);
    }

    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }

    @PutMapping("/items/{id}")
    public Item updateItem(@PathVariable Long id, @RequestParam String title, @RequestParam List<String> tags, @RequestParam List<Criteria> criteriaRatings) {
        return itemService.updateItem(id, title, tags, criteriaRatings);
    }

    @GetMapping("/tagsUsageCount")
    public List<Object[]> getTagsUsageCount(@RequestParam(value = "limit", defaultValue = "0") int limit) {
        return itemService.getTagsUsageCount(limit);
    }

    @GetMapping("/criteriaUsageCount")
    public List<Object[]> getCriteriaUsageCount(@RequestParam(value = "limit", defaultValue = "0") int limit) {
        return itemService.getCriteriaUsageCount(limit);
    }
}
