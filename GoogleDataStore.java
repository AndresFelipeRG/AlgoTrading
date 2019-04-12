
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.api.services.storage.StorageScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.datastore.v1.DatastoreScopes;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;


public class GoogleDataStore {

    String SCOPE = StorageScopes.DEVSTORAGE_FULL_CONTROL;
    String DATA_STORE_SCOPE1 = DatastoreScopes.CLOUD_PLATFORM;
    String DATA_STORE_SCOPE2 = DatastoreScopes.DATASTORE;
    
    public void auth() throws FileNotFoundException, IOException{
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/google/project.JSON"))
                                                .createScoped(Arrays.asList(DATA_STORE_SCOPE1,
                                                                                        DATA_STORE_SCOPE2));
        DatastoreOptions options = DatastoreOptions.newBuilder().setCredentials(credentials).build();
        Datastore service = options.getService();
        String kind = "Task";
        String name = "sampletask1";
        Key taskKey = service.newKeyFactory().setKind(kind).newKey(name);
        
        Entity task = Entity.newBuilder(taskKey).set("description","Buy milk").build();
        service.put(task);
        System.out.printf("Saved %s: %s%n", task.getKey().getName(), task.getString("description"));
        Entity retrieved = service.get(taskKey);
        System.out.printf("Retrieved %s: %s%n", taskKey.getName(), retrieved.getString("description"));
        
        
        
    }
    
    public static void main(String [] args) throws FileNotFoundException, IOException{
        
        
        
        new GoogleDataStore().auth();
        
        
    }
    
    
}
