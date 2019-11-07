package cn.luoweiying.search.vo;

import cn.luoweiying.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {

    public Map<String, List<Creative>> adSlot2Ads = new HashMap<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Creative {

        public Long adId;
        public String adUrl;
        public Integer width;
        public Integer height;
        public Integer type;
        public Integer materialType;

        //监测原本是要再创意对象绑定的， 这里为了方便。
        //展示监测url  广告位曝光此创意
        public List<String> showMonitorUrl = Arrays.asList("www.luoweiying.cm", "www.luoweiying.cm");
        //点击监测url  用户点击此创意
        public List<String> clickMonitorUrl = Arrays.asList("www.luoweiying.cm", "www.luoweiying.cm");
    }
    //对象转换
    public static Creative convert(CreativeObject creativeObject) {
        Creative creative = new Creative();
        creative.setAdId(creativeObject.getAdId());
        creative.setAdUrl(creativeObject.getAdUrl());
        creative.setWidth(creativeObject.getWidth());
        creative.setHeight(creativeObject.getHeight());
        creative.setType(creativeObject.getType());
        creative.setMaterialType(creativeObject.getMaterialType());
        return creative;
    }
}
