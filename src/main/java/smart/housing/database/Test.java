package smart.housing.database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Test {

    public static void main(String[] args) {
        File file = new File("src/main/resources/smart.housing/my.properties");
        if(! file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("user=fucker");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
