package cn.luoweiying.service.impl;

import cn.luoweiying.exception.AdException;
import cn.luoweiying.vo.CodeMsg;
import cn.luoweiying.dao.AdPlanRepository;
import cn.luoweiying.dao.AdUnitRepository;
import cn.luoweiying.dao.CreativeRepository;
import cn.luoweiying.dao.unit_condition.AdUnitDistrictRepository;
import cn.luoweiying.dao.unit_condition.AdUnitItRepository;
import cn.luoweiying.dao.unit_condition.AdUnitKeywordRepository;
import cn.luoweiying.dao.unit_condition.CreativeUnitRepository;
import cn.luoweiying.entity.AdPlan;
import cn.luoweiying.entity.AdUnit;
import cn.luoweiying.entity.unit_condition.AdUnitDistrict;
import cn.luoweiying.entity.unit_condition.AdUnitIt;
import cn.luoweiying.entity.unit_condition.AdUnitKeyword;
import cn.luoweiying.entity.unit_condition.CreativeUnit;
import cn.luoweiying.service.IAdUnitService;
import cn.luoweiying.vo.AdUnitRequest;
import cn.luoweiying.vo.AdUnitResponse;
import cn.luoweiying.vo.unit_condition.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdUnitServiceImpl implements IAdUnitService {

    private final AdPlanRepository adPlanRepository;
    private final AdUnitRepository adUnitRepository;
    private final AdUnitKeywordRepository adUnitKeywordRepository;
    private final AdUnitDistrictRepository adUnitDistrictRepository;
    private final AdUnitItRepository adUnitItRepository;
    private final CreativeRepository creativeRepository;
    private final CreativeUnitRepository creativeUnitRepository;

    @Autowired
    public AdUnitServiceImpl(AdPlanRepository adPlanRepository,
                             AdUnitRepository adUnitRepository,
                             AdUnitKeywordRepository adUnitKeywordRepository,
                             AdUnitDistrictRepository adUnitDistrictRepository,
                             AdUnitItRepository adUnitItRepository, CreativeRepository creativeRepository, CreativeUnitRepository creativeUnitRepository) {
        this.adPlanRepository = adPlanRepository;
        this.adUnitRepository = adUnitRepository;
        this.adUnitKeywordRepository = adUnitKeywordRepository;
        this.adUnitDistrictRepository = adUnitDistrictRepository;
        this.adUnitItRepository = adUnitItRepository;
        this.creativeRepository = creativeRepository;
        this.creativeUnitRepository = creativeUnitRepository;
    }

    @Override
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {
        if (!request.createValidate()) {
            throw new AdException(CodeMsg.REQUEST_PARAM_ERROR);
        }
//推广计划
        Optional<AdPlan> adPlan = adPlanRepository.findById(request.getPlanId());
        if (!adPlan.isPresent()) {
            throw new AdException(CodeMsg.CAN_NOT_FIND_RECORD);
        }
        //推广单元
        AdUnit oldAdUnit = adUnitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());
        if (oldAdUnit != null) {
            throw new AdException(CodeMsg.SAME_NAME_UNIT_ERROR);
        }
        AdUnit newAdUnit = adUnitRepository.save(new AdUnit(
                request.getPlanId(), request.getUnitName(), request.getPositionType(), request.getBudget()
        ));
        return new AdUnitResponse(newAdUnit.getId(), newAdUnit.getUnitName());
    }

    @Override
    public AdUnitKeywordResponse createUntiKeyword(AdUnitKeywordRequest request) throws AdException {
//        new Thread(() -> System.out.println("lambda"));获取unitids
        List<Long> unitIds = request.getUnitKeywords().stream()
                .map(AdUnitKeywordRequest.UnitKeyword::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(CodeMsg.REQUEST_PARAM_ERROR);
        }
        //生成unitkeyword集合
        List<AdUnitKeyword> oldUnitKeywords = new ArrayList<>();
        request.getUnitKeywords().forEach(
                x -> oldUnitKeywords.add(new AdUnitKeyword(x.getUnitId(),x.getKeyword()))
        );
        //保存集合，并从新的对象中 获取id
        List<AdUnitKeyword> newUnitKeyword = adUnitKeywordRepository.saveAll(oldUnitKeywords);
        List<Long> ids = newUnitKeyword.stream().map(AdUnitKeyword::getId).collect(Collectors.toList());

        return new AdUnitKeywordResponse(ids);
    }

    @Override
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {
        List<Long> unitIds = request.getUnitIts().stream()
                .map(AdUnitItRequest.UnitIt::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(CodeMsg.REQUEST_PARAM_ERROR);
        }
        List<AdUnitIt> oldAdUnitIt = new ArrayList<>();
        request.getUnitIts().stream().forEach(
                x -> oldAdUnitIt.add(new AdUnitIt(x.getUnitId(),x.getItTag()))
        );
        List<AdUnitIt> newAdUnitIts = adUnitItRepository.saveAll(oldAdUnitIt);
        List<Long> ids = newAdUnitIts.stream().map(AdUnitIt::getId).collect(Collectors.toList());
        return new AdUnitItResponse(ids);
    }

    @Override
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {
        List<Long> unitIds = request.getUnitDistricts().stream()
                .map(AdUnitDistrictRequest.UnitDistrict::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(CodeMsg.REQUEST_PARAM_ERROR);
        }
        List<AdUnitDistrict> oldAdUnitDistrict = new ArrayList<>();
        request.getUnitDistricts().forEach(
                x -> oldAdUnitDistrict.add(new AdUnitDistrict(x.getUnitId(),x.getProvince(),x.getCity()))
        );
        List<AdUnitDistrict> newAdUnitDistrict = adUnitDistrictRepository.saveAll(oldAdUnitDistrict);
        List<Long> ids = newAdUnitDistrict.stream().map(AdUnitDistrict::getId).collect(Collectors.toList());
        return new AdUnitDistrictResponse(ids);
    }

    @Override
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {
        //取得IDS
        List<Long> creativeIds = request.getCreativeUnitItems().stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getCreativeId)
                .collect(Collectors.toList());
        List<Long> unitIds = request.getCreativeUnitItems().stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getUnitId)
                .collect(Collectors.toList());
        //验证
        if (!isRelatedUnitExist(unitIds) || !isRelatedCreativeExist(creativeIds)) {
            throw new AdException(CodeMsg.REQUEST_PARAM_ERROR);
        }
        //保存到DB
        List<CreativeUnit> oldCreativeUnits = new ArrayList<>();
        request.getCreativeUnitItems().forEach(x -> oldCreativeUnits
                .add(new CreativeUnit(x.getCreativeId(),x.getUnitId())));
        List<CreativeUnit> newCreativeUnits = creativeUnitRepository.saveAll(oldCreativeUnits);
        //输出
        List<Long> ids = newCreativeUnits.stream()
                .map(CreativeUnit::getId)
                .collect(Collectors.toList());
        return new CreativeUnitResponse(ids);
    }

    //推广单元是否存在
    private boolean isRelatedUnitExist(List<Long> unitIds) {
        if (CollectionUtils.isEmpty(unitIds)) return false;
        return adUnitRepository.findAllById(unitIds).size() == new HashSet<>(unitIds).size();
    }

    //创意是否存在
    private boolean isRelatedCreativeExist(List<Long> creativeIds) {
        if (!CollectionUtils.isEmpty(creativeIds)) return false;
        return creativeRepository.findAllById(creativeIds).size() == new HashSet<>(creativeIds).size();
    }
}
