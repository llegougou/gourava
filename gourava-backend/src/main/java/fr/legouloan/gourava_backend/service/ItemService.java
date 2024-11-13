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
        return limit > 0 ? itemRepository.findAll().stream().limit(limit).toList() : itemRepository.findAll();
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public Item updateItem(Long id, String title, List<Tag> tags, List<Criteria> criterias) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
    
        item.setTitle(title);
        itemRepository.save(item); 
    
        tagRepository.deleteAll(item.getTags()); 
        for (Tag tag : tags) {
            tag.setItem(item);
            tagRepository.save(tag);
        }
    
        criteriaRepository.deleteAll(item.getCriterias());  
        for (Criteria criteria : criterias) {
            criteria.setItem(item);
            criteriaRepository.save(criteria);  
        }
    
        return item; 
    }

    public List<Object[]> getTagUsageCount(int limit) {
        return tagRepository.findTagUsageCount(limit);
    }

    public List<Object[]> getTagUsageCountRandom(int limit){
        return tagRepository.findTagUsageCountRandom(limit);
    }

    public List<Object[]> getCriteriaUsageCount(int limit) {
        return criteriaRepository.findCriteriaUsageCount(limit);
    }

    public List<Object[]> getCriteriaUsageCountRandom(int limit) {
        return criteriaRepository.findCriteriaUsageCountRandom(limit);
    }
}