package ltd.petpal.mall.common;

public class PetPalMallException extends RuntimeException {

    public PetPalMallException() {
    }

    public PetPalMallException(String message) {
        super(message);
    }

    /**throw new PetPalMallException
     *
     *
     * @param message
     */
    public static void fail(String message) {
        throw new PetPalMallException(message);
    }

}
