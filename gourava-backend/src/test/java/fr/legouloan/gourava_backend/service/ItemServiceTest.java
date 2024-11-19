package fr.legouloan.gourava_backend.service;

import fr.legouloan.gourava_backend.model.Criteria;
import fr.legouloan.gourava_backend.model.Item;
import fr.legouloan.gourava_backend.model.Tag;
import fr.legouloan.gourava_backend.repository.ItemRepository;
import fr.legouloan.gourava_backend.repository.CriteriaRepository;
import fr.legouloan.gourava_backend.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CriteriaRepository criteriaRepository;

    @Mock
    private TagRepository tagRepository;

    private Item testItem;
    private List<Tag> testTags;
    private List<Criteria> testCriterias;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testItem = new Item();
        testItem.setId(1L);
        testItem.setTitle("Test Item");

        testTags = new ArrayList<>();
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Electronics");
        tag1.setItem(testItem);
        testTags.add(tag1);

        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("Home Appliance");
        tag2.setItem(testItem);
        testTags.add(tag2);

        testCriterias = new ArrayList<>();
        Criteria criteria1 = new Criteria();
        criteria1.setId(1L);
        criteria1.setName("Quality");
        criteria1.setRating(5);
        criteria1.setItem(testItem);
        testCriterias.add(criteria1);

        Criteria criteria2 = new Criteria();
        criteria2.setId(2L);
        criteria2.setName("Durability");
        criteria2.setRating(4);
        criteria2.setItem(testItem);
        testCriterias.add(criteria2);
    }

    @Test
    void testAddItem() {
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);

        Item createdItem = itemService.addItem("New Item", testTags, testCriterias);

        assertNotNull(createdItem);
        assertEquals("New Item", createdItem.getTitle());
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(tagRepository, times(2)).save(any(Tag.class));
        verify(criteriaRepository, times(2)).save(any(Criteria.class));
    }

    @Test
    void testGetItemsWithLimit() {
        List<Item> items = List.of(testItem);
        when(itemRepository.findAll()).thenReturn(items);

        List<Item> result = itemService.getItems(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void testGetItemsWithoutLimit() {
        List<Item> items = List.of(testItem);
        when(itemRepository.findAll()).thenReturn(items);

        List<Item> result = itemService.getItems(0);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void testDeleteItem() {
        doNothing().when(itemRepository).deleteById(anyLong());

        itemService.deleteItem(1L);

        verify(itemRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(testItem));
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);

        testItem.setTags(new ArrayList<>(testTags));  
        testItem.setCriterias(new ArrayList<>(testCriterias)); 

        Item updatedItem = itemService.updateItem(2L, "Updated Title", testTags, testCriterias);

        assertNotNull(updatedItem);
        assertEquals("Updated Title", updatedItem.getTitle());
        verify(itemRepository, times(1)).findById(2L);
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(tagRepository, times(2)).save(any(Tag.class));
        verify(criteriaRepository, times(2)).save(any(Criteria.class));
}
    
    @Test
    void testGetTagUsageCount() {
        List<Object[]> usageCounts = List.of(new Object[]{"Electronics", 2L}, new Object[]{"Home Appliance", 1L});
        when(tagRepository.findAllTagUsageCount()).thenReturn(usageCounts);

        List<Object[]> result = itemService.getTagUsageCount(0);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tagRepository, times(1)).findAllTagUsageCount();
    }

    @Test
    void testGetTagUsageCountWithLimit() {
        List<Object[]> usageCounts = List.of(new Object[]{"Quality", 2L}, new Object[]{"Durability", 1L});
        when(tagRepository.findLimitedTagUsageCount(1)).thenReturn(usageCounts);

        List<Object[]> result = itemService.getTagUsageCount(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tagRepository, times(1)).findLimitedTagUsageCount(1);
    }

    @Test
    void testGetCriteriaUsageCount() {
        List<Object[]> usageCounts = List.of(new Object[]{"Quality", 2L}, new Object[]{"Durability", 1L});
        when(criteriaRepository.findAllCriteriaUsageCount()).thenReturn(usageCounts);

        List<Object[]> result = itemService.getCriteriaUsageCount(0);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(criteriaRepository, times(1)).findAllCriteriaUsageCount();
    }

    @Test
    void testGetCriteriaUsageCountWithLimit() {
        List<Object[]> usageCounts = List.of(new Object[]{"Quality", 2L}, new Object[]{"Durability", 1L});
        when(criteriaRepository.findLimitedCriteriaUsageCount(1)).thenReturn(usageCounts);

        List<Object[]> result = itemService.getCriteriaUsageCount(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(criteriaRepository, times(1)).findLimitedCriteriaUsageCount(1);
    }
}
