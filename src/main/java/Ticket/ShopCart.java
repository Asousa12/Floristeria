package Ticket;

import FlowerStore.Interfaces.GardenElements;

import java.util.HashMap;
import java.util.Map;

public class ShopCart {

    private static ShopCart instance;
    private Map<GardenElements, Integer> productos;

    private ShopCart() {
        this.productos = new HashMap<>();
    }

    public static ShopCart getInstance() {
        if (instance == null) {
            instance = new ShopCart();
        }
        return instance;
    }
    public void addProductos (GardenElements producto, int quantity){
       productos.put(producto, quantity);
    }
    public void removeProducto(GardenElements producto, int quantity) {
        if (productos.containsKey(producto)) {
            int currentQuantity = productos.get(producto);
            if (currentQuantity <= quantity) {
                productos.remove(producto);
            } else {
                productos.put(producto, currentQuantity - quantity);
            }
            System.out.println("The product removed was: " + producto + " - quantity: " + quantity);
        } else {
            System.out.println("The specif product, doesn't exist on ticket.");
        }
    }
    public int getProductosQuantity(GardenElements producto){
        return productos.getOrDefault(producto, 0);
    }
    public HashMap<GardenElements, Integer> getProducts() {
        return new HashMap<>(productos);
    }
    public void printTicket() {
        System.out.println("....Ticket information....");
        for (Map.Entry<GardenElements, Integer> entry : productos.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}




