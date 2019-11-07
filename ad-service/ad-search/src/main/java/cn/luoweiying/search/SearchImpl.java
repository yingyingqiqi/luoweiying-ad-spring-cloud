package cn.luoweiying.search;

import cn.luoweiying.index.DataTable;
import cn.luoweiying.index.adunit.AdUnitIndex;
import cn.luoweiying.index.adunit.AdUnitObject;
import cn.luoweiying.index.creative.CreativeIndex;
import cn.luoweiying.index.creative.CreativeObject;
import cn.luoweiying.index.creativeunit.CreativeUnitIndex;
import cn.luoweiying.index.district.UnitDistrictIndex;
import cn.luoweiying.index.interest.UnitItIndex;
import cn.luoweiying.index.keyword.UnitKeywordIndex;
import cn.luoweiying.search.vo.SearchRequest;
import cn.luoweiying.search.vo.SearchResponse;
import cn.luoweiying.search.vo.feature.DistrictFeature;
import cn.luoweiying.search.vo.feature.FeatureRelation;
import cn.luoweiying.search.vo.feature.ItFeature;
import cn.luoweiying.search.vo.feature.KeywordFeature;
import cn.luoweiying.search.vo.media.AdSlot;
import cn.luoweiying.constant.CommonStatus;
import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
//广告匹配， 并返回  ，那为什么检索那么多，没有高效的算法 ，形成千人千面的结果（推荐系统）
@Slf4j
@Component(value = "searchImpl")
public class SearchImpl implements ISearch {
    @Override
    @HystrixCommand()
    public SearchResponse fetchAds(SearchRequest searchRequest) {
        //请求的广告位信息
        List<AdSlot> adSlots = searchRequest.getReqestInfo().getAdSlots();
        //三个Feature
        KeywordFeature keywordFeature = searchRequest.getFeatureInfo().getKeywordFeature();
        ItFeature itFeature = searchRequest.getFeatureInfo().getItFeature();
        DistrictFeature districtFeature = searchRequest.getFeatureInfo().getDistrictFeature();
        FeatureRelation relation = searchRequest.getFeatureInfo().getRelation();
        //构造SearchResponse响应对象
        SearchResponse response = new SearchResponse();
        Map<String, List<SearchResponse.Creative>> adSlot2Ads = response.getAdSlot2Ads();
        //匹配
        adSlots.forEach(adSlot -> {

            Set<Long> targetUnitIdSet;

            //根据流量类型获取初始 AdUnitids
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).metch(adSlot.getPositionType());
            //根据匹配规则 ，再次限制adUnitIds
            if (relation == FeatureRelation.AND) {
                filterKeywordFeature(adUnitIdSet, keywordFeature);
                filterItTagFeature(adUnitIdSet, itFeature);
                filterDistrictFeature(adUnitIdSet, districtFeature);
                targetUnitIdSet = adUnitIdSet;
            } else {
                targetUnitIdSet =getOrRelationUnitIds(adUnitIdSet,
                        keywordFeature, districtFeature, itFeature);
            }
            List<AdUnitObject> unitObjects = DataTable.of(AdUnitIndex.class).fech(targetUnitIdSet);
            //状态信息
            filterAdUnitAndPlanStatus(unitObjects,CommonStatus.VALID);
            //拿到创意ids
            List<Long> adIds = DataTable.of(CreativeUnitIndex.class).selectAds(unitObjects);
            //拿到创意对象
            List<CreativeObject> creativeObjects = DataTable.of(CreativeIndex.class).fetch(adIds);
            //根据广告位信息筛选创意
            filterCreativeByAdSlot(creativeObjects,adSlot.getWidth(),
                    adSlot.getHeight(),adSlot.getType());
            //填充, 填充一个创意   ???    那为什么检索那么多，没有高效的算法 ，形成千人千面的结果
            adSlot2Ads.put(adSlot.getAdSlotCode(),buildCreativeResponse(creativeObjects));
        });
        log.info("fetchAds: {}-{}", JSON.toJSONString(relation),JSON.toJSONString(response));

