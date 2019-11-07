package cn.luoweiying;

import cn.luoweiying.dao.unit_condition.AdUnitItRepository;
import cn.luoweiying.entity.unit_condition.AdUnitIt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationTest.class})
public class JpaTest {
    @Autowired
    AdUnitItRepository adUnitItRepository;

    @Test
    public void adUnitItSava() {
        AdUnitIt adUnitIt = new AdUnitIt(Long.valueOf(14), "测试2");
        adUnitItRepository.save(adUnitIt);
    }
}
