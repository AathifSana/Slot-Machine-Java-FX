package Machine;

public interface ISymbol {

    /**
     *Method to set the Image
     * @param  img: Path for the Image
     */
    void setImage(String img);

    /**
     * Method to get the Image
     * @return the path of the image
     */
    String getImage();

    /**
     * Method to set the value to an Image
     * @param val : value for the image
     */
    void setValue(int val);

    /**
     * Method to get the value of the image
     * @return the value
     */
    int getValue();

}
