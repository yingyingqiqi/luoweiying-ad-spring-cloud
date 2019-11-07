package cn.luoweiying.index.interest;

import cn.luoweiying.index.IndexAware;
import cn.luoweiying.index.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 兴趣索引
 */
@Slf4j
@Component
public class UnitItIndex implements IndexAware<String, Set<Long>> {

    // <itTag, adUnitId set>， 反向索引
    private static Map<String, Set<Long>> itUnitMap;

    // <unitId, itTag set>， 正向索引
    private static Map<Long, Set<String>> unitItMap;

    static {
        itUnitMap = new ConcurrentHashMap<>();
        unitItMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        return itUnitMap.get(key);
    }

    @Override
    public void add(String key, Set<Long> value) {

        log.info("UnitItIndex, before add: {}", unitItMap);
//CommonUtils.getOrCreate(key, itUnitMap, ConcurrentSkipListSet::new).addAll(value);
        Set<Long> unitIds = CommonUtils.getOrCreate(
                key, itUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.addAll(value);
//value.forEach(x -> CommonUtils.getOrCreate(x, unitItMap, ConcurrentSkipListSet::new).add(key));
        for (Long unitId : value) {

            Set<String> its = CommonUtils.getOrCreate(
                    unitId, unitItMap,
                    ConcurrentSkipListSet::new
            );
            its.add(key);
        }

        log.info("UnitItIndex, after add: {}", unitItMap);
    }

    @Override
    public void update(String key, Set<Long> value) {

        log.error("it index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {

        log.info("UnitItIndex, before delete: {}", unitItMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(
                key, itUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.removeAll(value);

        for (Long unitId : value) {
            Set<String> itTagSet = CommonUtils.getOrCreate(
                    unitId, unitItMap,
                    ConcurrentSkipListSet::new
            );
            itTagSet.remove(key);
        }

        log.info("UnitItIndex, after delete: {}", unitItMap);
    }

    public boolean match(Long unitId, List<String> itTags) {

        if (unitItMap.containsKey(unitId)
                && CollectionUtils.isNotEmpty(unitItMap.get(unitId))) {

            Set<String> unitKeywords = unitItMap.get(unitId);

            return CollectionUtils.isSubCollection(itTags, unitKeywords);
        }

        return false;
    }
}
