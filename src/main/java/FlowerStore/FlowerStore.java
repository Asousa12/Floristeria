package FlowerStore;

import FlowerStore.Interfaces.GardenElements;
import FlowerStore.Products.Decoration;
import FlowerStore.Products.Flower;
import FlowerStore.Products.Tree;

public class FlowerStore {
    private String name;
    private String id;
    public FlowerStore(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
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
    //ToDo: Hay que usar esto para construir nuestros elementos, asi es una fabrica bien hecha
    public GardenElements getElement(String type, String characteristic, double price, int id, int quantity) {
        GardenElements element;
        switch(type.toUpperCase()) {
            case "FLOWER" -> element = new Flower(quantity,id, characteristic, price);
            case "DECORATION" -> element = new Decoration(quantity,id, characteristic, price);
            case "TREES" -> element = new Tree(quantity,id, characteristic, price);
            default -> throw new IllegalStateException("Unexpected value");
        }
        return element;
    }
}

