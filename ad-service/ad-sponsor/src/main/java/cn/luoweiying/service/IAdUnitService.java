package cn.luoweiying.service;

import cn.luoweiying.exception.AdException;
import cn.luoweiying.vo.AdUnitRequest;
import cn.luoweiying.vo.AdUnitResponse;
import cn.luoweiying.vo.unit_condition.*;

public interface IAdUnitService {

    AdUnitResponse createUnit(AdUnitRequest request) throws AdException;

    //限制维度
    AdUnitKeywordResponse createUntiKeyword(AdUnitKeywordRequest request) throws AdException;

    AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException;

    AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException;

    //推广单元与创意
    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException;
}
