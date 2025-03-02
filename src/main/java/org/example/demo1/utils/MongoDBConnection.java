package org.example.demo1.utils;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    // Connection string with all credentials and database name embedded
    private static final String CONNECTION_STRING = "mongodb+srv://ooad:Gaslitty@ooad.mwfkd.mongodb.net/dataStructuresVis?retryWrites=true&w=majority&appName=OOAD";

    // The database name is already in the connection string, but we'll keep it explicit for getDatabase()
    private static final String DATABASE_NAME = "dataStructuresVis";

    private static MongoClient mongoClient = null;

    /**
     * Get a MongoDB client instance
     * @return MongoClient instance
     */
    public static MongoClient getMongoClient() {
        if (mongoClient == null) {
            try {
                // Create connection string
                ConnectionString connectionString = new ConnectionString(CONNECTION_STRING);

                // Configure the MongoClientSettings
                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .serverApi(ServerApi.builder()
                                .version(ServerApiVersion.V1)
                                .build())
                        .build();

                // Create a new client
                mongoClient = MongoClients.create(settings);
                System.out.println("Successfully connected to MongoDB Atlas");
            } catch (Exception e) {
                System.err.println("Failed to connect to MongoDB Atlas: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }

        return mongoClient;
    }

    /**
     * Get the MongoDB database
     * @return MongoDatabase instance
     */
    public static MongoDatabase getDatabase() {
        return getMongoClient().getDatabase(DATABASE_NAME);
    }

    /**
     * Close the MongoDB connection
     */
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }

    /**
     * Test the MongoDB connection
     * @return true if connection is successful
     */
    public static boolean testConnection() {
        try {
            // Try to get the database and list collection names
            getDatabase().listCollectionNames().first();
            System.out.println("MongoDB connection test successful");
            return true;
        } catch (Exception e) {
            System.err.println("MongoDB connection test failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}