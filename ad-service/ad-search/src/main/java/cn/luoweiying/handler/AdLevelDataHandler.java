package cn.luoweiying.handler;

import cn.luoweiying.dump.table.*;
import cn.luoweiying.index.DataTable;
import cn.luoweiying.index.IndexAware;
import cn.luoweiying.index.adplan.AdPlanIndex;
import cn.luoweiying.index.adplan.AdPlanObject;
import cn.luoweiying.index.adunit.AdUnitIndex;
import cn.luoweiying.index.adunit.AdUnitObject;
import cn.luoweiying.index.creative.CreativeIndex;
import cn.luoweiying.index.creative.CreativeObject;
import cn.luoweiying.index.creativeunit.CreativeUnitIndex;
import cn.luoweiying.index.creativeunit.CreativeUnitObject;
import cn.luoweiying.index.district.UnitDistrictIndex;
import cn.luoweiying.index.interest.UnitItIndex;
import cn.luoweiying.index.keyword.UnitKeywordIndex;
import cn.luoweiying.index.utils.CommonUtils;
import cn.luoweiying.mysql.constant.OpType;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 1.索引之间存在着层级的划分，也就是依赖关系的划分
 * 2。加载全量索引是增量索引“添加”的一种特殊实现
 */
@Slf4j
public class AdLevelDataHandler {
    //增量索引
    public static <K, V> void handleBinlogEvent(IndexAware<K, V> index, K key, V value, OpType type) {
        switch (type) {
            case ADD:index.add(key,value);break;
            case UPDATE: index.update(key,value);break;
            case DELETE: index.delete(key,value);break;
            default:break;
        }
    }

    //第二层级，不予其他索引有依赖，创意，推广计划。 增量
    public static void handleLevel2(AdCreativeTable creativeTable, OpType type) {
        CreativeObject adCreativeObject = new CreativeObject(
                creativeTable.getAdId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAuditStatus(),
                creativeTable.getAdUrl()
        );
        handleBinlogEvent(DataTable.of(CreativeIndex.class),creativeTable.getAdId(),adCreativeObject,type);
    }
    public static void handleLevel2(AdPlanTable planTable, OpType type) {
        AdPlanObject adPlanObject = new AdPlanObject(
                planTable.getId(),
                planTable.getUserId(),
                planTable.getPlanStatus(),
                planTable.getStartDate(),
                planTable.getEndDate()
        );
        handleBinlogEvent(DataTable.of(AdPlanIndex.class),planTable.getId(),adPlanObject,type);
    }

    //第三层级 增量
    public static void handlerLevel3(AdUnitTable unitPlanTable, OpType type) {
        AdPlanObject adPlanObject = DataTable.of(AdPlanIndex.class).get(unitPlanTable.getPlanId());
        if (null == adPlanObject) {
            log.error("handleLevel3 found AdPlanObject error: {}",unitPlanTable.getPlanId());
            return;
        }
        AdUnitObject adUnitObject = new AdUnitObject(
                unitPlanTable.getUnitId(),
                unitPlanTable.getUnitStatus(),
                unitPlanTable.getPositionType(),
                unitPlanTable.getPlanId(),
                adPlanObject
        );
        handleBinlogEvent(DataTable.of(AdUnitIndex.class),unitPlanTable.getUnitId(),adUnitObject,type);
    }

    public static void handlerLevel3(AdCreativeUnitTable adCreativeUnitTable, OpType type) {
        AdUnitObject adUnitObject = DataTable.of(AdUnitIndex.class).get(adCreativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(adCreativeUnitTable.getAdId());
        if (null == adUnitObject || null == creativeObject) {
            log.error("AdCreativeUnitTable index error: {}", JSON.toJSONString(creativeObject));
            return;
        }
        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                adCreativeUnitTable.getAdId(),
                adCreativeUnitTable.getUnitId()
        );
        handleBinlogEvent(
                DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringConcat(
                        creativeUnitObject.getAdId().toString(),
                        creativeUnitObject.getAdId().toString()),
                creativeUnitObject, type
        );
    }
    //第四层级
    public static void handleLevel4(AdUnitDistrictTable unitDistrictTable,
                                    OpType type) {

        if (type == OpType.UPDATE) {
            log.error("district index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(unitDistrictTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitDistrictTable index error: {}", unitDistrictTable.getUnitId());
            return;
        }

        String key = CommonUtils.stringConcat(
                unitDistrictTable.getProvince(),
                unitDistrictTable.getCity()
        );
        Set<Long> value = new HashSet<>(
                Collections.singleton(unitDistrictTable.getUnitId())
        );
        handleBinlogEvent(
                DataTable.of(UnitDistrictIndex.class),
                key, value,
                type
        );
    }

    public static void handleLevel4(AdUnitItTable unitItTable, OpType type) {

        if (type == OpType.UPDATE) {
            log.error("it index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(unitItTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitItTable index error: {}",
                    unitItTable.getUnitId());
            return;
        }

        Set<Long> value = new HashSet<>(
                Collections.singleton(unitItTable.getUnitId())
        );
        handleBinlogEvent(
                DataTable.of(UnitItIndex.class),
                unitItTable.getItTag(),
                value,
                type
        );
    }

    public static void handleLevel4(AdUnitKeywordTable keywordTable,
                                    OpType type) {

        if (type == OpType.UPDATE) {
            log.error("keyword index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(keywordTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitKeywordTable index error: {}",
                    keywordTable.getUnitId());
            return;
        }

        Set<Long> value = new HashSet<>(
                Collections.singleton(keywordTable.getUnitId())
        );
        handleBinlogEvent(
                DataTable.of(UnitKeywordIndex.class),
                keywordTable.getKeyword(),
                value,
                type
        );
    }
}
