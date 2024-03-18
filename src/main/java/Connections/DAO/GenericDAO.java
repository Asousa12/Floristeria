package Connections.DAO;

import FlowerStore.FlowerStore;
import FlowerStore.Interfaces.GardenElements;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface GenericDAO {
        List<FlowerStore> showFlowerStore();
        List<GardenElements> allGardenElements(FlowerStore flowerStore);
        String createStore(String name);
        void addStock(String idFlowerStore, List<GardenElements> products);
        void updateStock(String idFlowerStore, GardenElements gardenElements);
        void deleteStock(String idFlowerStore, GardenElements gardenElements);
        HashMap<Integer, Date> allTickets(String idFlowerStore);
        void addTicket(FlowerStore flowerStore, List<GardenElements> gardenElementsList);
        void removeFlowerStore(String flowerStoreId);
        double totalPrice(String flowerStoreId);
}
