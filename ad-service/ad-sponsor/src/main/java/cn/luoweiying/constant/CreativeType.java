package cn.luoweiying.constant;

import lombok.Getter;

@Getter
public enum CreativeType {
    IMAGE(1, "图片"),
    VIDED(2, "视频"),
    TEXT(3, "文本");

    private Integer type;
    private String desc;

    CreativeType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
