import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pg.gda.edu.lsea.absStatistics.Statistics;
import pg.gda.edu.lsea.absStatistics.implStatistics.TeamStatistics;
import pg.gda.edu.lsea.database.DbManager;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.prediction.MatchPrediction;
import pg.gda.edu.lsea.team.Team;
import weka.classifiers.functions.Logistic;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MatchPredictionTest {

    @Mock
    private DbManager mockDbManager;

    private List<Match> mockMatches;
    private List<Team> mockTeams;
    private Map<UUID, Statistics> mockStatistics;
    private Logistic mockModel;
    private Instances mockDatasetStructure;

    private Team team1;
    private Team team2;
    private TeamStatistics stats1;
    private TeamStatistics stats2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        DbManager.setInstance(mockDbManager);


        mockMatches = new ArrayList<>();
        mockTeams = new ArrayList<>();
        mockStatistics = new HashMap<>();
        mockModel = mock(Logistic.class);

        ArrayList<Attribute> attributes = defineAttributes();
        mockDatasetStructure = new Instances("MatchPrediction", attributes, 0);
        mockDatasetStructure.setClassIndex(mockDatasetStructure.numAttributes() - 1);


        team1 = new Team(UUID.randomUUID());
        team1.setName("Barcelona");

        team2 = new Team(UUID.randomUUID());
        team2.setName("Real Madrid");

        mockTeams.add(team1);
        mockTeams.add(team2);

        stats1 = new TeamStatistics(team1.getId());
        stats1.setGoalsScored(45);
        stats1.setGamesPlayed(20);
        stats1.setGamesWon(15);
        stats1.setTotalCleanSheets(10);
        stats1.setTotalGoalConceded(12);
        stats1.setGoalPerc();
        stats1.setCleanSheetPerc();
        stats1.setWinPerc();


        stats2 = new TeamStatistics(team2.getId());
        stats2.setGoalsScored(38);
        stats2.setGamesPlayed(20);
        stats2.setGamesWon(13);
        stats2.setTotalCleanSheets(8);
        stats2.setTotalGoalConceded(15);
        stats2.setGoalPerc();
        stats2.setCleanSheetPerc();
        stats2.setWinPerc();

        mockStatistics.put(team1.getId(), stats1);
        mockStatistics.put(team2.getId(), stats2);


    }


    private ArrayList<Attribute> defineAttributes(){
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("team1_winPercentage"));
        attributes.add(new Attribute("team1_totalGoals"));
        attributes.add(new Attribute("team1_totalMatches"));
        attributes.add(new Attribute("team1_totalWinMatches"));
        attributes.add(new Attribute("team1_totalCleanSheets"));
        attributes.add(new Attribute("team1_totalGoalsConceded"));
        attributes.add(new Attribute("team1_goalPercentage"));
        attributes.add(new Attribute("team1_cleanSheetPercentage"));

        attributes.add(new Attribute("team2_winPercentage"));
        attributes.add(new Attribute("team2_totalGoals"));
        attributes.add(new Attribute("team2_totalMatches"));
        attributes.add(new Attribute("team2_totalWinMatches"));
        attributes.add(new Attribute("team2_totalCleanSheets"));
        attributes.add(new Attribute("team2_totalGoalsConceded"));
        attributes.add(new Attribute("team2_goalPercentage"));
        attributes.add(new Attribute("team2_cleanSheetPercentage"));

        List<String> classValues = List.of("Team1 win", "Team2 win");
        attributes.add(new Attribute("result", classValues));

        return attributes;
    }



    @Test
    public void testPredictMatch_ValidInputs_ReturnsPrediction() throws Exception {

        double[] probabilities = {0.6, 0.4};
        when(mockModel.distributionForInstance(any(Instance.class))).thenReturn(probabilities);


        when(mockDbManager.getValueFromColumn(eq("Barcelona"), eq(Team.class), eq("name"))).thenReturn(team1);
        when(mockDbManager.getValueFromColumn(eq("Real Madrid"), eq(Team.class), eq("name"))).thenReturn(team2);
        when(mockDbManager.getTableById(eq(team1.getId()), eq(TeamStatistics.class))).thenReturn(stats1);
        when(mockDbManager.getTableById(eq(team2.getId()), eq(TeamStatistics.class))).thenReturn(stats2);


        //Act
        String result = MatchPrediction.predictMatch("Barcelona", "Real Madrid", mockTeams, mockModel, mockStatistics, mockDatasetStructure);

        //Assert
        assertTrue(result.contains("Probability of winning Barcelona: 0.6"));
        assertTrue(result.contains("Probability of winning Real Madrid: 0.4"));

        verify(mockDbManager).getValueFromColumn(eq("Barcelona"), eq(Team.class), eq("name"));
        verify(mockDbManager).getValueFromColumn(eq("Real Madrid"), eq(Team.class), eq("name"));
        verify(mockDbManager).getTableById(eq(team1.getId()), eq(TeamStatistics.class));
        verify(mockDbManager).getTableById(eq(team2.getId()), eq(TeamStatistics.class));

    }

    @Test
    public void testPredictMatch_InvalidTeamName_ReturnsErrorMessage() throws Exception {

        when(mockDbManager.getValueFromColumn(eq("NonExistentTeam"), eq(Team.class), eq("name"))).thenReturn(null);

        //Act
        String result = MatchPrediction.predictMatch("NonExistentTeam", "Real Madrid", mockTeams, mockModel, mockStatistics, mockDatasetStructure);

        //Assert
        assertEquals("Wrong team", result);

        verify(mockDbManager).getValueFromColumn(eq("NonExistentTeam"), eq(Team.class), eq("name"));

    }

    @Test
    public void testPredictMatch_MissingStatistics_ReturnsErrorMessage() throws Exception {

        when(mockDbManager.getValueFromColumn(eq("Barcelona"), eq(Team.class), eq("name"))).thenReturn(team1);
        when(mockDbManager.getValueFromColumn(eq("Real Madrid"), eq(Team.class), eq("name"))).thenReturn(team2);
        when(mockDbManager.getTableById(eq(team1.getId()), eq(TeamStatistics.class))).thenReturn(null);

        //Act
        String result = MatchPrediction.predictMatch("Barcelona", "Real Madrid", mockTeams, mockModel, mockStatistics, mockDatasetStructure);

        assertEquals("Cannot get stats", result);

        verify(mockDbManager).getValueFromColumn(eq("Barcelona"), eq(Team.class), eq("name"));
        verify(mockDbManager).getValueFromColumn(eq("Real Madrid"), eq(Team.class), eq("name"));
        verify(mockDbManager).getTableById(eq(team1.getId()), eq(TeamStatistics.class));

    }


    @Test
    public void testTrainModel_ValidInputs_ReturnsModel()  throws Exception {
        List<Object[]> matchesFromDb = createMockMatchesData();


        when(mockDbManager.getFromDB(eq("matches"), eq("all"), eq("all"))).thenReturn(matchesFromDb);
        when(mockDbManager.getTableById(any(UUID.class), eq(TeamStatistics.class))).thenReturn(stats1);

        //Act
        Logistic model = MatchPrediction.trainModel(mockMatches, mockStatistics);

        //Assert
        assertNotNull(model);

        verify(mockDbManager).getFromDB(eq("matches"), eq("all"), eq("all"));
        verify(mockDbManager, atLeastOnce()).getTableById(any(UUID.class), eq(TeamStatistics.class));

    }

    private List<Object[]> createMockMatchesData() {
        List<Object[]> matchesData = new ArrayList<>();

        Object[] match1 = new Object[5];
        match1[1] = 1;
        match1[2] = 3;
        match1[3] = team2.getId().toString();
        match1[4] = team1.getId().toString();

        Object[] match2 = new Object[5];
        match2[1] = 2;
        match2[2] = 0;
        match2[3] = team1.getId().toString();
        match2[4] = team2.getId().toString();

        matchesData.add(match1);
        matchesData.add(match2);

        return matchesData;
    }

}
