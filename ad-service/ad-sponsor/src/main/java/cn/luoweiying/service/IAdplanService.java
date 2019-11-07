package cn.luoweiying.service;

import cn.luoweiying.exception.AdException;
import cn.luoweiying.entity.AdPlan;
import cn.luoweiying.vo.AdPlanGetRequest;
import cn.luoweiying.vo.AdPlanRequest;
import cn.luoweiying.vo.AdPlanResponse;

import java.util.List;

public interface IAdplanService {

    /**
     * 创建推广计划
     * */
    AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException;

    /**
     * 获取推广单元
     * */
    List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException;

    /**
     * 更新推广计划
     * */
    AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException;

    /**
     * 删除推广计划
     * */
    void deleteAdPlan(AdPlanRequest request) throws AdException;
}
