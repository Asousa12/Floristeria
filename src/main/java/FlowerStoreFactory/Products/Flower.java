package FlowerStoreFactory.Products;

import FlowerStore.Interfaces.GardenElements;

public class Flower implements GardenElements {

    private int quantity;
    private int idProduct;
    private String color;
    private double price;

    public Flower(int quantity, int idProduct, String color, double price){
        this.quantity=quantity;
        this.idProduct=idProduct;
        this.color=color;
        this.price=price;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }


    @Override
    public int getIdProduct() {
        return idProduct;
    }

    @Override
    public String getCharacteristics() {
        return color;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity=quantity;
    }

    @Override
    public void setPrice(double price) {
         this.price=price;
    }

    @Override
    public String toString() {
        return "Flower:" + "->" +
                " Quantity: " + quantity  +
                " idProduct: " + idProduct +
                " Color: " + color +
                " Price: " + price;
    }
}
