package FlowerStoreFactory.Products;

import FlowerStore.Interfaces.GardenElements;

public class Decoration implements GardenElements {

    private int quantity;
    private int idProduct;
    private String typeMaterial;
    private double price;

    public Decoration(int quantity, int idProduct, String typeMaterial, double price){
        this.quantity=quantity;
        this.idProduct=idProduct;
        this.typeMaterial=typeMaterial;
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
        return typeMaterial;
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
        return "Decoration:" + "->" +
                " Quantity: " + quantity +
                " idProduct: " + idProduct +
                " TypeMaterial: " + typeMaterial +
                " Price: " + price;
    }
}
