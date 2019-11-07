package cn.luoweiying.client;

import cn.luoweiying.client.vo.AdPlan;
import cn.luoweiying.client.vo.AdPlanGetRequest;
import cn.luoweiying.exception.AdException;
import cn.luoweiying.vo.CodeMsg;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class SponsorClientHystrix implements SponsorClient {
    @Override
    public List<AdPlan> getAdPlans(AdPlanGetRequest request) throws AdException {
        throw new AdException(CodeMsg.EUREKA_CLIENT_AD_SPONSOR_ERROR);
//        return new ArrayList<>().add(CommonResponse.error(CodeMsg.EUREKA_CLIENT_AD_SPONSOR_ERROR));
//        return CommonResponse.error(CodeMsg.EUREKA_CLIENT_AD_SPONSOR_ERROR);
//        return CommonResponse.error(CodeMsg.EUREKA_CLIENT_AD_SPONSOR_ERROR);
    }
}
