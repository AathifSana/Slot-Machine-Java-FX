package Machine; /**
 * Import necessary packages
 */

public final class Symbol implements ISymbol{

    /**
     * private fields
     */
    private String imagePath;
    private int value;

    /**
     * Constructor to create the a Symbol
     * @param imagePath : String path to image
     * @param value : points assigned to the image
     */
    public Symbol(String imagePath, int value) {
        setImage(imagePath);
        setValue(value);
    }

    /**
     * Method to set the Image
     * @param  img: Path for the Image
     */
    @Override
    public void setImage(String img) {
        imagePath = img;
    }

    /**
     * method to get the Image
     * @return the image path
     */
    @Override
    public String getImage() {
        return imagePath;
    }

    /**
     * Method to set the Value for the image
     * @param val : value for the image
     */
    @Override
    public void setValue(int val) {
        value = val;
    }

    /**
     * Method to get the value of the image
     * @return value of the image
     */
    @Override
    public int getValue() {
        return value;
    }

    /**
     * Method to check the equality of 2 images
     * @param a : Symbol object
     * @return boolean
     */
    public boolean checkEquality(Symbol a){
        return this.value == a.value;
    }
}
