package FlowerStoreFactory.Products;

import FlowerStore.Interfaces.GardenElements;

public class Tree implements GardenElements {

    private int quantity;
    private int idProduct;
    private String size;
    private double price;

    public Tree(int quantity, int idProduct, String size, double price) {
        this.quantity=quantity;
        this.idProduct=idProduct;
        this.size=size;
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
        return size;
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
}
