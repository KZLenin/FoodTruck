import javax.swing.*;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private byte[] imageBytes; // Agregamos el arreglo de bytes de la imagen

    public Product(int id, String name, String description, double price, byte[] imageBytes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageBytes = imageBytes;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public byte[] getImageBytes() { return imageBytes; }

    public ImageIcon getImageIcon() {
        if (imageBytes != null) {
            return new ImageIcon(imageBytes); // Convertimos los bytes en un ImageIcon
        }
        return null;
    }
}
