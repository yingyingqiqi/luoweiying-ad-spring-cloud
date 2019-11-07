package cn.luoweiying.index;

import cn.luoweiying.dump.DConstant;
import cn.luoweiying.dump.table.*;
import cn.luoweiying.handler.AdLevelDataHandler;
import cn.luoweiying.mysql.constant.OpType;
import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

//加载索引表，是一个全量索引
@Component
@DependsOn("dataTable")
public class IndexFileLoder {
    @PostConstruct
    public void init() {
        //第二层级表 加载
        List<String> adPlanString = loadDumpData(String.format(
                "%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_PLAN
        ));
        adPlanString.forEach(plan -> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(plan, AdPlanTable.class), OpType.ADD
        ));

        List<String> adCreativeString = loadDumpData(String.format(
                "%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE
        ));
        adCreativeString.forEach(creative -> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(creative, AdCreativeTable.class), OpType.ADD
        ));
        //第三层级
        List<String> adUnitString = loadDumpData(String.format(
                "%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT
        ));
        adUnitString.forEach(unit -> AdLevelDataHandler.handlerLevel3(
                JSON.parseObject(unit, AdUnitTable.class), OpType.ADD
        ));

        List<String> adCreativeUnitString = loadDumpData(String.format(
                "%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE_UNIT
        ));
        adCreativeUnitString.forEach(creativeUnit -> AdLevelDataHandler.handlerLevel3(
                JSON.parseObject(creativeUnit, AdCreativeUnitTable.class),OpType.ADD
        ));
        //第四层级
        List<String> adUnitDistrictStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_DISTRICT)
        );
        adUnitDistrictStrings.forEach(d -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(d, AdUnitDistrictTable.class),
                OpType.ADD
        ));

        List<String> adUnitItStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_IT)
        );
        adUnitItStrings.forEach(i -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(i, AdUnitItTable.class),
                OpType.ADD
        ));

        List<String> adUnitKeywordStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_KEYWORD)
        );
        adUnitKeywordStrings.forEach(k -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(k, AdUnitKeywordTable.class),
                OpType.ADD
        ));
    }

    private List<String> loadDumpData(String fileName) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
            return br.lines().collect(Collectors.toList());
        } catch (IOException e) {
//            throw new AdException(CodeMsg.INDEX_FILE_LODER_ERROR);
            throw new RuntimeException(e.getMessage());
        }
    }
}
