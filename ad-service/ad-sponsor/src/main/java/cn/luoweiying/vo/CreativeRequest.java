package cn.luoweiying.vo;

import cn.luoweiying.constant.CommonStatus;
import cn.luoweiying.entity.Creative;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreativeRequest {

    private String name;
    private Integer type;
    private Integer materialType;
    private Integer height;
    private Integer width;
    private Long size;
    private Integer duration;
    private Long userId;
    private String url;

    public boolean validate() {
        return !StringUtils.isEmpty(name) &&
                type != null &&
                materialType != null &&
                height != null &&
                width != null &&
                size != null &&
                duration != null &&
                userId != null &&
                !StringUtils.isEmpty(url);
    }

    public Creative convertToEntity() {

        Creative creative = new Creative();
        creative.setName(name);
        creative.setType(type);
        creative.setMaterialType(materialType);
        creative.setHeight(height);
        creative.setWidth(width);
        creative.setSize(size);
        creative.setAuditStatus(duration);
        creative.setAuditStatus(CommonStatus.VALID.getStatus());
        creative.setUserId(userId);
        creative.setUrl(url);
        creative.setCreateTime(new Date());
        creative.setUpdateTime(creative.getCreateTime());

        return creative;
    }
}
