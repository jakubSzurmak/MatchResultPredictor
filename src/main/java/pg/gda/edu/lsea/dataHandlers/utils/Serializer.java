package pg.gda.edu.lsea.dataHandlers.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.io.ObjectInputStream;


/**
 * Serializer class, holder for all methods concerning de/serializaion of objects used in the process of
 * communicating in between client and server
 */
public class Serializer {

    /**
     * Basic serialization for string collections based on list, used for program result reporting to the user
     * @param inputStream collection implementing the List Interface that is to be serialized
     */
    public static byte[] getSerializedForm(List<String> inputStream) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
        objectOutputStream.writeObject(inputStream);
        return byteOutputStream.toByteArray();
    }

    /**
     * Basic deserialization for presenting program output that was sent from server to client(user)
     * @param inputStream byte array that is to be deserialized for user view
     */
    public static List<String> getDeserializedForm(byte[] inputStream) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(inputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
        return (List<String>)objectInputStream.readObject();
    }

}
