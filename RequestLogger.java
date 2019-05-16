
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

public class RequestLogger {

    String SCOPE = StorageScopes.DEVSTORAGE_FULL_CONTROL;
    String DATA_STORE_SCOPE1 = DatastoreScopes.CLOUD_PLATFORM;
    String DATA_STORE_SCOPE2 = DatastoreScopes.DATASTORE;
    int MAX_REQUESTS = 30;
    boolean limitReached = false;
    
    public Datastore auth() throws FileNotFoundException, IOException{
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/google/project.JSON"))
                                                .createScoped(Arrays.asList(DATA_STORE_SCOPE1,
                                                                                        DATA_STORE_SCOPE2));
        DatastoreOptions options = DatastoreOptions.newBuilder().setCredentials(credentials).build();
        Datastore service = options.getService();
        return service;
                
    }
    public Key createKey(Datastore service, String kind, String key){
        return  service.newKeyFactory().setKind(kind).newKey(key);
    }
    public Entity createEntity(Datastore service, String kind, String key, String enName, String enKey){
        Key requestKey = createKey(service, kind, key);
        return  Entity.newBuilder(requestKey).set(enKey , enName).build();
    }
    public void saveRequest(String request, String count) throws FileNotFoundException, IOException{
        Datastore service = this.auth();
        service.put(createEntity(service, "request-save", "request-save-key", count, request));        
    }
    public void increaseMonthlyCount(int currentMonthlyCount) throws FileNotFoundException, IOException{
        Datastore service = this.auth();
        service.put(createEntity(service, "monthly-count", "monthly-count-key", "monthly-count", new Integer(currentMonthlyCount+1).toString()));
    }
    public int retrieveMonthlyCount() throws FileNotFoundException, IOException{
        Datastore service = this.auth();
        return new Integer(service.get(createKey(service, "monthly-count", "monthly-count-key")).getString("monthly-count"));        
    }
    public boolean limitReached() throws FileNotFoundException, IOException{
        return MAX_REQUESTS <= retrieveMonthlyCount();
    }    

}
