package ${basePackageName}.${projectName}.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(org.springframework.test.context.junit4.SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-test.xml" ,"classpath:spring/applicationContext-dao.xml" })
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class DefaultSampleDAOTest {
//    @Autowired
//    private SampleDAO sampleDAO;

//    @Test
//    public void test_sampleMethod() {
//      sampleDAO.sampleMethod();
//    }
}
