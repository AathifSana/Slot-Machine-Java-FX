package Machine;

import javafx.scene.control.Alert;

/**
 * final class to Control the functionalities in the slot machine
 */
public final class Controller {

    /**
     * private and default, volatile fields to control the slot machine
     */
    private final int GIVEN_CREDITS = 10;
    private final int MAXIMUM_BETTING_CREDITS = 3;
    private int credits = GIVEN_CREDITS;
    private int bettingCredits = 0;
    private int averageCreditsNetted , numberOfGames , wins , looses;
    private String displayMessege = null;
    private volatile boolean alreadySpinning;
    private boolean maximumBetted;
    private boolean statisticsOpen;
    private STATUS currentStatus;
    volatile Symbol symbolReel1 , symbolReel2 , symbolReel3;

    /**
     * enum to determine the Status
     */
    enum STATUS{
        WON,LOST
    }

    /**
     * 3 Reel objects to Start the machine
     */
    volatile Reel reel1 = new Reel();
    volatile Reel reel2 = new Reel();
    volatile Reel reel3 = new Reel();

    /**
     *
     * Getters and Setters for the private fields
     */
    public int getMAXIMUM_BETTING_CREDITS() { return MAXIMUM_BETTING_CREDITS; }

    public void setStatisticsOpen(boolean statisticsOpen) {
        this.statisticsOpen = statisticsOpen;
    }

    public int getCredits() {
        return credits;
    }

    public int getBettingCredits() {
        return bettingCredits;
    }

    public int getAverageCreditsNetted() {
        return averageCreditsNetted;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public int getWins() {
        return wins;
    }

    public int getLooses() {
        return looses;
    }

    public String getDisplayMessege() {
        return displayMessege;
    }

    public boolean isAlreadySpinning() { return alreadySpinning; }

    public void setAlreadySpinning(boolean alreadySpinning) {
        this.alreadySpinning = alreadySpinning;
    }

    public boolean isMaximumBetted() {
        return maximumBetted;
    }

    /**
     * Method to add 1 to Credit
     */
    public void addCoin(){
            credits++;
    }

    /**
     * Method to Reset the Bidding
     */
    public void reset(){
        credits += bettingCredits;
        bettingCredits = 0;
        maximumBetted = false;
    }

    /**
     * Method to Bet the maximum amount
     */
    public void betMax(){
        if(credits >= MAXIMUM_BETTING_CREDITS) {
            maximumBetted = true;
            bettingCredits += MAXIMUM_BETTING_CREDITS;
            credits -= MAXIMUM_BETTING_CREDITS;
        }
    }

    /**
     * Method to Bet one credit
     */
    public void betOne(){
        if(credits > 0) {
            credits -= 1;
            bettingCredits += 1;
        }
    }

    /**
     * Method to update the Variables and Check Equality of the Symbols after stopping the reels
     */
    public void update() {

        /**
         * Enabaling to re press the max button
         */
        if (maximumBetted) maximumBetted = false;

        /**
         * Checking if 2 symbols are equal
         */
        if (symbolReel1.checkEquality(symbolReel2) || symbolReel2.checkEquality(symbolReel3)) {

            /**
             * Round Win and Setting all the variables
             */
            wins++;
            numberOfGames++;
            int wonCredits = (bettingCredits * symbolReel2.getValue());
            credits += wonCredits;
            currentStatus = STATUS.WON;
            averageCreditsNetted += (wonCredits - bettingCredits);
            bettingCredits = 0;
            displayMessege = "You Won, " + wonCredits + " credits !!";

        /**
        * Checking if next symbols are equal
        */
        }else if(symbolReel3.checkEquality(symbolReel1)){

            /**
             * Round Win and Setting all the variables
             */
            wins++;
            numberOfGames++;
            int wonCredits = (bettingCredits * symbolReel3.getValue());
            credits += wonCredits;
            currentStatus = STATUS.WON;
            averageCreditsNetted +=( wonCredits - bettingCredits);
            bettingCredits = 0;
            displayMessege = "You Won, " + wonCredits + " credits !!";

        } else {

            /**
             * None of them are equal and Round Loss
             * Setting all the variables
             */
            looses++;
            numberOfGames++;
            averageCreditsNetted -= bettingCredits;
            bettingCredits = 0;
            displayMessege = "You Lost!!! ";
            currentStatus = STATUS.LOST;

        }
    }

    /**
     * Static method to display pop up boxes.
     * @param title - title for the Alert box
     * @param header - header message of the alert box
     * @param alertType - Type of the alert
     */
    public static void popUp(String title, String header, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}
