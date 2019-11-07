package cn.luoweiying.index.keyword;

import cn.luoweiying.index.IndexAware;
import cn.luoweiying.index.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

;

//倒排索引
@Slf4j
@Component
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {

    private static Map<String, Set<Long>> keywordUnitMap;//倒排索引，一个关键词对应多个推广单元
    private static Map<Long, Set<String>> unitKeywordMap;//正向索引，一个单元对应多个关键词

    static {
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        if (StringUtils.isEmpty(key)) {
            return Collections.emptySet();
        }
        Set<Long> result = keywordUnitMap.get(key);
        return result != null ? result : Collections.emptySet();
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("UnitKeywordIndex, before add: {}", unitKeywordMap);
        //map中是否存在
        CommonUtils.getOrCreate(key, keywordUnitMap, ConcurrentSkipListSet::new).addAll(value);

/*        Set<Long> unitIdsSet = CommonUtils.getOrCreate(key, keywordUnitMap, ConcurrentSkipListSet::new);
        unitIdsSet.addAll(value);*/
        //同理检查另外一个map,并添加
        value.forEach(x -> CommonUtils.getOrCreate(x, unitKeywordMap, ConcurrentSkipListSet::new).add(key));
        //同上，原做法
 /*       for (Long unitId : value) {
            Set<String> keywordSet = CommonUtils.getOrCreate(unitId, unitKeywordMap, ConcurrentSkipListSet::new);
            keywordSet.add(key);
        }*/
        log.info("UnitKeywordIndex, after add: {}", unitKeywordMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        //自己后面实现
        log.error("keyword index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("UnitKeywordIndex, before delete: {}", unitKeywordMap);
        CommonUtils.getOrCreate(key, keywordUnitMap, ConcurrentSkipListSet::new).removeAll(value);
        value.forEach(x -> CommonUtils.getOrCreate(x, unitKeywordMap, ConcurrentSkipListSet::new).remove(key));
        log.info("UnitKeywordIndex, after delete: {}", unitKeywordMap);
    }

    //匹配，推广单元是否和关键词匹配
    public boolean match(Long unitId, List<String> keywrod) {
        if (unitKeywordMap.containsKey(unitId)
                && CollectionUtils.isEmpty(unitKeywordMap.get(unitId))) {
            Set<String> unitKeywords = unitKeywordMap.get(unitId);
            //子集 org.apache.commons.collections4.CollectionUtils;
            return CollectionUtils.isSubCollection(keywrod, unitKeywords);
        }
        return false;
    }
}
