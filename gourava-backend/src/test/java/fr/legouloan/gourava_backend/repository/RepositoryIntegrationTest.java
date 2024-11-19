package fr.legouloan.gourava_backend.repository;

import fr.legouloan.gourava_backend.model.Criteria;
import fr.legouloan.gourava_backend.model.Item;
import fr.legouloan.gourava_backend.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class RepositoryIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        Item item = new Item();
        item.setTitle("Test Item");
        item = itemRepository.save(item);

        Criteria criteria1 = new Criteria();
        criteria1.setName("Quality");
        criteria1.setRating(5);
        criteria1.setItem(item);

        Criteria criteria2 = new Criteria();
        criteria2.setName("Durability");
        criteria2.setRating(4);
        criteria2.setItem(item);

        Criteria criteria3 = new Criteria();
        criteria3.setName("Quality");
        criteria3.setRating(3);
        criteria3.setItem(item);

        criteriaRepository.save(criteria1);
        criteriaRepository.save(criteria2);
        criteriaRepository.save(criteria3);

        Tag tag1 = new Tag();
        tag1.setName("Electronics");
        tag1.setItem(item);

        Tag tag2 = new Tag();
        tag2.setName("Home Appliance");
        tag2.setItem(item);

        Tag tag3 = new Tag();
        tag3.setName("Electronics");
        tag3.setItem(item);

        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);
    }

    @Test
    void testItemRepository() {
        List<Item> items = itemRepository.findAll();
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals("Test Item", items.get(0).getTitle());
    }

    @Test
    void testCriteriaRepository() {
        List<Object[]> randomCriteriaUsage = criteriaRepository.findAllCriteriaUsageCountRandom();
        assertNotNull(randomCriteriaUsage);
        assertTrue(randomCriteriaUsage.size() <= 2);

        int limit = 1;
        List<Object[]> limitedRandomCriteriaUsage = criteriaRepository.findLimitedCriteriaUsageCountRandom(limit);
        assertNotNull(limitedRandomCriteriaUsage);
        assertEquals(limit, limitedRandomCriteriaUsage.size());

        List<Object[]> criteriaUsage = criteriaRepository.findAllCriteriaUsageCount();
        assertNotNull(criteriaUsage);
        assertEquals(2, criteriaUsage.size());
        assertEquals("Quality", criteriaUsage.get(0)[0]);
        assertEquals(2L, criteriaUsage.get(0)[1]);

        List<Object[]> limitedCriteriaUsage = criteriaRepository.findLimitedCriteriaUsageCount(limit);
        assertNotNull(limitedCriteriaUsage);
        assertEquals(limit, limitedCriteriaUsage.size());
        assertEquals("Quality", limitedCriteriaUsage.get(0)[0]);
        assertEquals(2L, limitedCriteriaUsage.get(0)[1]);
    }

    @Test
    void testTagRepository() {
        List<Object[]> randomTagUsage = tagRepository.findAllTagUsageCountRandom();
        assertNotNull(randomTagUsage);
        assertTrue(randomTagUsage.size() <= 2);

        int limit = 1;
        List<Object[]> limitedRandomTagUsage = tagRepository.findLimitedTagUsageCountRandom(limit);
        assertNotNull(limitedRandomTagUsage);
        assertEquals(limit, limitedRandomTagUsage.size());

        List<Object[]> tagUsage = tagRepository.findAllTagUsageCount();
        assertNotNull(tagUsage);
        assertEquals(2, tagUsage.size());
        assertEquals("Electronics", tagUsage.get(0)[0]);
        assertEquals(2L, tagUsage.get(0)[1]);

        List<Object[]> limitedTagUsage = tagRepository.findLimitedTagUsageCount(limit);
        assertNotNull(limitedTagUsage);
        assertEquals(limit, limitedTagUsage.size());
        assertEquals("Electronics", limitedTagUsage.get(0)[0]);
        assertEquals(2L, limitedTagUsage.get(0)[1]);
    }
}