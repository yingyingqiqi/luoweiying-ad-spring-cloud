package cn.luoweiying.client;

import cn.luoweiying.client.vo.AdPlan;
import cn.luoweiying.client.vo.AdPlanGetRequest;
import cn.luoweiying.exception.AdException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "eureka-client-ad-sponsor",fallback = SponsorClientHystrix.class)
public interface SponsorClient {
    @RequestMapping(value = "/ad-sponsor/get/adPlan",method = RequestMethod.POST)
    List<AdPlan> getAdPlans(@RequestBody AdPlanGetRequest request) throws AdException;
}
