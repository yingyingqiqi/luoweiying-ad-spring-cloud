package cn.luoweiying.search;

import cn.luoweiying.ApplicationTest;
import cn.luoweiying.search.vo.SearchRequest;
import cn.luoweiying.search.vo.SearchResponse;
import cn.luoweiying.search.vo.feature.DistrictFeature;
import cn.luoweiying.search.vo.feature.FeatureRelation;
import cn.luoweiying.search.vo.feature.ItFeature;
import cn.luoweiying.search.vo.feature.KeywordFeature;
import cn.luoweiying.search.vo.media.AdSlot;
import cn.luoweiying.search.vo.media.App;
import cn.luoweiying.search.vo.media.Device;
import cn.luoweiying.search.vo.media.Geo;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationTest.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ISearchTest {
    @Autowired
    private ISearch search;


    @Test
    public void fetchAdsTest() {
        SearchRequest request = new SearchRequest();
        request.setMediaId("请求方标识--我是1111");
        request.setReqestInfo(getRequsetInfo);
        request.setFeatureInfo(getFeatureInfo);
        SearchResponse searchResponse = search.fetchAds(request);
        System.out.println(JSON.toJSONString(request));
        System.out.println(JSON.toJSONString(searchResponse));
        //第二个请求
        request.setMediaId("请求方标识--我是2222");
        request.setReqestInfo(getRequsetInfo_two);
        request.setFeatureInfo(getFeatureInfo_two);
        searchResponse = search.fetchAds(request);
        System.out.println(JSON.toJSONString(request));
        System.out.println(JSON.toJSONString(searchResponse));

    }

    public static SearchRequest.RequestInfo getRequsetInfo = new SearchRequest.RequestInfo(
            "aaa"
            , Collections.singletonList(new AdSlot("ad-x", 1, 1080, 720, Arrays.asList(1, 2), 1000))
            , new App("imooc", "imooc", "com.imood", "video")
            , new Geo(100.28f, 88.61f, "北京市", "北京市")
            , new Device("iphone", "00000xxxxx", "127.0.0.1", "x", "1080", "720", "123456789"));
    public static SearchRequest.FeatureInfo getFeatureInfo = new SearchRequest.FeatureInfo(
            new KeywordFeature(Arrays.asList("宝马", "大众"))
            , new DistrictFeature(Collections.singletonList(new DistrictFeature.ProvinceAndCity("安徽省", "合肥")))
            , new ItFeature(Arrays.asList("台球", "游泳"))
            , FeatureRelation.OR
    );
    public static SearchRequest.RequestInfo getRequsetInfo_two = new SearchRequest.RequestInfo(
            "aaa"
            , Collections.singletonList(new AdSlot("ad-y", 1, 1080, 720, Arrays.asList(1, 2), 1000))
            , new App("imooc", "imooc", "com.imood", "video")
            , new Geo(100.28f, 88.61f, "北京市", "北京市")
            , new Device("iphone", "00000xxxxx", "127.0.0.1", "x", "1080", "720", "123456789"));
    public static SearchRequest.FeatureInfo getFeatureInfo_two = new SearchRequest.FeatureInfo(
            new KeywordFeature(Arrays.asList("宝马", "大众", "标志"))
            , new DistrictFeature(Collections.singletonList(new DistrictFeature.ProvinceAndCity("安徽省", "合肥")))
            , new ItFeature(Arrays.asList("台球", "游泳"))
            , FeatureRelation.AND
    );

}
