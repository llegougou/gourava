package fr.legouloan.gourava_backend.controller;

import fr.legouloan.gourava_backend.dto.ItemDto;
import fr.legouloan.gourava_backend.model.Item;
import fr.legouloan.gourava_backend.model.Tag;
import fr.legouloan.gourava_backend.model.Criteria;
import fr.legouloan.gourava_backend.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemService itemService;

    private MockMvc mockMvc;
    private Item testItem;
    private List<Tag> testTags;
    private List<Criteria> testCriterias;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();

        testItem = new Item();
        testItem.setId(1L);
        testItem.setTitle("Test Item");

        testTags = new ArrayList<>();
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Electronics");
        tag1.setItem(testItem);
        testTags.add(tag1);

        testCriterias = new ArrayList<>();
        Criteria criteria1 = new Criteria();
        criteria1.setId(1L);
        criteria1.setName("Quality");
        criteria1.setRating(5);
        criteria1.setItem(testItem);
        testCriterias.add(criteria1);
    }

    @Test
void testAddItem() throws Exception {
    ItemDto itemDto = new ItemDto();
    itemDto.setTitle("New Item");
    itemDto.setTags(testTags);
    itemDto.setCriterias(testCriterias);

    Item itemToReturn = new Item();
    itemToReturn.setId(1L);
    itemToReturn.setTitle("New Item");

    when(itemService.addItem(anyString(), any(), any())).thenReturn(itemToReturn);

    mockMvc.perform(post("/gouravaApi/addItem")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"New Item\",\"tags\":[{\"id\":1,\"name\":\"Electronics\"}],\"criterias\":[{\"id\":1,\"name\":\"Quality\",\"rating\":5}]}")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("New Item"))  
            .andExpect(jsonPath("$.id").value(1)); 

    verify(itemService, times(1)).addItem(anyString(), any(), any());
}

    @Test
    void testGetItemsWithLimit() throws Exception {
        List<Item> items = List.of(testItem);
        when(itemService.getItems(1)).thenReturn(items);

        mockMvc.perform(get("/gouravaApi/getItems")
                        .param("limit", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Item"));
        
        verify(itemService, times(1)).getItems(1);
    }

    @Test
    void testGetItemsWithoutLimit() throws Exception {
        List<Item> items = List.of(testItem);
        when(itemService.getItems(0)).thenReturn(items);

        mockMvc.perform(get("/gouravaApi/getItems")
                        .param("limit", "0")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Item"));
        
        verify(itemService, times(1)).getItems(0);
    }

    @Test
    void testDeleteItem() throws Exception {
        doNothing().when(itemService).deleteItem(anyLong());

        mockMvc.perform(delete("/gouravaApi/deleteItem/{id}", 1L))
                .andExpect(status().isOk());
        
        verify(itemService, times(1)).deleteItem(1L);
    }

    @Test
    void testUpdateItem() throws Exception {
        Item updatedItem = new Item();
        updatedItem.setId(1L);
        updatedItem.setTitle("Updated Item");

        when(itemService.updateItem(anyLong(), anyString(), any(), any())).thenReturn(updatedItem);

        ItemDto updateDto = new ItemDto();
        updateDto.setTitle("Updated Item");
        updateDto.setTags(testTags);
        updateDto.setCriterias(testCriterias);

        mockMvc.perform(put("/gouravaApi/updateItem/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Item\",\"tags\":[{\"id\":1,\"name\":\"Electronics\"}],\"criterias\":[{\"id\":1,\"name\":\"Quality\",\"rating\":5}]}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Item"));
        
        verify(itemService, times(1)).updateItem(anyLong(), anyString(), any(), any());
    }

    @Test
    void testGetTagsUsageCount() throws Exception {
        List<Object[]> usageCounts = List.of(new Object[]{"Electronics", 2L}, new Object[]{"Home Appliance", 1L});
        when(itemService.getTagUsageCount(0)).thenReturn(usageCounts);

        mockMvc.perform(get("/gouravaApi/tagsUsageCount")
                        .param("limit", "0")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0][0]").value("Electronics"))
                .andExpect(jsonPath("$[0][1]").value(2));
        
        verify(itemService, times(1)).getTagUsageCount(0);
    }
}
