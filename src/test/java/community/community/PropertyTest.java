package community.community;

import community.community.pojo.FruitProperty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PropertyTest {

    Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    FruitProperty fruitProperty;

    @Test
    public void test(){
        List<Map> fruitData = fruitProperty.getList();
        fruitData.stream().forEach(map ->{
            map.forEach((o, o2) -> {
                LOG.info(o +":" +o2);
            });
        });

    }


}
