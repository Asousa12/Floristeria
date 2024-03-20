package Connections.MongoDB;

import Connections.DAO.Constants;
import Connections.DAO.GenericDAO;
import FlowerStore.FlowerStore;
import FlowerStore.Interfaces.GardenElements;
import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class GardenElementsMongoDB implements GenericDAO {
    private static ConnectionString connectionString = new ConnectionString(Constants.MONGO_URL);
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    public GardenElementsMongoDB() {
        try {
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase("flowerStoreBBDD");
        } catch (MongoException e){
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
    private static void connectMongodb(){
        try{
            if(mongoClient == null){
                mongoClient = MongoClients.create(connectionString);
                database = mongoClient.getDatabase("flowerStoreBBDD");
            }
        } catch (MongoException e){
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
    private static void disconnectMongodb(){
        try{
            if(mongoClient != null){
                mongoClient.close();
            }
        } catch (MongoException e){
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    @Override
    public List<FlowerStore> showFlowerStore() {
        List<FlowerStore> flowerStores = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("FlowerShops");
        FindIterable<Document> documents = collection.find();
        for (Document doc : documents) {
            flowerStores.add(new FlowerStore(doc.getObjectId("_id").toHexString(), doc.getString("name")));
        }
        return flowerStores;
    }

    @Override
    public List<GardenElements> allGardenElements(FlowerStore flowerStore) {
        List<GardenElements> elements = new ArrayList<>();

        MongoCollection<Document> collection = database.getCollection("FlowerShops");

        Document query = new Document("_id", new ObjectId(flowerStore.getId()));
        FindIterable<Document> results = collection.find(query);

        ArrayList<Document> stock = (ArrayList<Document>) results.first().get("stock");
        for(Document stockIte : stock){
            String type = stockIte.getString("type");
            String features = stockIte.getString("Features");
            int quantity = stockIte.getInteger("Quantity");
            double price = stockIte.getDouble("Price");
            try{
                elements.add(flowerStore.createElement(0,0,type,features,price,quantity));
            }catch (IllegalArgumentException e){
                System.out.println("Error: " + e.getMessage());
            }
        }
        return elements;
    }

    @Override
    public String createStore(String name) {
        MongoCollection<Document> collection = database.getCollection("FlowerShops");
        Document newFlowerShop = new Document();
        try {
            List<Document> stock = new ArrayList<>();
            stock.add(new Document("type", "tree")
                    .append("Features", "3 m")
                    .append("Quantity", 0)
                    .append("Price", 0.0));
            stock.add(new Document("type", "tree")
                    .append("Features", "5 m")
                    .append("Quantity", 0)
                    .append("Price", 0.0));
            stock.add(new Document("type", "tree")
                    .append("Features", "50 cm")
                    .append("Quantity", 0)
                    .append("Price", 0.0));
            stock.add(new Document("type", "flower")
                    .append("Features", "Red")
                    .append("Quantity", 0)
                    .append("Price", 0.0));
            stock.add(new Document("type", "flower")
                    .append("Features", "Blue")
                    .append("Quantity", 0)
                    .append("Price", 0.0));
            stock.add(new Document("type", "flower")
                    .append("Features", "Green")
                    .append("Quantity", 0)
                    .append("Price", 0.0));
            stock.add(new Document("type", "decoration")
                    .append("Features", "Metal")
                    .append("Quantity", 0)
                    .append("Price", 0.0));
            stock.add(new Document("type", "decoration")
                    .append("Features", "Plastic")
                    .append("Quantity", 0)
                    .append("Price", 0.0));

            newFlowerShop = new Document("_id", new ObjectId())
                    .append("name", name)
                    .append("stock", stock);
            InsertOneResult result = collection.insertOne(newFlowerShop);
        } catch (MongoException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        ObjectId idFlowerStore = newFlowerShop.getObjectId("_id");
        return idFlowerStore.toHexString();
    }

    @Override
    public void updateStock(String idFlowerStore, GardenElements gardenElements) {
        MongoCollection<Document> collection = database.getCollection("FlowerShops");

        Document filter = new Document("_id", new ObjectId(idFlowerStore))
                .append("stock", new Document("$elemMatch", new Document("type", gardenElements.getNameType())
                        .append("Features", gardenElements.getFeatures())));
        Document update = new Document("$set", new Document("stock.$.Quantity", gardenElements.getQuantity())
                .append("stock.$.Price", gardenElements.getPrice()));
        collection.updateOne(filter,update);
    }



    @Override
    public void deleteStock(String idFlowerStore, GardenElements gardenElements) {
        MongoCollection<Document> collection = database.getCollection("FlowerShops");

        // Construye la consulta para encontrar el elemento en el stock
        Document query = new Document("_id", new ObjectId(idFlowerStore))
                .append("stock.type", gardenElements.getNameType())
                .append("stock.Features", gardenElements.getFeatures());

        // Ejecuta la eliminación del producto del stock
        collection.updateOne(query, new Document("$pull", new Document("stock", new Document("Features", gardenElements.getFeatures()))));

    }


    @Override
    public HashMap<Integer, Date> allTickets(String idFlowerStore) {
        MongoCollection<Document> collection = database.getCollection("Tickets");
        HashMap<Integer, Date> ticketsMap = new HashMap<>();

        Document query = new Document("FlowerStore", idFlowerStore);
        FindIterable<Document> tickets = collection.find(query);

        for (Document ticket : tickets) {
            Integer ticketId = ticket.getInteger("_id");
            Date ticketDate = ticket.getDate("date");
            ticketsMap.put(ticketId, ticketDate);
        }

        return ticketsMap;
    }

    @Override
    public void addTicket(FlowerStore flowerStore, List<GardenElements> gardenElementsList) {
        MongoCollection<Document> collection = database.getCollection("Tickets");
        List<Document> ticketInfoProd = new ArrayList<>();
        double totalPrice = 0d;

        for(GardenElements product : gardenElementsList){
            ticketInfoProd.add(new Document("Type",product.getNameType())
                    .append("Features", product.getFeatures())
                    .append("Quantity", product.getQuantity())
                    .append("Price", product.getPrice()));
            totalPrice += product.getQuantity() * product.getPrice();
        }
        Document newTicket = new Document("_id", new ObjectId())
                .append("date", new Date())
                .append("FlowerStore", new ObjectId(flowerStore.getId()))
                .append("products", ticketInfoProd)
                .append("totalPrice", totalPrice);
        collection.insertOne(newTicket);
    }

    @Override
    public void removeFlowerStore(String flowerStoreId) {
        MongoCollection<Document> collection = database.getCollection("FlowerShops");

        Document query = new Document("_id", new ObjectId(flowerStoreId));

        collection.deleteOne(query);

    }

    @Override
    public double totalPrice(String flowerStoreId) {
        MongoCollection<Document> collection = database.getCollection("Tickets");
        double totalMoneyEarned = 0;
        Document query = new Document("FlowerStore", new ObjectId(flowerStoreId));
        FindIterable<Document> allTickets = collection.find(query);
        for(Document oneTicket : allTickets){
            totalMoneyEarned += oneTicket.getDouble("totalPrice");
        }
        return totalMoneyEarned;
    }

    @Override
    public void addStock(String idFlowerStore, List<GardenElements> products) {
    // Don't use it, we add stock when we create the flowerstore

    }
}
