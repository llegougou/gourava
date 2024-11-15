package fr.legouloan.gourava_backend.service;

import fr.legouloan.gourava_backend.model.Criteria;
import fr.legouloan.gourava_backend.model.Item;
import fr.legouloan.gourava_backend.model.Tag;
import fr.legouloan.gourava_backend.repository.ItemRepository;
import fr.legouloan.gourava_backend.repository.TagRepository;
import fr.legouloan.gourava_backend.repository.CriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collections;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CriteriaRepository criteriaRepository;

    public Item addItem(String title, List<Tag> tags, List<Criteria> criterias) {
        Item item = new Item();
        item.setTitle(title);

        itemRepository.save(item);

        for (Tag tag : tags) {
            tag.setItem(item);
            tagRepository.save(tag);
        }

        for (Criteria criteria : criterias) {
            criteria.setItem(item);
            criteriaRepository.save(criteria);
        }
        return item;
    }

    public List<Item> getItems(int limit) {
        List<Item> items = itemRepository.findAll();
    
        if (limit > 0) {
            Collections.shuffle(items); 
            return items.stream().limit(limit).toList();
        }
        
        
        return items;
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public Item updateItem(Long id, String title, List<Tag> tags, List<Criteria> criterias) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));

        item.setTitle(title);
        item.getTags().clear();
        item.getCriterias().clear();
        itemRepository.save(item);

        for (Tag tag : tags) {
            tag.setItem(item);
            tagRepository.save(tag);
        }
    
        for (Criteria criteria : criterias) {
            criteria.setItem(item);
            criteriaRepository.save(criteria);  
        }
    
        return item; 
    }

    public List<Object[]> getTagUsageCount(int limit) {
        if (limit == 0) {
            return tagRepository.findAllTagUsageCount();
        } else {
            return tagRepository.findLimitedTagUsageCount(limit);
        }
    }

    public List<Object[]> getTagUsageCountRandom(int limit){
        if (limit == 0) {
            return tagRepository.findAllTagUsageCountRandom();
        } else {
            return tagRepository.findLimitedTagUsageCountRandom(limit);
        }
    }

    public List<Object[]> getCriteriaUsageCount(int limit) {
        if (limit == 0) {
            return criteriaRepository.findAllCriteriaUsageCount();
        } else {
            return criteriaRepository.findLimitedCriteriaUsageCount(limit);
        }
    }

    public List<Object[]> getCriteriaUsageCountRandom(int limit) {
        if (limit == 0) {
            return criteriaRepository.findAllCriteriaUsageCountRandom();
        } else {
            return criteriaRepository.findLimitedCriteriaUsageCountRandom(limit);
        }
    }
}