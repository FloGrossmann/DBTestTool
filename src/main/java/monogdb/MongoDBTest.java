package monogdb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBTest {
	
	ConnectionString connString;
	MongoClient mongoClient;
	MongoDatabase testDatabase;
	public MongoDBTest() {
	}
	
	public void connect(String connectionString) {
		connString = new ConnectionString(connectionString);
		System.out.println("Test connection with connectionString" + connString.getConnectionString());
		MongoClientSettings settings = MongoClientSettings.builder()
			    .applyConnectionString(connString)
			    .retryWrites(true)
			    .build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase("dbTest");
	}
}
