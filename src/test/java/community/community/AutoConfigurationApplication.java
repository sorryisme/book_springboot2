package community.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest

public class AutoConfigurationApplication {

    Logger LOG = LoggerFactory.getLogger(getClass());

    @Value("${property.test.name}")
    private String propertyTestName;

    @Value("${propertyTest}")
    private String propertyTest;

    @Value("${noKey:default value}")
    private String defaultValue;

    @Value("${propertyTestList}")
    private String[] propertyTestArray;

    @Value("#{'${propertyTestList}'.split(',')}")
    private List<String> propertyTestList;



    @Test
    public void testValue(){
        LOG.info("---------------");
        LOG.info(propertyTestName);
        LOG.info(propertyTest);
        LOG.info(defaultValue);
        LOG.info(propertyTestArray[0]);
        LOG.info(propertyTestList.get(0));

    }

}
