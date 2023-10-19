package smart.housing;

import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
        URL url = Main.class.getResource("views/login_page.fxml");

        try {
            URL url2 = new URL("smart/housing/views/login_page.fxml");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
