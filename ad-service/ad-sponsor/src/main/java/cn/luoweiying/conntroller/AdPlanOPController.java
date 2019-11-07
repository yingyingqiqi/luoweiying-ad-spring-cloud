package cn.luoweiying.conntroller;

import cn.luoweiying.entity.AdPlan;
import cn.luoweiying.exception.AdException;
import cn.luoweiying.service.impl.AdPlanServiceImpl;
import cn.luoweiying.vo.AdPlanGetRequest;
import cn.luoweiying.vo.AdPlanRequest;
import cn.luoweiying.vo.AdPlanResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class AdPlanOPController {

    private final AdPlanServiceImpl adPlanService;
    @Autowired
    public AdPlanOPController(AdPlanServiceImpl adPlanService) {
        this.adPlanService = adPlanService;
    }

    @PostMapping("/create/adPlan")
    public AdPlanResponse createAdPlan(@RequestBody AdPlanRequest request) throws AdException {
        log.info("ad-sponsor:creatAdPlan: -> {}", JSON.toJSON(request));
        return adPlanService.createAdPlan(request);
    }
    @PostMapping("get/adPlan")
    public List<AdPlan> getAdPlanByIds(@RequestBody AdPlanGetRequest request) throws AdException {
        log.info("ad-sponsor:getAdPlanByIds: -> {}",JSON.toJSONString(request));
        return adPlanService.getAdPlanByIds(request);
    }
    @PutMapping("update/adPlan")
    public AdPlanResponse updateAdPlan(@RequestBody AdPlanRequest request) throws AdException {
        log.info("ad-sponsor:updateAdPlan: -> {}",JSON.toJSONString(request));
        return adPlanService.updateAdPlan(request);
    }
    @DeleteMapping("delete/adPlan")
    public void deleteAdPlan(@RequestBody AdPlanRequest request) throws AdException {
        log.info("ad-sponsor:deleteAdPlan: -> {}",JSON.toJSONString(request));
        adPlanService.deleteAdPlan(request);
    }
    @RequestMapping("test")
    public List<String> Test() {

        List<AdPlan> all = adPlanService.findAll();
        all.forEach(x->{
            System.out.println(x.toString());
            System.out.println(x.getId());
        });
        List<String> collect = all.stream().map(JSON::toJSONString).collect(Collectors.toList());
        return collect;
    }

    @RequestMapping("exception")
    public void AdExceptionTest() throws AdException {
        adPlanService.adExceptionTest();
    }
}
