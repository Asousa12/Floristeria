package FlowerStore;

import FlowerStore.Interfaces.GardenElements;
import FlowerStoreFactory.Products.Decoration;
import FlowerStoreFactory.Products.Flower;
import FlowerStoreFactory.Products.Tree;

public class FlowerStore {
    private String name;
    private int id;
    public FlowerStore(String name, int id){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FlowerStoreFactory{" +
                "name='" + name + '\'' +
                '}';
    }

    public GardenElements getElement(String type, String characteristic, double price, int id, int quantity) {
        GardenElements element = null;
        switch(type.toUpperCase()) {
            case "FLOWER" -> element = new Flower(quantity,id, characteristic, price);
            case "DECORATION" -> element = new Decoration(quantity,id, characteristic, price);
            case "TREES" -> element = new Tree(quantity,id, characteristic, price);
            default -> throw new IllegalStateException("Unexpected value");
        }
        return element;
    }
}

