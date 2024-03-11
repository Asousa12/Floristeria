package Connections.DAO;

import FlowerStore.Interfaces.GardenElements;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface GenericDAO<T> {
        HashMap<Integer,String> showFlowerStore() throws SQLException;
        GardenElements findById(int id) throws SQLException;
        List<GardenElements> allGardenElements(int idFlowerStore);
        int createStore(String name) throws SQLException;
        void addStock(GardenElements gardenElement, int quantity);
        void updateStock(GardenElements gardenElement, int quantity);
        void deleteStock(GardenElements gardenElement);
        HashMap<Integer, Date> allTickets(int idFlowerStore);
        void addTicket(int idFlowerstore, HashMap<Integer,Integer> gardenElementsList);
}
