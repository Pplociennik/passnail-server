import com.passnail.server.core.app.config.AppConfig;
import com.passnail.server.data.service.UserServiceIf;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by: Pszemko at czwartek, 11.03.2021 17:54
 * Project: passnail-server
 */
@SpringBootTest(classes = {AppConfig.class})
public class SynchronizationServiceTest {

    @Autowired
    private UserServiceIf userService;



    @Test
    public void testSynchronization() {

    }
}
