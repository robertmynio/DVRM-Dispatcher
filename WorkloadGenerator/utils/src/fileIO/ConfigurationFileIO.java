package fileIO;

import java.io.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: Jun 19, 2010
 * Time: 12:10:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationFileIO {
    private ConfigurationFileIO() {
    }

    public static void saveConfiguration(List<String[]> configuration, File file) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        outputStream.writeObject(configuration);
        outputStream.flush();
        outputStream.close();
    }

    public static List<String[]> loadConfiguration(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
        List<String[]> data = (List<String[]>) inputStream.readObject();
        inputStream.close();
        return data;
    }

    public static void saveGeneralConfig(Serializable o, File file) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        outputStream.writeObject(o);
        outputStream.flush();
        outputStream.close();
    }

    public static Object loadGeneralConfig(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
        return inputStream.readObject();
    }
}
