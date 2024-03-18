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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
//            FlowerStore flowerStore = new FlowerStore(doc.getObjectId("_id").toString(), doc.getString("name"));
//            flowerStore.setId(doc.getObjectId("_id").toString());
//            flowerStore.setName(doc.getString("name"));
//
//            flowerStores.add(flowerStore);


            flowerStores.add(new FlowerStore(doc.getObjectId("_id").toString(), doc.getString("name")));
        }

        return flowerStores;
        }

    @Override
    public List<GardenElements> allGardenElements(FlowerStore flowerStore) {
        List<GardenElements> elements = new ArrayList<>();

        MongoCollection<Document> collection = database.getCollection("FlowerShops");

        Document query = new Document("_id", flowerStore.getId());
        FindIterable<Document> results = collection.find(query);

        //Document res = collection.find(query);
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






//        for (Document doc : results) {
//            List<Document> stock = (List<Document>) doc.get("stock");
//
//            for (Document item : stock) {
//                String type = item.getString("type");
//                String features = item.getString("Features");
//                int quantity = item.getInteger("Quantity");
//                double price = item.getDouble("Price");
//                try{
//                    elements.add(flowerStore.createElement(0,0,type,features,price,quantity));
//                }catch (IllegalArgumentException e){
//                    System.out.println("Error: " + e.getMessage());
//                }


//                switch (type) {
//                    case "tree":
//                        Tree tree = new Tree(0, 0, "Tree", features, price, quantity);
//                        elements.add(tree);
//                        break;
//                    case "flower":
//                        Flower flower = new Flower(0, 0, "Flower", features, price, quantity);
//                        elements.add(flower);
//                        break;
//                    case "decoration":
//                        Decoration decoration = new Decoration(0, 0, "Decoration", features, price, quantity);
//                        elements.add(decoration);
//                        break;
//                    default:
//                        throw new IllegalArgumentException("Invalid type: " + type);
//                }
//            }
//        }

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

        // Construye la consulta para encontrar el elemento en el stock
        Document query = new Document("_id", new ObjectId(idFlowerStore))
                .append("stock.type", gardenElements.getNameType())
                .append("stock.Features", gardenElements.getFeatures());

        // Construye la actualización del stock
        Document update = new Document("$inc", new Document("stock.$.Quantity", gardenElements.getQuantity()));

        // Ejecuta la actualización
        collection.updateOne(query, update);
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

        for(GardenElements product : gardenElementsList){
            ticketInfoProd.add(new Document("Type",product.getNameType())
                    .append("Features", product.getFeatures())
                    .append("Quantity", product.getQuantity())
                    .append("Price", product.getPrice()));
        }
        Document newTicket = new Document("_id", new ObjectId())
                .append("date", new Date())
                .append("FlowerStore", flowerStore.getName())
                .append("products", ticketInfoProd);
        InsertOneResult result = collection.insertOne(newTicket);
    }

    @Override
    public void removeFlowerStore(String flowerStoreId) {
        MongoCollection<Document> collection = database.getCollection("FlowerShops");

        Document query = new Document("_id", flowerStoreId);

        collection.deleteOne(query);

    }

    @Override
    public double totalPrice(String flowerStoreId) {
        MongoCollection<Document> collection = database.getCollection("FlowerShops");

        Document query = new Document("_id", flowerStoreId);
        Document flowerShop = collection.find(query).first();

        List<Document> stock = (List<Document>) flowerShop.get("stock");

        double totalMoneyEarned = 0;
        for (Document product : stock) {
            double price = product.getDouble("Price");
            int quantity = product.getInteger("Quantity");
            totalMoneyEarned += price * quantity;
        }

        return totalMoneyEarned;
    }

    @Override
    public void addStock(String idFlowerStore, List<GardenElements> products) {
        MongoCollection<Document> collection = database.getCollection("Stock");

        try {
            for (GardenElements prod : products) {

                Document gardenElementDocument = new Document();
                gardenElementDocument.append("FlowerShopId", idFlowerStore)
                        .append("type", prod.getNameType())
                        .append("Quantity", prod.getQuantity())
                        .append("Price", prod.getPrice());

                collection.insertOne(gardenElementDocument);
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }

    }
}
