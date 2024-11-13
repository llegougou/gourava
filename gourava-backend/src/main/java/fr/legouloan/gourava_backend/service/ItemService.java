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
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CriteriaRepository criteriaRepository;

    public Item addItem(String title, List<String> tags, List<Criteria> criteriaRatings) {
        Item item = new Item();
        item.setTitle(title);
        
        itemRepository.save(item);
    
        for (String tag : tags) {
            Tag newTag = new Tag();
            newTag.setTag(tag);
            newTag.setItem(item);
            tagRepository.save(newTag);
        }
    
        for (Criteria criteria : criteriaRatings) {
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

    public Item updateItem(Long id, String title, List<String> tags, List<Criteria> criteriaRatings) {
        Optional<Item> itemOpt = itemRepository.findById(id);
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();
            item.setTitle(title);
            itemRepository.save(item);

            tagRepository.deleteAll(item.getTags());
            for (String tag : tags) {
                Tag newTag = new Tag();
                newTag.setTag(tag);
                newTag.setItem(item);
                tagRepository.save(newTag);
            }

            criteriaRepository.deleteAll(item.getCriteriaRatings());
            for (Criteria criteria : criteriaRatings) {
                criteria.setItem(item);
                criteriaRepository.save(criteria);
            }
            return item;
        } else {
            return null;
        }
    }

    public List<Object[]> getTagsUsageCount(int limit) {
        return tagRepository.findTagsUsageCount(limit);
    }

    public List<Object[]> getCriteriaUsageCount(int limit) {
        return criteriaRepository.findCriteriaUsageCount(limit);
    }
}