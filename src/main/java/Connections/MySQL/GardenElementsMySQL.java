package Connections.MySQL;

import Connections.DAO.Constants;
import Connections.DAO.GenericDAO;
import FlowerStore.Interfaces.GardenElements;
import FlowerStore.Products.Decoration;
import FlowerStore.Products.Flower;
import FlowerStore.Products.Tree;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class GardenElementsMySQL implements GenericDAO {

    private static Connection connection;
    private static final String  URL = "jdbc:mysql://" + Constants.MYSQL_SERVER + "/" + Constants.MYSQL_DATABASE;


    public GardenElementsMySQL() {
        try {
            connection = DriverManager.getConnection(URL,Constants.MYSQL_USERNAME,Constants.MYSQL_PASSWORD);
            System.out.println("Conectado a la bbdd");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void connect() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(URL,Constants.MYSQL_USERNAME,Constants.MYSQL_PASSWORD);
                System.out.println("Conectado a la bbdd");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public HashMap<Integer, String> showFlowerStore() {
        HashMap<Integer, String> flowerStores = new HashMap<>();
        connect();
        try{
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM FlowerShops");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                flowerStores.put(rs.getInt("IdFlowerShop"), rs.getString("Name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return flowerStores;
    }

    @Override
    public GardenElements findById(int id) {
        GardenElements gardenElement = null;
        connect();
        String query = "SELECT * FROM GardenElements WHERE IdGardenElements = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int type = rs.getInt("TypesId"); // Supongamos que tienes un campo "type" en tu base de datos que indica el tipo de elemento (Flower, Tree, o Decoration)
                switch (type) {
                    case 1:
                        gardenElement = new Tree(
                                rs.getInt("quantity"),
                                rs.getInt("idProduct"),
                                rs.getString("size"),
                                rs.getDouble("price")
                        );
                        break;
                    case 2:
                        gardenElement = new Flower(
                                rs.getInt("quantity"),
                                rs.getInt("idProduct"),
                                rs.getString("color"),
                                rs.getDouble("price")
                        );
                        break;
                    case 3:
                        gardenElement = new Decoration(
                                rs.getInt("quantity"),
                                rs.getInt("idProduct"),
                                rs.getString("typeMaterial"),
                                rs.getDouble("price")
                        );
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid type: " + type);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        return gardenElement;
    }

    @Override
    public List<GardenElements> allGardenElements(int idFlowerStore) {
        List<GardenElements> elements = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement("Select * from GardenElements left join Stock on GardenElements.idGardenElements = Stock.GardenElementsId and Stock.FlowerShopId = ?;");
            pstmt.setInt(1, idFlowerStore);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int type = rs.getInt("TypesId");
                    switch (type) {
                        case 1 -> elements.add(new Tree(rs.getInt("quantity"), rs.getInt("idGardenElements"), rs.getString("features"), rs.getDouble("price")));


                        case 2 -> elements.add(new Flower(rs.getInt("quantity"), rs.getInt("idGardenElements"), rs.getString("features"), rs.getDouble("price")));

                        case 3 -> elements.add(new Decoration(rs.getInt("quantity"), rs.getInt("idGardenElements"), rs.getString("features"), rs.getDouble("price")));

                        default -> throw new IllegalArgumentException("Invalid type: " + type);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return elements;
    }

    @Override
    public int createStore(String name) {
        int newStoreId = -1;
        connect();
        String query = "INSERT INTO FlowerShops (Name) VALUES (?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                newStoreId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to get the generated ID for the new store.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newStoreId;
    }

    @Override
    public void addStock(int idFlowerStore, List<GardenElements> products) {
        connect();
        String query = "insert into Stock (FlowerShopId,GardenElementsId,Quantity,Price) VALUES (?,?,?,?)";
        try {
            for (GardenElements prod : products) {
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, idFlowerStore);
                pstmt.setInt(2, prod.getIdProduct());
                pstmt.setInt(3, prod.getQuantity());
                pstmt.setDouble(4, prod.getPrice());
                pstmt.executeUpdate();
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateStock(GardenElements gardenElement, int quantity) {
        connect();
        String query = "UPDATE Stock SET Quantity = Quantity + ? WHERE GardenElementsId = ? and FlowerStoreId = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, gardenElement.getIdProduct());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteStock(GardenElements gardenElement) {
        connect();
        String query = "UPDATE Stock SET Quantity = Quantity - ? WHERE GardenElementsId = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, gardenElement.getIdProduct());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public HashMap<Integer, Date> allTickets(int idFlowerStore) {
        HashMap<Integer, Date> tickets = new HashMap<>();
        connect();
        String query = "SELECT * FROM Ticket WHERE FlowerShopId = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, idFlowerStore);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tickets.put(rs.getInt("IdTicket"), rs.getDate("Date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    @Override
    public void addTicket(int idFlowerstore, HashMap gardenElementsList) {
        connect();

        String query = "INSERT INTO Ticket (FlowerShopId, TotalPrice) VALUES (?,?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            for (Object gardenElementId : gardenElementsList.keySet()) {
                pstmt.setInt(1, idFlowerstore);
                pstmt.setInt(2, (Integer) gardenElementsList.get(gardenElementId));
                pstmt.addBatch();
            }
            pstmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void removeFlowerStore(int flowerStoreId) {

        String query = "DELETE FROM FlowerShops WHERE IdFlowerShop = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, flowerStoreId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No FlowerStore found with ID " + flowerStoreId);
            } else {
                System.out.println("FlowerStore with ID " + flowerStoreId + " has been deleted.");
            }
        } catch (SQLException e) {
            System.out.println("Error removing FlowerStore with ID " + flowerStoreId);
        }
    }
    @Override
    public double TotalPrice(){
        double totalMoneyEarned=0;
        try {
            String query = "SELECT SUM(TotalPrice) AS TotalMoneyEarned FROM Ticket";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        totalMoneyEarned = rs.getDouble("TotalMoneyEarned");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect(); // Desconectarse de la base de datos
        }

        return totalMoneyEarned;
    }
}





