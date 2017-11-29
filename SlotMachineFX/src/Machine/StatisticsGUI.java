package Machine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public final class StatisticsGUI {

    private Stage stage;
    private GridPane layout;

    /**
     * Constructor for the GUI
     * creates a new Stage
     * @param controller - controller object withe all the updated variables
     */
    public StatisticsGUI(Controller controller ){
        this.stage = new Stage();
        setGUI( controller );
    }

    /**
     * Setting the GUI  and adding the components
     * css/style.css for additional styling
     * making the class singleton
     * Creating 12 colums and rows for the Gridlayout to adjust the responsiveness and position
     * Creating a pie chart with wins and losses in the controller object
     * winLabel, lossLabel, netCreditsLabel, gamesLabel to display the wins, losses , netcredits and no of games respectively
     * Savebtn to save the statistics as a text file
     * @param controller - controller object withe all the updated variables
     */
    private void setGUI(Controller controller ) {
        layout = new GridPane();
        layout.setPadding(new Insets(5,5,5,5));

        for (int column = 0; column < 12; column++) {
            ColumnConstraints c = new ColumnConstraints();
            c.setHgrow(Priority.SOMETIMES);
            c.setMinWidth(10.00);
            c.setPrefWidth(100.00);
            layout.getColumnConstraints().add(c);
        }

        for (int row = 0; row < 12; row++) {
            RowConstraints c = new RowConstraints();
            c.setVgrow(Priority.SOMETIMES);
            c.setMinHeight(10.00);
            c.setPrefHeight(30.00);
            layout.getRowConstraints().add(c);
        }

        PieChart.Data p = new PieChart.Data("Wins", controller.getWins());
        PieChart.Data p1 = new PieChart.Data("Loses", controller.getLooses());

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(p,p1);

        final PieChart chart = new PieChart(pieChartData);
        chart.setId("chart");
        chart.setLabelLineLength(15);
        chart.setLegendSide(Side.LEFT);
        GridPane.setConstraints(chart, 1, 0, 10, 8);

        Label winlbl = new Label("Wins \n" + controller.getWins());
        winlbl.setId("StatsLabels");
        winlbl.setPrefSize(350, 100);
        GridPane.setConstraints(winlbl, 1 ,7 , 3, 2);

        Label losslbl = new Label("Losses \n" + controller.getLooses());
        losslbl.setId("StatsLabels");
        losslbl.setPrefSize(350, 100);
        GridPane.setConstraints(losslbl, 1 ,10 , 3, 2);

        Label gameslbl = new Label("Games \n" + controller.getNumberOfGames());
        gameslbl.setId("StatsLabels");
        gameslbl.setPrefSize(350, 100);
        GridPane.setConstraints(gameslbl, 8 ,7 , 3, 2);

        Label netcreditslbl = new Label("Net Credits per game\n" + controller.getAverageCreditsNetted()/controller.getNumberOfGames());
        netcreditslbl.setId("StatsLabels");
        netcreditslbl.setStyle("-fx-font-size: 15px;");
        netcreditslbl.setPrefSize(350, 100);
        GridPane.setConstraints(netcreditslbl, 8 ,10 , 4, 2);

        Button Savebtn = new Button("SAVE");
        Savebtn.setId("buttons");
        GridPane.setConstraints(Savebtn, 5 ,9 , 2, 1);

        /**
         * Action Performance for the save button
         */
        Savebtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                saveStatistics(controller.getNumberOfGames() , controller.getWins() , controller.getLooses(),
                        controller.getAverageCreditsNetted()/controller.getNumberOfGames());
            }
        });

        layout.getChildren().addAll(chart , winlbl , losslbl , gameslbl , netcreditslbl,Savebtn);

        Scene scene = new Scene(this.layout, 600, 600);
        scene.getStylesheets().add("css/styles.css");

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Statistics");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

    }

    /**
     * Method to Save the Statistics to a text file
     * Get the current date and time
     * format it in SimpleDateFormat and set it as the file name
     * Printing the number of games, number of wins, number of loses, Average net credits to the text file through FileWriter
     * Display alert popup after file has been saved successfully
     * If exception occurs, catch it and display error pop up
     * finally close the filewriter.
     * @param numberOfGames
     * @param wins
     * @param looses
     * @param avgCreditsPerGame
     */
    private void saveStatistics(int numberOfGames, int wins, int looses, double avgCreditsPerGame){

        Date dateTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy--HH.mm.ss");
        String fileName = sdf.format(dateTime);
        String msg = "Number of Games : " + numberOfGames + "\nNumber of Wins : " + wins +
                "\nNumber of Loses : " + looses + "\nAverage Net Credits Per Game : " + avgCreditsPerGame;

        File f = new File(fileName+".txt");
        FileWriter fw = null;

        try{
            fw = new FileWriter(f);
            fw.write(msg);

            Controller.popUp("Done" , "File saved successfully." , Alert.AlertType.INFORMATION);

        }catch (IOException e){
            Controller.popUp("Error" , "Error in Saving!!!" , Alert.AlertType.ERROR);
        }finally {
            try {
                fw.close();
            } catch (IOException e) {
                Controller.popUp("Error" , "Error in closing the File Writer" , Alert.AlertType.ERROR);
            }
        }
    }

}
