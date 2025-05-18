import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JUnitTest {

    @Mock
    EntityManager em;
    EntityTransaction tx = Mockito.mock(EntityTransaction.class);

    @Test
    public void exampleTest(){
        //write test here
    }
}
