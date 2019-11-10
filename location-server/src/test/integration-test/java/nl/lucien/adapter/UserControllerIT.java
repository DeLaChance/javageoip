package nl.lucien.adapter;

import nl.lucien.BaseIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class UserControllerIT extends BaseIntegrationTest {

    @Test
    public void test() {
        System.out.println("Run test");
        assertTrue(true);
    }
}
