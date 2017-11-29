package Machine;

import javafx.application.Application;
import javafx.stage.Stage;

public final class Main extends Application {


    /**
     * Starts the Program
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        new SlotMachineGUI(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
