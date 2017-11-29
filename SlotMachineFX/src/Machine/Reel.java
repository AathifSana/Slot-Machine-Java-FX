package Machine;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Reel extends Thread {

    /**
     * Random object to get random int values
     */
    private Random r = new Random();
    /**
     * Symbol Array to Represent the reel
     */
    volatile Symbol[] reel;

    Reel(){
        createReel();
    }

    /**
     * Initialize reel
     */
    private void createReel() {
        reel = new Symbol[]{ new Symbol("images/bell.png",6)
                , new Symbol("images/cherry.png", 2)
                , new Symbol("images/lemon.png", 3)
                , new Symbol("images/plum.png", 4)
                , new Symbol("images/redseven.png", 7)
                , new Symbol("images/watermelon.png", 5)
        };
    }


    /**
     * Method to Spin the reels
     * @return the Symbol Array
     */
    Symbol[] spin(){
        Collections.shuffle(Arrays.asList(this.reel));
        return this.reel;
    }

    /**
     * Method to get a Random Image to display at the beginning at the game
     * @return
     */
    Symbol getStartingSymbol(){
        return reel[r.nextInt(reel.length)];
    }

}
