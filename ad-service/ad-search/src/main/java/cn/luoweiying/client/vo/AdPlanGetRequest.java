package cn.luoweiying.client.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanGetRequest {

    private Long id;
    private String planName;

}
