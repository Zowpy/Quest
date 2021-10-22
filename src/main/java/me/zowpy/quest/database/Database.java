package me.zowpy.quest.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 10/22/2021
 * Project: Quest
 */

@Getter
public class Database {

    private final MongoClient client;
    private final MongoDatabase database;
    private final MongoCollection<Document> profiles;

    public Database(String uri) {
        client = new MongoClient(new MongoClientURI(uri));
        database = client.getDatabase("quests");
        profiles = database.getCollection("profiles");
    }
}
