package Connections.MongoDB;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException;

import javax.swing.text.Document;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class GardenElementsMongoDB {
    private final MongoCollection<Document> collection;
    private MongoClient mongoClient;
    private MongoDatabase database;
    public GardenElementsMongoDB() {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("your_database_name");
        this.collection = database.getCollection("your_collection_name");
    }
    public GardenElementsMongoDB(String connectionString, String databaseName) {
        try {
            MongoClient
            mongoClient = MongoClients.create(connectionString);
            // Obtener una referencia a la base de datos
            database = mongoClient.getDatabase(databaseName);
        } catch (MongoException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
