package FlowerStoreFactory.Products;

import FlowerStore.Interfaces.GardenElements;

public class Tree implements GardenElements {

    private String name;
    private int idProduct;
    private String size;
    private double price;

    public Tree(String name, int idProduct, String size, double price) {
        this.name=name;
        this.idProduct=idProduct;
        this.size=size;
        this.price=price;
    }


    @Override
    public String getName() {
        return name;
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
}
