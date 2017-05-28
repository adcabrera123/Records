package Records;

/**
 * Created by andrewcabrera on 5/27/17.
 */
public class ArtistName {
    private String name;
    private String quantity;

    public ArtistName(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void print() {
        System.out.println("Name: " + name);
        System.out.println("Quantity: " + quantity);
    }

}
