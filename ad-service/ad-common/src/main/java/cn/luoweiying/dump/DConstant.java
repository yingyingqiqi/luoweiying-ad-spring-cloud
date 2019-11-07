package cn.luoweiying.dump;

import java.io.File;

/**
 * Created by Qinyi.
 */
public class DConstant {

    public static final String DATA_ROOT_DIR = "f:" + File.separator + "暂时存放" + File.separator +
            "com.luoweiying" + File.separator + "luoweiying-ad-spring-cloud" + File.separator +
            "mysql_data" + File.separator;
//    public static final String DATA_ROOT_DIR = "/Users/zhanghu05/imooc/mysql_data/";

    // 各个表数据的存储文件名
    public static final String AD_PLAN = "ad_plan.data";
    public static final String AD_UNIT = "ad_unit.data";
    public static final String AD_CREATIVE = "ad_creative.data";
    public static final String AD_CREATIVE_UNIT = "ad_creative_unit.data";
    public static final String AD_UNIT_IT = "ad_unit_it.data";
    public static final String AD_UNIT_DISTRICT = "ad_unit_district.data";
    public static final String AD_UNIT_KEYWORD = "ad_unit_keyword.data";
}
