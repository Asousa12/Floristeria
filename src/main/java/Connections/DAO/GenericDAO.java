package Connections.DAO;

import FlowerStore.Interfaces.GardenElements;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface GenericDAO<T> {
        HashMap<Integer,String> showFlowerStore();
        GardenElements findById(int id);
        List<GardenElements> allGardenElements(int idFlowerStore);
        int createStore(String name);
        void addStock(int idFlowerStore, ArrayList<GardenElements> products);
        void updateStock(GardenElements gardenElement, int quantity);
        void deleteStock(GardenElements gardenElement);
        HashMap<Integer, Date> allTickets(int idFlowerStore);
        void addTicket(int idFlowerstore, HashMap<Integer,Integer> gardenElementsList);
}
