package FlowerStore;

import FlowerStore.Interfaces.GardenElements;
import FlowerStore.Products.Decoration;
import FlowerStore.Products.Flower;
import FlowerStore.Products.Tree;

public class FlowerStore {
    private String id;
    private String name;
    public FlowerStore(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }

    public void setId(String id){
        this.id=id;
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
    public GardenElements createElement(int idProduct, int idType, String nameType, String features, double price, int quantity) {

        if(idType == 1 || nameType.equalsIgnoreCase("tree")){
            return new Tree(idProduct,idType,nameType,features,price,quantity);
        } else if (idType == 2 || nameType.equalsIgnoreCase("flower")) {
            return new Flower(idProduct,idType,nameType,features,price,quantity);
        } else if (idType == 3 || nameType.equalsIgnoreCase("decoration")) {
            return new Decoration(idProduct,idType,nameType,features,price,quantity);
        } else {
            throw new IllegalArgumentException("Tipo de elemento inválido: " + nameType);
        }
    }
}

