package FlowerStoreFactory.Products;

import FlowerStore.Interfaces.GardenElements;

public class Decoration implements GardenElements {

    private String name;
    private int idProduct;
    private String typeMaterial;
    private double price;

    public Decoration(String name, int idProduct, String typeMaterial, double price){
        this.name=name;
        this.idProduct=idProduct;
        this.typeMaterial=typeMaterial;
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
        return typeMaterial;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