        return response;
    }

    //根据关键字 再次筛选
    public void filterKeywordFeature(Collection<Long> adUntiIds, KeywordFeature keywordFeature) {
        if (CollectionUtils.isEmpty(adUntiIds)) return;
        if (CollectionUtils.isNotEmpty(keywordFeature.getKeywords())) {
            CollectionUtils.filter(adUntiIds
                    , (adUnitId) -> DataTable.of(UnitKeywordIndex.class)
                            .match(adUnitId, keywordFeature.getKeywords()));
        }
    }

    //根据地域 再次筛选
    public void filterDistrictFeature(Collection<Long> adUntiIds, DistrictFeature districtFeature) {
        if (CollectionUtils.isEmpty(adUntiIds)) return;
        if (CollectionUtils.isNotEmpty(districtFeature.getDistrictFeature())) {
            CollectionUtils.filter(adUntiIds
                    , (adUnitId) -> DataTable.of(UnitDistrictIndex.class)
                            .match(adUnitId, districtFeature.getDistrictFeature()));
        }
    }

    //根据兴趣 再次筛选
    public void filterItTagFeature(Collection<Long> adUntiIds, ItFeature itFeature) {
        if (CollectionUtils.isEmpty(adUntiIds)) return;
        if (CollectionUtils.isNotEmpty(itFeature.getIts())) {
            CollectionUtils.filter(adUntiIds
                    , (adUnitId) -> DataTable.of(UnitItIndex.class)
                            .match(adUnitId, itFeature.getIts()));
        }
    }

    //限制信息不全匹配
    private Set<Long> getOrRelationUnitIds(Set<Long> adUnitIdSet, KeywordFeature keywordFeature,
                                           DistrictFeature districtFeature,
                                           ItFeature itFeature) {
        if (CollectionUtils.isEmpty(adUnitIdSet)) return Collections.emptySet();

        //备份adunitids
        Set<Long> keywordFeatureSet = new HashSet<>(adUnitIdSet);
        Set<Long> districtFeatureSet = new HashSet<>(adUnitIdSet);
        Set<Long> itFeatureSet = new HashSet<>(adUnitIdSet);

        filterKeywordFeature(keywordFeatureSet, keywordFeature);
        filterDistrictFeature(districtFeatureSet, districtFeature);
        filterItTagFeature(itFeatureSet, itFeature);

        return new HashSet<>(
                CollectionUtils.union(
                        CollectionUtils.union(keywordFeatureSet, districtFeatureSet), itFeatureSet)
        );
    }

    //为什么不提前根据状态信息提前筛选呢！
    public void filterAdUnitAndPlanStatus(List<AdUnitObject> unitObjects, CommonStatus status) {
        if (CollectionUtils.isEmpty(unitObjects)) return;
        CollectionUtils.filter(unitObjects, unitObject ->
                unitObject.getUnitStatus().equals(status.getStatus()) &&
                        unitObject.getAdPlanObject().getPlanStatus().equals(status.getStatus()));
    }

    //为什么不根据广告位信息 提前筛选
    public void filterCreativeByAdSlot(List<CreativeObject> creativeObjects, Integer width,
                                       Integer height, List<Integer> type) {
        if (CollectionUtils.isEmpty(creativeObjects)) return;
        CollectionUtils.filter(creativeObjects, creativeObject ->
                creativeObject.getAuditStatus().equals(CommonStatus.VALID.getStatus())
                        && creativeObject.getWidth().equals(width)
                        && creativeObject.getHeight().equals(height)
                        && type.contains(creativeObject.getType())
        );
    }

    private List<SearchResponse.Creative> buildCreativeResponse(List<CreativeObject> creatives) {

        if (CollectionUtils.isEmpty(creatives)) return Collections.emptyList();

        CreativeObject randomObject = creatives.get(
                Math.abs(new Random().nextInt()) % creatives.size()
        );
//        return new ArrayList<>((Collection<? extends SearchResponse.Creative>) SearchResponse.convert(randomObject));
        return Collections.singletonList(SearchResponse.convert(randomObject));
    }
}
