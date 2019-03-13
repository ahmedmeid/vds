package se.alten.challenge.config.dbmigrations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@ChangeLog
public class DatabaseChangelog {

	@ChangeSet(order = "001", id = "20190309202230_added_initial_data", author = "AhmedEid")
	public void importantWorkToDo(MongoDatabase db) {
		
		MongoCollection<Document> connectionStatuscollection = db.getCollection("vehicle_connection_status");
		
		List<Document> initialStausData = new ArrayList<>();

		initialStausData.add(new Document("vehicle_id", "YS2R4X20005399401").append("status", "DISCONNECTED")
				.append("last_updated", Date.from(Instant.now())));
		
		initialStausData.add(new Document("vehicle_id", "VLUR4X20009093588").append("status", "DISCONNECTED")
				.append("last_updated", Date.from(Instant.now())));
		
		initialStausData.add(new Document("vehicle_id", "VLUR4X20009048066").append("status", "DISCONNECTED")
				.append("last_updated", Date.from(Instant.now())));
		
		initialStausData.add(new Document("vehicle_id", "YS2R4X20005388011").append("status", "DISCONNECTED")
				.append("last_updated", Date.from(Instant.now())));
		
		initialStausData.add(new Document("vehicle_id", "YS2R4X20005387949").append("status", "DISCONNECTED")
				.append("last_updated", Date.from(Instant.now())));
		
		initialStausData.add(new Document("vehicle_id", "YS2R4X20005387055").append("status", "DISCONNECTED")
				.append("last_updated", Date.from(Instant.now())));
		
		connectionStatuscollection.insertMany(initialStausData);
		
        MongoCollection<Document> connectionStatusHistorycollection = db.getCollection("vehicle_connectiom_status_history");
		
		List<Document> initialStausHistData = new ArrayList<>();

		initialStausHistData.add(new Document("vehicle_id", "YS2R4X20005399401").append("status", "DISCONNECTED")
				.append("status_at", Date.from(Instant.now())));
		
		initialStausHistData.add(new Document("vehicle_id", "VLUR4X20009093588").append("status", "DISCONNECTED")
				.append("status_at", Date.from(Instant.now())));
		
		initialStausHistData.add(new Document("vehicle_id", "VLUR4X20009048066").append("status", "DISCONNECTED")
				.append("status_at", Date.from(Instant.now())));
		
		initialStausHistData.add(new Document("vehicle_id", "YS2R4X20005388011").append("status", "DISCONNECTED")
				.append("status_at", Date.from(Instant.now())));
		
		initialStausHistData.add(new Document("vehicle_id", "YS2R4X20005387949").append("status", "DISCONNECTED")
				.append("status_at", Date.from(Instant.now())));
		
		initialStausHistData.add(new Document("vehicle_id", "YS2R4X20005387055").append("status", "DISCONNECTED")
				.append("status_at", Date.from(Instant.now())));
		
		connectionStatusHistorycollection.insertMany(initialStausHistData);
		
		
		
		
	}

}
