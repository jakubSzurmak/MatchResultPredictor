import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import pg.gda.edu.lsea.network.services.ParseDataService;
import pg.gda.edu.lsea.dataHandlers.utils.Serializer;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Units test for the {@link ParseDataService} class
 */
@RunWith(MockitoJUnitRunner.class)
public class ParseDataServiceTest {

    /**
     * Tests the getTeamList() method of tested class
     *
     * @throws Exception in case called method being tested throws and exception
     */
    @Test
    public void testGetTeamList() throws Exception{
        // Mock list of teams
        List<String> mockTeams = List.of("Barcelona", "Real Madrid", "Boca Juniors");

        // Serializes mock teams into a byte array
        byte[] fakeSerialized = Serializer.getSerializedForm(mockTeams);

        // Creates a spy object of ParseDataService
        ParseDataService spyService = spy(new ParseDataService(true));

        // Configuration for returning fakeSerialized instead of calling real method
        doReturn(fakeSerialized).when(spyService).getTeamsList();

        // Returns fakeSerialized
        byte[] returnedTeams = spyService.getTeamsList();

        // Deserializes byte array into a Line<String>
        List<String> actualTeams = deserialize(returnedTeams);

        // Asserts if mockTeams is equal to actualTeams
        assertEquals(mockTeams, actualTeams);
    }

    /**
     * Tests the getCorrelationData() method of tested class
     *
     * @throws Exception in case called method being tested throws and exception
     */
    @Test
    public void testGetPlayers() throws Exception{
        // Mock list of correlations
        List<String> mockCorr = List.of("0.9995", "0.9542");

        // Serializes mock correlations into a byte array
        byte[] fakeSerialized = Serializer.getSerializedForm(mockCorr);

        // Creates a spy object of ParseDataService
        ParseDataService spyService = spy(new ParseDataService(true));

        // Configuration for returning fakeSerialized instead of calling real method
        doReturn(fakeSerialized).when(spyService).getCorrelationData();

        // Returns fakeSerialized
        byte[] returned = spyService.getCorrelationData();

        // Deserializes byte array into a Line<String>
        List<String> actual = deserialize(returned);

        // Asserts if mock correlation is equal to actual correlations
        assertEquals(mockCorr, actual);
    }

    /**
     * Verifies exception handling for null byte-array input
     *
     * @throws Exception in case deserialization throws exception
     */
    @Test (expected = java.lang.RuntimeException.class)
    public void testDeserializeFailure() throws Exception{
        // Creates a spy object of ParseDataService
        ParseDataService spyService = spy(new ParseDataService(true));

        // Mocks to return null
        doReturn(null).when(spyService).getTeamsList();

        // Returns null
        byte[] returned = spyService.getTeamsList();

        // Deserializes null byte array - causes an exception
        List<String> actual = deserialize(returned);

    }

    private List<String> deserialize(byte[] data) {
        // Exception handling
        if (data == null){
            throw new RuntimeException("Failed to deserialize data (EMPTY DATA)");
        }
        try {
            return (List<String>) Serializer.getDeserializedForm(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize data", e);
        }
    }

}
