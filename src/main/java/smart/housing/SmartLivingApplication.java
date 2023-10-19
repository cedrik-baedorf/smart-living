package smart.housing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smart.housing.controllers.LoginPageController;
import smart.housing.controllers.SmartHousingController;

import javax.persistence.EntityManager;
import java.io.IOException;

public class SmartLivingApplication extends Application {

    private final SmartHousingController START_CONTROLLER = new LoginPageController(this);

    private EntityManager entityManager;

    private final String
        START_VIEW = START_CONTROLLER.getViewName(),
        VIEW_DIR = "views/";

    private Stage stage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setMaximized(true);
        stage.setTitle("Smart Living Application");
        setRoot(START_VIEW, START_CONTROLLER);
        stage.show();
    }

    public void setRoot(String rootView, SmartHousingController controller) {
        double width = stage.getWidth();
        double height = stage.getHeight();
        FXMLLoader loader = createFXMLLoader(rootView);
        loader.setControllerFactory(c -> controller);
        Scene scene = createScene(loader);
        stage.setScene(scene);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setMaximized(stage.isMaximized());
    }

    private Scene createScene(FXMLLoader loader) {
        try {
            return new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException("Unable to load fxml file at location " + loader.getLocation());
        }
    }

    private FXMLLoader createFXMLLoader(String fxmlFile) {
        return new FXMLLoader(SmartLivingApplication.class.getResource(VIEW_DIR + fxmlFile));
    }

    public <T> T loadFXML(String fxmlFile, SmartHousingController controller) {
        FXMLLoader loader = createFXMLLoader(fxmlFile);
        try {
            loader.setControllerFactory(c -> controller);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load fxml file at location " + loader.getLocation());
        }
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
