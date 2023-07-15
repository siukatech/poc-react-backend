package com.siukatech.poc.react.backend.app.data;


import com.siukatech.poc.react.backend.app.AbstractJpaTests;
import com.siukatech.poc.react.backend.app.business.dto.ItemDto;
import com.siukatech.poc.react.backend.app.data.entity.ItemEntity;
import com.siukatech.poc.react.backend.app.data.repository.ItemRepository;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemRepositoryUnitTests extends AbstractJpaTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ItemRepository itemRepository;


    private ItemEntity prepareItemEntity_basic() {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setName("shf figure 1");
        itemEntity.setPurchasedDate(LocalDate.now());
        itemEntity.setCreatedBy("admin");
        itemEntity.setCreatedDatetime(LocalDateTime.now());
        itemEntity.setLastModifiedBy("admin");
        itemEntity.setLastModifiedDatetime(LocalDateTime.now());
        itemEntity.setVersionNo(1L);
        return itemEntity;
    }

    private ItemDto prepareItemDto_basic() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("shf figure 1");
        itemDto.setPurchasedDate(LocalDate.now());
        itemDto.setCreatedBy("admin");
        itemDto.setCreatedDatetime(LocalDateTime.now());
        itemDto.setLastModifiedBy("admin");
        itemDto.setLastModifiedDatetime(LocalDateTime.now());
        itemDto.setVersionNo(1L);
        return itemDto;
    }

    @BeforeEach()
    public void setup(TestInfo testInfo) {
        ItemEntity itemEntity = this.prepareItemEntity_basic();
        this.itemRepository.save(itemEntity);
    }

    @AfterEach
    public void teardown(TestInfo testInfo) {
        ItemEntity itemEntity = this.prepareItemEntity_basic();
        this.itemRepository.delete(itemEntity);
    }

    @Test
    public void findAll_basic() {
        List<ItemEntity> itemEntityList = itemRepository.findAll();
        assertThat(itemEntityList).filteredOn(itemEntity -> {
            return itemEntity.getName().contains("shf ");
        });
    }

}
