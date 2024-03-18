package Connections.MySQL;

import Connections.DAO.Constants;
import Connections.DAO.GenericDAO;
import FlowerStore.FlowerStore;
import FlowerStore.Interfaces.GardenElements;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class GardenElementsMySQL implements GenericDAO {

    private static Connection connection;
    private static final String URL = "jdbc:mysql://" + Constants.MYSQL_SERVER + "/" + Constants.MYSQL_DATABASE;

    public GardenElementsMySQL() {
        try {
            connection = DriverManager.getConnection(URL, Constants.MYSQL_USERNAME, Constants.MYSQL_PASSWORD);
            System.out.println("Conectado a la bbdd");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void connectMySQL() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(URL, Constants.MYSQL_USERNAME, Constants.MYSQL_PASSWORD);
                System.out.println("Conectado a la bbdd");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void disconnectMySQL() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public List<FlowerStore> showFlowerStore() {
        List<FlowerStore> flowerStores = new ArrayList<>();
        connectMySQL();
        try {
            PreparedStatement pstmt = connection.prepareStatement(QueryMySQL.ALLSHOPS_QUERY);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                flowerStores.add(new FlowerStore(rs.getString("IdFlowerShop"), rs.getString("Name")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return flowerStores;
    }

    @Override
    public List<GardenElements> allGardenElements(FlowerStore flowerStore) {
        List<GardenElements> elements = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement(QueryMySQL.ALLSTOCK_QUERY);
            pstmt.setInt(1, Integer.parseInt(flowerStore.getId()));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    elements.add(flowerStore.createElement(rs.getInt("IdGardenElements"),rs.getInt("idType"),rs.getString("TypeName"),rs.getString("features"),rs.getDouble("Price"),rs.getInt("Quantity")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return elements;
    }

    @Override
    public String createStore(String name) {
        String newStoreId = "-1";
        connectMySQL();
        String query = QueryMySQL.NEWSHOP_QUERY;
        try {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                newStoreId = generatedKeys.getString(1);
            } else {
                throw new SQLException("Failed to get the generated ID for the new store.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newStoreId;
    }

    @Override
    public void addStock(String idFlowerStore, List<GardenElements> gardenElements) {
        connectMySQL();
        String query = QueryMySQL.ADDSTOCK_QUERY;
        try {
            for (GardenElements prod : gardenElements) {
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, Integer.parseInt(idFlowerStore));
                pstmt.setInt(2, prod.getIdProduct());
                pstmt.setInt(3, prod.getQuantity());
                pstmt.setDouble(4, prod.getPrice());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStock(String idFlowerStore, GardenElements gardenElements) {
        connectMySQL();
        String query = QueryMySQL.UPDATESTOCK_QUERY;
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, gardenElements.getQuantity());
            pstmt.setDouble(2, gardenElements.getPrice());
            pstmt.setInt(3, gardenElements.getIdProduct());
            pstmt.setInt(4, Integer.parseInt(idFlowerStore));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteStock(String idFlowerStore, GardenElements gardenElements) {
        connectMySQL();
        String query = QueryMySQL.REMOVESTOCK_QUERY;
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, gardenElements.getQuantity());
            pstmt.setInt(2, gardenElements.getIdProduct());
            pstmt.setInt(3, Integer.parseInt(idFlowerStore));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<Integer, Date> allTickets(String idFlowerStore) {
        HashMap<Integer, Date> tickets = new HashMap<>();
        connectMySQL();
        String query = QueryMySQL.SHOWALLTICKETS_QUERY;

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(idFlowerStore));
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
    public void addTicket(FlowerStore flowerStore, List<GardenElements> gardenElementsList) {
        connectMySQL();

        String query = QueryMySQL.ADDNEWTICKET_QUERY;

        try {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, Integer.parseInt(flowerStore.getId()));
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            int ticketId;
            if (generatedKeys.next()) {
                ticketId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to get the generated ID for the new ticket.");
            }

            String updateQuery = QueryMySQL.ADDPRODTOTICKET_QUERY;
            pstmt = connection.prepareStatement(updateQuery);

            for (GardenElements gardenElement : gardenElementsList) {
                pstmt.setInt(1, ticketId);
                pstmt.setInt(2, gardenElement.getIdProduct());
                pstmt.setInt(3, gardenElement.getQuantity());
                pstmt.addBatch();
            }

            pstmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFlowerStore(String flowerStoreId) {

        String query = QueryMySQL.REMOVESTORE_QUERY;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(flowerStoreId));
            int rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error removing FlowerStore with ID " + flowerStoreId);
        }
    }

    @Override
    public double totalPrice(String flowerShopId) {
        double totalMoneyEarned = 0;
        String query = QueryMySQL.TOTALPRICE_QUERY;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(flowerShopId));
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    totalMoneyEarned = rs.getDouble("TotalMoneyEarned");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return totalMoneyEarned;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}





