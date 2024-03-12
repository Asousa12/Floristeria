package Connections.MongoDB;

import Connections.DAO.Constants;
import Connections.DAO.GenericDAO;
import FlowerStore.Interfaces.GardenElements;
import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GardenElementsMongoDB implements GenericDAO {

    //private final MongoCollection<Document> collection;
    private static ConnectionString connectionString = new ConnectionString(Constants.MONGO_URL);
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    public GardenElementsMongoDB() {
        try {
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase("flowerStoreBBDD");
        } catch (MongoException e){
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
    private static void connectMongodb(){
        try{
            if(mongoClient == null){
                mongoClient = MongoClients.create(connectionString);
                database = mongoClient.getDatabase("flowerStoreBBDD");
            }
        } catch (MongoException e){
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
    private static void disconnectMongodb(){
        try{
            if(mongoClient != null){
                mongoClient.close();
            }
        } catch (MongoException e){
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    @Override
    public HashMap<Integer, String> showFlowerStore() {
        return null;
    }

    @Override
    public GardenElements findById(int id) {
        return null;
    }

    @Override
    public List<GardenElements> allGardenElements(int idFlowerStore) {
        return null;
    }

    @Override
    public int createStore(String name) {
        MongoCollection<Document> collection = database.getCollection("FlowerShops");
        Document newFlowerShop;
        try {
            List<Document> stock = new ArrayList<>();
            stock.add(new Document("type", "tree")
                    .append("Features", "3 m")
                    .append("Quantity", 0)
                    .append("Price", 0));
            stock.add(new Document("type", "tree")
                    .append("Features", "5 m")
                    .append("Quantity", 0)
                    .append("Price", 0));
            stock.add(new Document("type", "tree")
                    .append("Features", "50 cm")
                    .append("Quantity", 0)
                    .append("Price", 0));
            stock.add(new Document("type", "flower")
                    .append("Features", "Red")
                    .append("Quantity", 0)
                    .append("Price", 0));
            stock.add(new Document("type", "flower")
                    .append("Features", "Blue")
                    .append("Quantity", 0)
                    .append("Price", 0));
            stock.add(new Document("type", "flower")
                    .append("Features", "Green")
                    .append("Quantity", 0)
                    .append("Price", 0));
            stock.add(new Document("type", "decoration")
                    .append("Features", "Metal")
                    .append("Quantity", 0)
                    .append("Price", 0));
            stock.add(new Document("type", "decoration")
                    .append("Features", "Plastic")
                    .append("Quantity", 0)
                    .append("Price", 0));

            newFlowerShop = new Document("_id", new ObjectId())
                    .append("name", name)
                    .append("stock", stock);
            InsertOneResult result = collection.insertOne(newFlowerShop);
        } catch (MongoException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        //return newFlowerShop.get("_id");
        return 0;
    }

    @Override
    public void updateStock(GardenElements gardenElement, int quantity) {

    }

    @Override
    public void deleteStock(GardenElements gardenElement) {

    }

    @Override
    public HashMap<Integer, Date> allTickets(int idFlowerStore) {
        return null;
    }

    @Override
    public void addTicket(int idFlowerstore, HashMap gardenElementsList) {

    }

    @Override
    public void addStock(int idFlowerStore, List<GardenElements> products) {

    }
}
