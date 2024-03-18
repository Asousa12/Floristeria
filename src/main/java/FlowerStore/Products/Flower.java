package FlowerStore.Products;

import FlowerStore.Interfaces.GardenElements;

public class Flower implements GardenElements {

    private int idProduct;
    private int idType;
    private String nameType;
    private String features;
    private double price;
    private int quantity;

    public Flower(int idProduct, int idType, String nameType, String features, double price, int quantity) {
        this.idProduct = idProduct;
        this.idType = idType;
        this.nameType = nameType;
        this.features = features;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public int getIdProduct() {
        return idProduct;
    }
    @Override
    public int getIdType() {
        return idType;
    }
    @Override
    public String getNameType() {
        return nameType;
    }
    @Override
    public String getFeatures() {
        return features;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }
    @Override
    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }
    @Override
    public void setIdType(int idType) {
        this.idType = idType;
    }
    @Override
    public void setNameType(String nameType) {
        this.nameType = nameType;
    }
    @Override
    public void setFeatures(String features) {
        this.features = features;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    @Override
    public String toString() {
        return "Flower:" + "->" + " Color: " + features + " Quantity: " + quantity + " Price: " + price;
    }
}
