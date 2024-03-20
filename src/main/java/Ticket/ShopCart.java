package Ticket;

import FlowerStore.Interfaces.GardenElements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopCart {

    private static ShopCart instance;
    private List<GardenElements> productos;

    private ShopCart() {
        this.productos = new ArrayList<>();
    }

    public static ShopCart getInstance() {
        if (instance == null) {
            instance = new ShopCart();
        }
        return instance;
    }

    public void addProductos(GardenElements producto, int quantity) {
        producto.setQuantity(quantity);
        this.productos.add(producto);

    }

    public List<GardenElements> getProducts() {
        return this.productos;
    }

    public void printTicket() {
        System.out.println("....Ticket information....");
        for (int i = 0; i < productos.size(); i++) {
            System.out.println(productos.get(i));
        }
    }
}