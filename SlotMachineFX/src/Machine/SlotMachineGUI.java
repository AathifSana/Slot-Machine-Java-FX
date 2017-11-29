package Machine;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.scene.paint.Color;


final class SlotMachineGUI {

    /**
     * private , volatile objects and variables for the GUI
     */
    private GridPane layout;

    private Button btnBetOne, btnBetMax, btnAddCoin, btnSpin, btnReset, btnStats;

    private Label r1_Label, r2_Label, r3_Label, labelHeading, labelStatus,
            labelCreditAmount, labelBetArea, labelBetAmount, labelCreditArea;

    private volatile ImageView r1_ImageView, r2_ImageView, r3_ImageView;

    private Controller controller = new Controller();

    /**
     * Constructor for the SlotMachine window
     * Setting the layout with padding and 16 colums , 14 rows for the responsiveness and positioning
     * setting Labels
     * setting Images
     * setting Buttons
     * setting Actions for the buttons and Labels
     * getting additional styles from css/styles.css
     * @param primaryStage - Stage
     */
    SlotMachineGUI(Stage primaryStage) {

        layout = new GridPane();
        layout.setPadding(new Insets(5, 5, 5, 5));

        for (int column = 0; column < 16; column++) {
            ColumnConstraints c = new ColumnConstraints();
            c.setHgrow(Priority.SOMETIMES);
            c.setMinWidth(10.00);
            c.setPrefWidth(100.00);
            layout.getColumnConstraints().add(c);
        }

        for (int row = 0; row < 14; row++) {
            RowConstraints c = new RowConstraints();
            c.setVgrow(Priority.SOMETIMES);
            c.setMinHeight(10.00);
            c.setPrefHeight(30.00);
            layout.getRowConstraints().add(c);
        }

        setImages();
        setLabels();
        setButtons();
        setActions();

        this.layout.getChildren().addAll(r1_Label, r2_Label, r3_Label, labelHeading, labelStatus,
                btnBetOne, btnBetMax, btnAddCoin, btnSpin, btnReset, btnStats, labelCreditArea, labelCreditAmount,
                labelBetArea, labelBetAmount);

        primaryStage.setTitle("SLOT MACHINE");
        Scene scene = new Scene(this.layout, 800, 600);
        scene.getStylesheets().addAll("css/styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Method to add all the actions for the buttons and Labels
     */
    private void setActions() {

        /**
         * On mouse Clicked event on Spin button
         * if no credits betted, display required message
         * if not, disable all the buttons except the statistics
         * setting isAlreadySpinnig boolean variable to true
         * Create 3 new threads and setDaemon to true because threads has to be stopped if the GUI closed
         * override the run method
         * start the threads
         */
        btnSpin.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (controller.getBettingCredits() == 0) {
                    labelStatus.setText("You have not Betted any Credits");
                    labelStatus.setTextFill(Color.WHITE);
                } else {

                    btnAddCoin.setDisable(true);
                    btnBetMax.setDisable(true);
                    btnBetOne.setDisable(true);
                    btnReset.setDisable(true);
                    btnSpin.setDisable(true);

                    controller.setAlreadySpinning(true);
                    labelStatus.setText("Spinning !!!");
                    labelStatus.setTextFill(Color.WHITE);

                    controller.reel1 = new Reel(){
                        public void run() {
                            spinReels(controller.reel1, r1_ImageView, 1);
                        }
                    };
                    controller.reel2 = new Reel(){
                        public void run() {
                            spinReels(controller.reel2, r2_ImageView, 2);
                        }
                    };
                    controller.reel3 = new Reel(){
                        public void run() {
                            spinReels(controller.reel3, r3_ImageView, 3);
                        }
                    };

                    controller.reel1.setDaemon(true);
                    controller.reel2.setDaemon(true);
                    controller.reel3.setDaemon(true);

                    controller.reel1.start();
                    controller.reel2.start();
                    controller.reel3.start();
                }
            }
        });

