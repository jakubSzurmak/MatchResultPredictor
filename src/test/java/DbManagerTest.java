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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.team.Team;
import pg.gda.edu.lsea.database.DbManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DbManagerTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

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
}