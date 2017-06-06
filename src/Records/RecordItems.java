package Records;

/**
 * Created by andrewcabrera on 5/31/17.
 */
public class RecordItems {
    private String quantity;
    private String year;
    private String variant;
    private String album;
    private String name;

    /**
     * @param quantity
     * @param year
     * @param variant
     * @param album
     */
    public RecordItems(String quantity, String year, String variant, String album) {
        this.quantity = quantity;
        this.year = year;
        this.variant = variant;
        this.album = album;
    }

    /**
     *
     * @return
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     *
     * @param quantity
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @return
     */
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
