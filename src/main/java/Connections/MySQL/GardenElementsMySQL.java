package Connections.MySQL;

import Connections.DAO.GenericDAO;
import FlowerStore.Interfaces.GardenElements;
import FlowerStoreFactory.Products.Decoration;
import FlowerStoreFactory.Products.Flower;
import FlowerStoreFactory.Products.Tree;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GardenElementsMySQL<T extends GardenElements> implements GenericDAO {

    private static final HikariDataSource dataSource;
    private static Connection connection;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + Constants.MYSQL_SERVER + "/" + Constants.MYSQL_DATABASE);
        config.setUsername(Constants.MYSQL_USERNAME);
        config.setPassword(Constants.MYSQL_PASSWORD);
        dataSource = new HikariDataSource(config);
    }

    public GardenElementsMySQL() {
        try {
            connection = dataSource.getConnection();
            System.out.println("Conectado a la bbdd");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void connectMySQL() {
        try {
            if (connection == null) {
                connection = dataSource.getConnection();
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
    public HashMap<Integer, String> showFlowerStore() throws SQLException {
        HashMap<Integer, String> flowerStores = new HashMap<>();
        connectMySQL();
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM FlowerShops");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            flowerStores.put(rs.getInt("IdFlowerShop"), rs.getString("Name"));
        }
        disconnectMySQL();
        return flowerStores;
    }

    @Override
    public GardenElements findById(int id) {
        GardenElements gardenElement = null;
        connectMySQL();
        String query = "SELECT * FROM GardenElements WHERE IdGardenElements = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String type = rs.getString("TypeName"); // Supongamos que tienes un campo "type" en tu base de datos que indica el tipo de elemento (Flower, Tree, o Decoration)
                switch (type.toUpperCase()) {
                    case "FLOWER":
                        gardenElement = new Flower(
                                rs.getString("name"),
                                rs.getInt("idProduct"),
                                rs.getString("color"),
                                rs.getDouble("price")
                        );
                        break;
                    case "TREE":
                        gardenElement = new Tree(
                                rs.getString("name"),
                                rs.getInt("idProduct"),
                                rs.getString("size"),
                                rs.getDouble("price")
                        );
                        break;
                    case "DECORATION":
                        gardenElement = new Decoration(
                                rs.getString("name"),
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
            e.printStackTrace();
        } finally {
            disconnectMySQL();
        }
        return gardenElement;
    }

    @Override
    public List<GardenElements> allGardenElements(int idFlowerStore) {
        List<GardenElements> gardenElements = new ArrayList<>();
        connectMySQL();
        String query = "SELECT * FROM GardenElements";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idFlowerStore);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {

                GardenElements gardenElement = new GardenElementImpl(
                        rs.getInt("IdGardenElements"),
                        rs.getInt("TypesId"),
                        rs.getString("Features")

                );
                gardenElements.add(gardenElement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnectMySQL();
        }
        return gardenElements;
    }


    @Override
    public int createStore(String name) {
        int newStoreId = -1;
        connectMySQL();
        String query = "INSERT INTO FlowerShops (Name) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
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
        } finally {
            disconnectMySQL();
        }
        return newStoreId;
    }


    @Override
    public void addStock(GardenElements gardenElement, int quantity) {
        connectMySQL();
        String query = "UPDATE Stock SET Quantity = Quantity + ? WHERE GardenElementsId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, gardenElement.getIdProduct());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnectMySQL();
        }
    }

    @Override
    public void updateStock(GardenElements gardenElement, int quantity) {
        connectMySQL();
        String query = "UPDATE Stock SET idStock = 1 WHERE GardenElementsId = 1";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, gardenElement.getIdProduct());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnectMySQL();
        }
    }

    @Override
    public void deleteStock(GardenElements gardenElement) {
        connectMySQL();
        String query = "UPDATE Stock SET Quantity = Quantity - ? WHERE GardenElementsId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, gardenElement.getIdProduct());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnectMySQL();
        }
    }


    @Override
    public HashMap<Integer, Date> allTickets(int idFlowerStore) {
        HashMap<Integer, Date> tickets = new HashMap<>();
        connectMySQL();
        String query = "SELECT IdTicket, Date FROM Ticket WHERE FlowerShopId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idFlowerStore);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tickets.put(rs.getInt("IdTicket"), rs.getDate("Date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnectMySQL();
        }
        return tickets;
    }

    @Override
    public void addTicket(int idFlowerstore, HashMap gardenElementsList) {
    }
}

