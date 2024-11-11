package pong.test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spock.lang.Ignore;

import java.util.Arrays;

@Ignore
public class MongoDbTest {
    private static MongoClient client;
    private static MongoDatabase database;

    @BeforeAll
    static void init() {
        client = MongoClients.create("mongodb://127.0.0.1:27017");
        database = client.getDatabase("testDB");
    }

    @AfterAll
    static void after() {
        client.close();
    }

    @Test
    void test() {
        MongoCollection<Document> coll = database.getCollection("test");
        Document document = new Document();
        document.put("name", "李四");
        document.put("phone", "123456");
        coll.insertOne(document);

        coll.find(Filters.eq("name", "张三")).forEach(doc -> System.out.println(doc.toJson()));

        coll.updateMany(Filters.eq("name", "李四"), Updates.set("age", 2));

        coll.find(Filters.eq("name", "/李/")).forEach(doc -> System.out.println(doc.toJson()));

        coll.aggregate(Arrays.asList(Aggregates.match(new Document("name", "李四")), Aggregates.count()))
            .forEach(doc -> System.out.println(doc.toJson()));

        coll.deleteMany(Filters.eq("name", "李四"));
    }
}
