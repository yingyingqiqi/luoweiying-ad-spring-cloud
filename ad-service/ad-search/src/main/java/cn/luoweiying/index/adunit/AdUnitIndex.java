package cn.luoweiying.index.adunit;

import cn.luoweiying.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AdUnitIndex implements IndexAware<Long, AdUnitObject> {
    private static Map<Long, AdUnitObject> objectMap;
    static {
        objectMap = new ConcurrentHashMap<>();
    }
//匹配 流量类型 满足则返回ids
    public Set<Long> metch(Integer positionType) {

        Set<Long> adUnitIds = new HashSet<>();
        objectMap.forEach((k,v)->{
            if (AdUnitObject.isAdSlotTypeOK(positionType, v.getPositionType())) {
                adUnitIds.add(k);
            }
        });
        return adUnitIds;
    }
    //通过ids匹配对象
    public List<AdUnitObject> fech(Collection<Long> adUnitIds) {
        if (CollectionUtils.isEmpty(adUnitIds)) return Collections.emptyList();
        List<AdUnitObject> adUnitObjects = new ArrayList<>();
        adUnitIds.forEach(id->{
            AdUnitObject object = get(id);
            if (null == object) {
                log.error("AdUnitObject not found :{}", id);
                return;
            }
            adUnitObjects.add(object);
        });
        return adUnitObjects;
    }
    @Override
    public AdUnitObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdUnitObject value) {
        log.info("before add: {}", objectMap);
        objectMap.put(key, value);
        log.info("after add: {}", objectMap);
    }

    @Override
    public void update(Long key, AdUnitObject value) {
        log.info("before update: {}",objectMap);
        AdUnitObject oldAdUnitObject = objectMap.get(key);
        if (null == oldAdUnitObject) {
            objectMap.put(key, value);
        } else {
            oldAdUnitObject.update(value);
        }
        log.info("after update: {}", objectMap);
    }

    @Override
    public void delete(Long key, AdUnitObject value) {
        log.info("before delete: {}", objectMap);
        objectMap.remove(key);
        log.info("after delete:{}", objectMap);
    }
}
