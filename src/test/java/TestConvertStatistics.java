import jakarta.persistence.Convert;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absStatistics.Statistics;
import pg.gda.edu.lsea.absStatistics.statisticHandlers.ConvertStatistics;
import pg.gda.edu.lsea.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TestConvertStatistics {



    // Mocking objects necessary for testing methods
    @Mock Map<UUID,Statistics> players;
    UUID playerId = UUID.randomUUID();
    @Mock Player currPlayer;



    ConvertStatistics convertStatistics;
    @Mock Map<UUID,Integer> goalScored;
    private UUID teamId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        convertStatistics = new ConvertStatistics();
        playerId = UUID.randomUUID();
    }

    /**
     * Tests the checkforShot method of tested class
     *
     * @throws Exception in case called method being tested throws and exception
     */
    @Test
    public void testCheckForShotAndGoal() throws Exception {

        Method methodToTest = ConvertStatistics.class.getDeclaredMethod("checkForShot", Map.class,
                UUID.class, Integer.class, Player.class);
        // Get access to private method
        methodToTest.setAccessible(true);

        // Act
        methodToTest.invoke(convertStatistics, players, playerId, 1, currPlayer);

        // Assert - void method, verify used
        verify(players).put(eq(playerId), any(Statistics.class));
    }


    /**
     * Tests the teamCleanSheet method of tested class - with a team in the map
     *
     * @throws Exception in case called method being tested throws and exception
     */
    @Test
    public void testCheckCleanSheetMethod_withTeamInTheMap() throws Exception {
        teamId = UUID.randomUUID();
        goalScored = new HashMap<>();
        goalScored.put(teamId, 0);
        goalScored.put(UUID.randomUUID(), 1);
        goalScored.put(UUID.randomUUID(), 2);

        Method m = ConvertStatistics.class
                .getDeclaredMethod("teamCleanSheet", Map.class, UUID.class);
        // Get access to private method
        m.setAccessible(true);

        // Act
        int cleanSheetFlag = (int) m.invoke(convertStatistics, goalScored, teamId);

        // Assert
        assertEquals(0, cleanSheetFlag);
    }

    /**
     * Tests the teamCleanSheet method of tested class - without a team in the map
     *
     * @throws Exception in case called method being tested throws and exception
     */
    @Test
    public void testCheckCleanSheetMethod_withoutTeamInTheMap() throws Exception {
        teamId = UUID.randomUUID();
        goalScored = new HashMap<>();
        goalScored.put(UUID.randomUUID(), 2);


        Method m = ConvertStatistics.class
                .getDeclaredMethod("teamCleanSheet", Map.class, UUID.class);
        // Get access to private method
        m.setAccessible(true);
        
        // Act
        Object cleanSheetFlag = m.invoke(convertStatistics, goalScored, teamId);

        // Assert
        assertNull(cleanSheetFlag);
    }

    /**
     * Tests the testGoalConceedByTeam method of tested class - with a team in the map
     *
     * @throws Exception in case called method being tested throws and exception
     */
    @Test
    public void testGoalConceedByTeam_withTeamInTheMap() throws Exception {
        teamId = UUID.randomUUID();
        goalScored = new HashMap<>();
        goalScored.put(teamId, 1);
        goalScored.put(UUID.randomUUID(), 2);
        goalScored.put(UUID.randomUUID(), 2);

        Method m = ConvertStatistics.class
                .getDeclaredMethod("goalConceedByTeam", Map.class, UUID.class);
        // Get access to private method
        m.setAccessible(true);

        // Act
        int cleanSheetFlag = (int) m.invoke(convertStatistics, goalScored, teamId);

        // Assert
        assertEquals(2, cleanSheetFlag);
    }

    /**
     * Tests the testGoalConceedByTeam method of tested class - without a team in the map
     *
     * @throws Exception in case called method being tested throws and exception
     */
    @Test
    public void testGoalConceedByTeam_withoutTeamInTheMap() throws Exception {
        teamId = UUID.randomUUID();
        goalScored = new HashMap<>();
        goalScored.put(UUID.randomUUID(), 1);
        Method m = ConvertStatistics.class
                .getDeclaredMethod("goalConceedByTeam", Map.class, UUID.class);
        // Get access to private method
        m.setAccessible(true);

        // Act
        int cleanSheetFlag = (int) m.invoke(convertStatistics, goalScored, teamId);

        // Assert
        assertEquals(0, cleanSheetFlag);
    }
}


