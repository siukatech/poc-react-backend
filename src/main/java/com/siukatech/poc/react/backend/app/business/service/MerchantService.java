package com.siukatech.poc.react.backend.app.business.service;

import com.siukatech.poc.react.backend.app.business.dto.MerchantDto;
import com.siukatech.poc.react.backend.app.data.entity.MerchantEntity;
import com.siukatech.poc.react.backend.app.data.repository.MerchantRepository;
import com.siukatech.poc.react.backend.app.web.model.MerchantForm;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MerchantService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ModelMapper modelMapper;
    private final MerchantRepository merchantRepository;

    public MerchantService(ModelMapper modelMapper, MerchantRepository merchantRepository) {
        this.modelMapper = modelMapper;
        this.merchantRepository = merchantRepository;
    }

    public List<MerchantDto> findMerchantAll() {
        List<MerchantEntity> merchantEntityList = this.merchantRepository.findAllByOrderByName();
        List<MerchantDto> merchantDtoList = merchantEntityList.stream().map(merchantEntity -> this.modelMapper.map(merchantEntity, MerchantDto.class))
////                .sorted((a1, a2) -> a1.getLastModifiedDatetime().isAfter(a2.getLastModifiedDatetime()) ? 1 : -1) // ascending order
//                .sorted((a1, a2) -> a1.getLastModifiedDatetime().isAfter(a2.getLastModifiedDatetime()) ? -1 : 1) // descending order
                .collect(Collectors.toList());
        return merchantDtoList;
    }

    public MerchantDto findMerchantById(Long targetMerchantId) {
        MerchantDto merchantDto = this.merchantRepository.findById(targetMerchantId)
                .map(merchantEntity -> this.modelMapper.map(merchantEntity, MerchantDto.class))
                .orElseThrow(() -> new EntityNotFoundException("targetMerchantId: %s".formatted(targetMerchantId)));
        return merchantDto;
    }

    public MerchantDto findMerchantByMid(String targetMerchantMid) {
        MerchantDto merchantDto = this.merchantRepository.findByMid(targetMerchantMid)
                .map(merchantEntity -> this.modelMapper.map(merchantEntity, MerchantDto.class))
                .orElseThrow(() -> new EntityNotFoundException("targetMerchantMid: %s".formatted(targetMerchantMid)));
        return merchantDto;
    }

    public MerchantDto createMerchant(MerchantForm merchantForm) {
        // This converts MerchantForm to blank new MerchantEntity
        MerchantEntity merchantReq = this.modelMapper.map(merchantForm, MerchantEntity.class);
        logger.debug("createMerchant - before save - merchantForm.getId: [" + merchantForm.getId()
                + "], merchantForm: [" + merchantForm
                + "], merchantReq.getId: [" + merchantReq.getId()
                + "], merchantReq: [" + merchantReq
                + "]");
        merchantReq = this.merchantRepository.save(merchantReq);
        Long createdMerchantId = merchantReq.getId();
        MerchantDto merchantDto = this.findMerchantById(createdMerchantId);
        //
        logger.debug("createMerchant - after save - createdMerchantId: [" + createdMerchantId
                + "], merchantForm.getId: [" + merchantForm.getId()
                + "], merchantForm: [" + merchantForm
                + "], merchantReq.getId: [" + merchantReq.getId()
                + "], merchantReq: [" + merchantReq
                + "]");
        return merchantDto;
    }

    public MerchantDto updateMerchant(MerchantForm merchantForm, Long targetMerchantId) {
        // findMerchantById returns MerchantDto, not MerchantEntity
        // Therefore, merchantRepository is required at this point
        MerchantEntity merchantEntity = this.merchantRepository.findById(targetMerchantId).orElseThrow(() -> new EntityNotFoundException("targetMerchantId: %s".formatted(targetMerchantId)));

        MerchantEntity merchantReq = new MerchantEntity();
        // This maps merchantEntity's values to merchantReq
        this.modelMapper.map(merchantEntity, merchantReq);

        // merchantForm versionNo was null and caused the repository treats as "CREATE"
        // This maps merchantForm's values to merchantReq
        this.modelMapper.map(merchantForm, merchantReq);
        //
        logger.debug("updateMerchant - before save - merchantEntity.getId: [" + merchantEntity.getId()
                + "], merchantEntity: [" + merchantEntity
                + "], merchantReq.getId: [" + merchantReq.getId()
                + "], merchantReq: [" + merchantReq
                + "]");
        merchantReq = this.merchantRepository.save(merchantReq);
        Long updatedMerchantId = merchantReq.getId();
        MerchantDto merchantDto = this.findMerchantById(updatedMerchantId);
        //
        logger.debug("updateMerchant - after save - updatedMerchantId: [" + updatedMerchantId
                + "], merchantForm.getId: [" + merchantForm.getId()
                + "], merchantForm: [" + merchantForm
                + "], merchantReq.getId: [" + merchantReq.getId()
                + "], merchantReq: [" + merchantReq
                + "]");
        return merchantDto;
    }

    public void deleteMerchant(Long targetMerchantId) {
        MerchantEntity merchantEntity = this.merchantRepository.findById(targetMerchantId).orElseThrow(() -> new EntityNotFoundException("targetMerchantId: %s".formatted(targetMerchantId)));
        this.merchantRepository.delete(merchantEntity);
    }

}