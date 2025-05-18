import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.team.Team;
import pg.gda.edu.lsea.database.DbManager;
import pg.gda.edu.lsea.absStatistics.implStatistics.TeamStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class DbManagerTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;
    @Spy
    @InjectMocks
    private DbManager dbManager;

    private final UUID testId = UUID.randomUUID();
    private final Team testTeam = new Team(testId, "Test Team", null);
    private final Player testPlayer = new Player(testId);

    @Before
    public void setUp() {
        try {
            java.lang.reflect.Field field = DbManager.class.getDeclaredField("entityManagerFactory");
            field.setAccessible(true);
            EntityManagerFactory entityManagerFactory = mock(jakarta.persistence.EntityManagerFactory.class);
            when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
            field.set(null, entityManagerFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }

        when(entityManager.getTransaction()).thenReturn(transaction);
    }

    /**
     * Tests saving an entity to the database successfully
     */
    @Test
    public void testSaveToDb_Success() {
        // Arrange
        doNothing().when(entityManager).persist(any());

        // Act
        dbManager.saveToDb(testTeam);

        // Assert
        verify(transaction).begin();
        verify(entityManager).persist(testTeam);
        verify(transaction).commit();
        verify(entityManager).close();
    }

    /**
     * Tests saving an entity with exception handling
     */
    @Test
    public void testSaveToDb_HandlesException() {
        // Arrange
        doThrow(new RuntimeException("Test exception")).when(entityManager).persist(any());

        // Act
        dbManager.saveToDb(testTeam);

        // Assert
        verify(transaction).begin();
        verify(entityManager).persist(testTeam);
        verify(transaction, never()).commit();
        verify(entityManager).close();
    }

    /**
     * Tests retrieving an entity by ID
     */
    @Test
    public void testGetTableById_ReturnsCorrectEntity() {
        // Arrange
        when(entityManager.find(Team.class, testId)).thenReturn(testTeam);

        // Act
        Team result = dbManager.getTableById(testId, Team.class);

        // Assert
        assertEquals(testTeam, result);
        verify(entityManager).find(Team.class, testId);
    }

    /**
     * Tests the getValueFromColumn method returns correct entity
     */
    @Test
    public void testGetValueFromColumn_ReturnsCorrectEntity() {
        // Arrange
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<Team> criteriaQuery = mock(CriteriaQuery.class);
        Root<Team> root = mock(Root.class);
        TypedQuery<Team> typedQuery = mock(TypedQuery.class);
        List<Team> resultList = new ArrayList<>();
        resultList.add(testTeam);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Team.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Team.class)).thenReturn(root);
        when(criteriaQuery.where(any(Predicate.class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(resultList);

        // Act
        Team result = dbManager.getValueFromColumn("Test Team", Team.class, "name");

        // Assert
        assertEquals(testTeam, result);
    }

    /**
     * Tests the getValueFromColumn method returns null when no entity found
     */
    @Test
    public void testGetValueFromColumn_ReturnsNullWhenEmpty() {
        // Arrange
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<Team> criteriaQuery = mock(CriteriaQuery.class);
        Root<Team> root = mock(Root.class);
        TypedQuery<Team> typedQuery = mock(TypedQuery.class);
        List<Team> resultList = new ArrayList<>();

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Team.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Team.class)).thenReturn(root);
        when(criteriaQuery.where(any(Predicate.class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(resultList);

        // Act
        Team result = dbManager.getValueFromColumn("Nonexistent Team", Team.class, "name");

        // Assert
        assertNull(result);
    }

    /**
     * Tests getFromDB with "all" condition value
     */
    @Test
    public void testGetFromDB_WithAllCondition() {
        // Arrange
        jakarta.persistence.Query query = mock(jakarta.persistence.Query.class);
        List<Object[]> expectedResult = new ArrayList<>();
        when(entityManager.createNativeQuery("SELECT * FROM teams")).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedResult);

        // Act
        Object result = dbManager.getFromDB("teams", "name", "all");

        // Assert
        assertEquals(expectedResult, result);
    }
    /**
     * Tests getFromDB with specific condition value
     */
    @Test
    public void testGetFromDB_WithSpecificCondition() {
        // Arrange
        jakarta.persistence.Query query = mock(jakarta.persistence.Query.class);
        List<Object[]> expectedResult = new ArrayList<>();
        when(entityManager.createNativeQuery("SELECT * FROM teams WHERE name LIKE '%Test%'")).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedResult);

        // Act
        Object result = dbManager.getFromDB("teams", "name", "Test");

        // Assert
        assertEquals(expectedResult, result);
    }

    /**
     * Tests update in DB method executes the correct query
     */
    @Test
    public void testUpdateInDb_ExecutesCorrectQuery() {
        // Arrange
        jakarta.persistence.Query query = mock(jakarta.persistence.Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Act
        dbManager.updateInDb("teams", "name", "New Team", "id", "1234");

        // Assert
        verify(transaction).begin();
        verify(entityManager).createNativeQuery("UPDATE teams SET name = 'New Team' WHERE id LIKE '%1234%'");
        verify(query).executeUpdate();
        verify(transaction).commit();
    }

    /**
     * Tests update in DB with numeric value
     */
    @Test
    public void testUpdateInDb_WithNumericValue() {
        // Arrange
        jakarta.persistence.Query query = mock(jakarta.persistence.Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Act
        dbManager.updateInDb("teams", "score", "5", "id", "1234");

        // Assert
        verify(entityManager).createNativeQuery("UPDATE teams SET score = 5 WHERE id LIKE '%1234%'");
    }

    /**
     * Tests delete player from DB
     */
    @Test
    public void testDeleteFromDb_Player() {
        // Arrange
        Player player = mock(Player.class);
        Set<Team> teamSet = mock(Set.class);
        when(player.getTeamSet()).thenReturn(teamSet);
        doReturn(List.of(player)).when(dbManager).getFromDBJPQL(anyString(), anyString(), anyString());
        when(entityManager.contains(player)).thenReturn(true);
        when(entityManager.merge(player)).thenReturn(player);

        // Act
        dbManager.deleteFromDb("Player", "id", "1");

        // Assert
        verify(transaction).begin();
        verify(player.getTeamSet()).clear();
        verify(entityManager).merge(player);
        verify(entityManager).remove(player);
        verify(transaction).commit();
        verify(entityManager).close();
    }

    /**
     * Tests delete team from DB
     */
    @Test
    public void testDeleteFromDb_Team() {
        // Arrange
        Team team = mock(Team.class);
        Set<Player> playerSet = mock(Set.class);
        when(team.getPlayerSet()).thenReturn(playerSet);
        when(team.getId()).thenReturn(testId);
        doReturn(List.of(team)).when(dbManager).getFromDBJPQL(anyString(), anyString(), anyString());
        when(entityManager.contains(team)).thenReturn(true);
        when(entityManager.merge(team)).thenReturn(team);
        TypedQuery<Match> matchQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Match.class))).thenReturn(matchQuery);
        Match match = mock(Match.class);
        List<Match> matches = List.of(match);
        when(matchQuery.setParameter(eq("team"), eq(team))).thenReturn(matchQuery);
        when(matchQuery.getResultList()).thenReturn(matches);
        TeamStatistics stats = mock(TeamStatistics.class);
        when(entityManager.find(TeamStatistics.class, testId)).thenReturn(stats);

        // Act
        dbManager.deleteFromDb("Team", "id", testId.toString());

        // Assert
        verify(transaction).begin();
        verify(entityManager).remove(match);
        verify(entityManager).remove(stats);
        verify(playerSet).clear();
        verify(entityManager).merge(team);
        verify(entityManager).remove(team);
        verify(transaction).commit();
        verify(entityManager).close();
    }

    /**
     * Tests delete nonexistent entry from DB
     */
    @Test
    public void testDeleteFromDb_NoResults() throws Exception {
        // Arrange
        doReturn(List.of()).when(dbManager).getFromDBJPQL(anyString(), anyString(), anyString());

        // Act
        dbManager.deleteFromDb("Player", "id", "nonexistent");

        // Assert
        verify(transaction).begin();
        verify(transaction).commit();
        verify(entityManager, never()).remove(any());
        verify(entityManager).close();
    }
}