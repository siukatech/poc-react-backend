package com.siukatech.poc.react.backend.app.item.web;

import com.siukatech.poc.react.backend.app.item.business.dto.ItemDto;
import com.siukatech.poc.react.backend.app.item.business.form.ItemForm;
import com.siukatech.poc.react.backend.app.item.business.service.ItemService;
import com.siukatech.poc.react.backend.app.item.web.controller.ItemController;
import com.siukatech.poc.react.backend.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.core.AbstractWebTests;
import com.siukatech.poc.react.backend.core.web.annotation.v1.ProtectedApiV1Controller;
import com.siukatech.poc.react.backend.core.web.micrometer.CorrelationIdHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = {ItemController.class})
@AutoConfigureMockMvc(addFilters = false)
public class ItemControllerTests extends AbstractWebTests {

//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;

////    @MockBean
//    private ModelMapper modelMapper;

    @MockBean
    private ItemService itemService;
//    @MockBean
//    private CorrelationIdHandler correlationIdHandler;


    @BeforeAll
    public static void init() {
        AbstractUnitTests.init();
    }

    @BeforeEach
    public void setup(TestInfo testInfo) {
    }

    @AfterEach
    public void teardown(TestInfo testInfo) {
    }

    private ItemForm prepareItemForm() {
        ItemForm itemForm = new ItemForm();
        itemForm.setId(UUID.randomUUID().toString());
        itemForm.setName("shf figure 2");
        itemForm.setPurchasedDate(LocalDate.now());
        itemForm.setVersionNo(1L);
        return itemForm;
    }

    private ItemDto convertToItemDto(ItemForm itemForm) {
        ModelMapper mapper = new ModelMapper();
        ItemDto itemDto = mapper.map(itemForm, ItemDto.class);
        return itemDto;
    }

    @Test
    public void listItems_basic() throws Exception {
        // given
        ItemDto itemDto = this.convertToItemDto(this.prepareItemForm());
        when(itemService.findItemAll()).thenReturn(List.of(itemDto));

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/items")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        // then
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("shf figure")))
                .andReturn();

    }

    @Test
    public void createItem_basic() throws Exception {
        // given
        ItemForm itemForm = this.prepareItemForm();
        String itemFormStr = this.objectMapper.writeValueAsString(itemForm);
        log.debug("createItem_basic - itemFormStr: [{}]", itemFormStr);
        when(this.itemService.createItem(itemForm)).thenReturn(this.convertToItemDto(itemForm));

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/items")
                .with(csrf())
                .content(itemFormStr)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void updateItem_basic() throws Exception {
        // given
        ItemForm itemForm = this.prepareItemForm();
        itemForm.setName(itemForm.getName() + ", updated");
        String itemFormStr = this.objectMapper.writeValueAsString(itemForm);
        log.debug("updateItem_basic - itemFormStr: [{}]", itemFormStr);
        when(this.itemService.updateItem(itemForm, itemForm.getId())).thenReturn(this.convertToItemDto(itemForm));

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/items/{targetItemId}", itemForm.getId())
                .with(csrf())
                .content(itemFormStr)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    /**
     * void handling is different with return
     * Reference
     * https://baeldung.com/mockito-void-methods
     *
     * @throws Exception
     */
    @Test
    public void deleteItem_basic() throws Exception {
        // given
        // doNothing
        doNothing().when(this.itemService).deleteItem(anyString());

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/items/{targetItemId}", 1L)
                .with(csrf())
//                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }


}
