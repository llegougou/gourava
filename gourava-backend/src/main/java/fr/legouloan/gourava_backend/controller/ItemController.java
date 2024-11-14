package fr.legouloan.gourava_backend.controller;

import fr.legouloan.gourava_backend.dto.ItemDto;
import fr.legouloan.gourava_backend.model.Item;
import fr.legouloan.gourava_backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/gouravaApi")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/addItem")
    public Item addItem(@RequestBody ItemDto addItemDto) {
        return itemService.addItem(
                addItemDto.getTitle(),
                addItemDto.getTags(),
                addItemDto.getCriterias());
    }

    @GetMapping("/getItems")
    public List<Item> getItems(@RequestParam(value = "limit", defaultValue = "0") int limit) {
        return itemService.getItems(limit);
    }

    @DeleteMapping("/deleteItem/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }

    @PutMapping("/updateItem/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody ItemDto updateItemDto) {
        return itemService.updateItem(
                id,
                updateItemDto.getTitle(),
                updateItemDto.getTags(),
                updateItemDto.getCriterias());
    }

    @GetMapping("/tagsUsageCount")
    public List<Object[]> getTagsUsageCount(@RequestParam(value = "limit", defaultValue = "0") int limit) {
        return itemService.getTagUsageCount(limit);
    }

    @GetMapping("/tagsUsageCountRandom")
    public List<Object[]> getTagsUsageCountRandom(@RequestParam(value = "limit", defaultValue = "0") int limit) {
        return itemService.getTagUsageCountRandom(limit);
    }

    @GetMapping("/criteriasUsageCount")
    public List<Object[]> getCriteriaUsageCount(@RequestParam(value = "limit", defaultValue = "0") int limit) {
        return itemService.getCriteriaUsageCount(limit);
    }

    @GetMapping("/criteriasUsageCountRandom")
    public List<Object[]> getCriteriasUsageCountRandom(@RequestParam(value = "limit", defaultValue = "0") int limit) {
        return itemService.getCriteriaUsageCountRandom(limit);
    }
}