        /**
         * On Mouse Clicked event on 3 reel Labels
         * calls the stop spin metho
         * sends the necessary variable as arguments
         */
        r1_Label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stopSpin();
            }
        });

        r2_Label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stopSpin();
            }
        });

        r3_Label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stopSpin();
            }
        });

        /**
         * On mouse Clicked event on Bet One button
         * if no credits remaining, display required message
         * else call the betOne method in controller object
         * update values in the Labels
         */
        btnBetOne.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (controller.getCredits() == 0) {
                    labelStatus.setText("You do not have enough credits");
                    labelStatus.setTextFill(Color.WHITE);
                } else {
                    controller.betOne();
                    labelStatus.setText("You Bet 1 more Credit");
                    labelStatus.setTextFill(Color.WHITE);
                    labelBetAmount.setText(controller.getBettingCredits() + "");
                    labelCreditAmount.setText(controller.getCredits() + "");
                }
            }

        });

        /**
         * On mouse Clicked event on Bet Max button
         * if credits is less than 3, display required messege
         * if max once is already pressed , display required message
         * else call the betMax method in controller object
         */
        btnBetMax.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (controller.getCredits() >= controller.getMAXIMUM_BETTING_CREDITS()) {
                    if (controller.isMaximumBetted()) {
                        Controller.popUp("Error" , "You can only Bet Max once" , Alert.AlertType.ERROR);
                    } else {
                        controller.betMax();
                        labelStatus.setText("You Betted the Maximum Credits");
                        labelStatus.setTextFill(Color.WHITE);
                        labelBetAmount.setText(controller.getBettingCredits() + "");
                        labelCreditAmount.setText(controller.getCredits() + "");
                    }
                } else {
                    Controller.popUp("Error" , "You do not have enough credits" , Alert.AlertType.ERROR);
                }
            }
        });

        /**
         * On mouse Clicked event on AddCoin button
         * call the betOne method in controller object
         */
        btnAddCoin.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                controller.addCoin();
                labelStatus.setText("You Added 1 Coin");
                labelStatus.setTextFill(Color.WHITE);
                labelBetAmount.setText(controller.getBettingCredits() + "");
                labelCreditAmount.setText(controller.getCredits() + "");

            }
        });

        /**
         * On mouse Clicked event on Reset button
         * if no bets placed, display required message
         * else call the reset method in controller object
         */
        btnReset.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (controller.getBettingCredits() == 0) {
                    labelStatus.setText("You have not Betted any Credits");
                    labelStatus.setTextFill(Color.WHITE);
                } else {
                    controller.reset();
                    labelStatus.setText("You Reset the Betting");
                    labelStatus.setTextFill(Color.WHITE);
                    labelBetAmount.setText(controller.getBettingCredits() + "");
                    labelCreditAmount.setText(controller.getCredits() + "");
                }
            }
        });

        /**
         * On mouse Clicked event on Stats button
         * if no games have played display required message
         * esle call open new Object in StatisticGUI
         */
        btnStats.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (controller.getNumberOfGames() == 0) {
                    labelStatus.setText("No Games Have Played");
                    labelStatus.setTextFill(Color.WHITE);
                } else {
                    controller.setStatisticsOpen(true);
                    new StatisticsGUI(controller);
                }
            }

        });

    }

    /**
     * Method to stop the reels from spinning
     * set boolean alreadySpinning to false
     * call the update method in controller object
     * enabling all the buttons
     * display updated values
     */
    private void stopSpin(){
        if (controller.isAlreadySpinning()) {
            controller.setAlreadySpinning(false);
            controller.update();

            labelBetAmount.setText(controller.getBettingCredits() + "");
            labelCreditAmount.setText(controller.getCredits() + "");
            labelStatus.setText(controller.getDisplayMessege());

            btnAddCoin.setDisable(false);
            btnBetMax.setDisable(false);
            btnBetOne.setDisable(false);
            btnReset.setDisable(false);
            btnSpin.setDisable(false);

        }

    }

    /**
     * Method to change the images while spinning
     * Get the shuffled array in reel object
     * while isSpinning boolean valraible true , change the images according to the index of the array
     * sleep the threads for 80 milliseconds
     * @param reel - reel object
     * @param iv - imageView
     * @param x - value to check the reels
     */
    private void spinReels(Reel reel , ImageView iv , int x){

        reel.reel = reel.spin();

        while(controller.isAlreadySpinning()){

            for(Symbol s : reel.reel){

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(controller.isAlreadySpinning()) {
                                iv.setImage(new Image(s.getImage()));
                                switch (x) {
                                    case 1:
                                        controller.symbolReel1 = s;
                                        break;
                                    case 2:
                                        controller.symbolReel2 = s;
                                        break;
                                    case 3:
                                        controller.symbolReel3 = s;
                                        break;
                                }
                            }
                        }
                    });
                    try {
                        Reel.sleep(80);
                    } catch (InterruptedException e) {
                        Controller.popUp("Error" , "Error while pausing threads" , Alert.AlertType.ERROR);
                    }

            }

        }
    }

    /**
     * Method to set the labels and positions them and set Images to them
     */
    private void setLabels() {

        this.r1_Label = new Label();
        r1_Label.setId("reel");
        GridPane.setConstraints(r1_Label, 1, 4, 4, 4);
        r1_Label.setGraphic(r1_ImageView); //uncomment this after implementation

        this.r2_Label = new Label();
        r2_Label.setId("reel");
        GridPane.setConstraints(r2_Label, 6, 4, 4, 4);
        r2_Label.setGraphic(r2_ImageView); //uncomment this after implementation

        this.r3_Label = new Label();
        r3_Label.setId("reel");
        GridPane.setConstraints(r3_Label, 11, 4, 4, 4);
        r3_Label.setGraphic(r3_ImageView); //uncomment this after implementation

        this.labelHeading = new Label("BET AND WIN");
        labelHeading.setId("labelHeading");
        labelHeading.setPrefSize(800, 60);
        GridPane.setConstraints(labelHeading, 3, 0, 10, 2);

        this.labelStatus = new Label("Lets Play!!!!");
        labelStatus.setId("labelResult");
        GridPane.setConstraints(labelStatus, 1, 8, 14, 2);

        this.labelCreditArea = new Label("CREDIT AREA");
        labelCreditArea.setId("labelBet_Credit");
        labelCreditArea.setStyle("-fx-text-fill: white;");
        labelCreditArea.setPrefSize(350, 65);
        GridPane.setConstraints(labelCreditArea, 1, 2, 3, 1);

        this.labelCreditAmount = new Label(controller.getCredits() + "");
        labelCreditAmount.setId("labelBet_Credit");
        labelCreditAmount.setStyle("-fx-background-color: #ffffff;");
        labelCreditAmount.setPrefSize(350, 65);
        GridPane.setConstraints(labelCreditAmount, 1, 3, 3, 1);

        this.labelBetArea = new Label("BET AREA");
        labelBetArea.setId("labelBet_Credit");
        labelBetArea.setStyle("-fx-text-fill: white;");
        labelBetArea.setPrefSize(350, 65);
        GridPane.setConstraints(labelBetArea, 12, 2, 3, 1);

        this.labelBetAmount = new Label(controller.getBettingCredits() + "");
        labelBetAmount.setId("labelBet_Credit");
        labelBetAmount.setStyle("-fx-background-color: #ffffff;");
        labelBetAmount.setPrefSize(350, 65);
        GridPane.setConstraints(labelBetAmount, 12, 3, 3, 1);

    }

    /**
     * Method to set the buttons and positions them
     */
    private void setButtons() {

        this.btnBetOne = new Button("BET ONE");
        this.btnBetOne.setId("buttons");
        //this.btnBetOne.setPrefSize(400, 70);
        GridPane.setConstraints(btnBetOne, 1, 10, 3, 2);

        this.btnSpin = new Button("SPIN");
        this.btnSpin.setId("buttons");
        // this.btnSpin.setPrefSize(800, 70);
        GridPane.setConstraints(btnSpin, 5, 10, 6, 2);

        this.btnBetMax = new Button("BET MAX");
        this.btnBetMax.setId("buttons");
        this.btnBetMax.setPrefSize(400, 70);
        GridPane.setConstraints(btnBetMax, 12, 10, 3, 2);

        this.btnReset = new Button("RESET");
        this.btnReset.setId("buttons");
        // this.btnReset.setPrefSize(400, 70);
        GridPane.setConstraints(btnReset, 1, 12, 3, 2);

        this.btnAddCoin = new Button("ADD COIN");
        this.btnAddCoin.setId("buttons");
        // this.btnAddCoin.setPrefSize(800, 70);
        GridPane.setConstraints(btnAddCoin, 5, 12, 6, 2);

        this.btnStats = new Button("STATS");
        this.btnStats.setId("buttons");
        // this.btnStats.setPrefSize(400, 70);
        GridPane.setConstraints(btnStats, 12, 12, 3, 2);


    }

    /**
     * Method to set ImageViews and set width and height
     */
    private void setImages() {

        r1_ImageView = new ImageView(controller.reel1.getStartingSymbol().getImage());
        r1_ImageView.setFitHeight(150);
        r1_ImageView.setFitWidth(150);

        r2_ImageView = new ImageView(controller.reel1.getStartingSymbol().getImage());
        r2_ImageView.setFitHeight(150);
        r2_ImageView.setFitWidth(150);

        r3_ImageView = new ImageView(controller.reel1.getStartingSymbol().getImage());
        r3_ImageView.setFitHeight(150);
        r3_ImageView.setFitWidth(150);

    }

}
