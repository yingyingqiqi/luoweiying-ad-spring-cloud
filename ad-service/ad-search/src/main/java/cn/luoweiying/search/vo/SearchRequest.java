package cn.luoweiying.search.vo;

import cn.luoweiying.search.vo.feature.DistrictFeature;
import cn.luoweiying.search.vo.feature.FeatureRelation;
import cn.luoweiying.search.vo.feature.ItFeature;
import cn.luoweiying.search.vo.feature.KeywordFeature;
import cn.luoweiying.search.vo.media.AdSlot;
import cn.luoweiying.search.vo.media.App;
import cn.luoweiying.search.vo.media.Device;
import cn.luoweiying.search.vo.media.Geo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//广告位请求对象
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    //    *1.媒体方请求标识
    private String mediaId;
    //     * 2.请求基本信息     id.宽高、地域、mac、ip、设备信息
    private RequestInfo reqestInfo;
    //     * 3.请求匹配信息     关键字、兴趣、地域
    private FeatureInfo featureInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestInfo {

        private String requestId;
        private List<AdSlot> adSlots;
        private App app;
        private Geo geo;
        private Device device;
    }
    //匹配信息的
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FeatureInfo {
        private KeywordFeature keywordFeature;
        private DistrictFeature districtFeature;
        private ItFeature itFeature;

        private FeatureRelation relation = FeatureRelation.AND;
    }
}
