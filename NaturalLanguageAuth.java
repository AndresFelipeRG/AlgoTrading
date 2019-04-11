
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.services.language.v1beta2.CloudNaturalLanguageScopes;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.Sentiment;



public class NaturalLanguageAuth {
    
    public void auth() throws FileNotFoundException, IOException{
        
        
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/google/project.JSON"))
                                                .createScoped(Arrays.asList(CloudNaturalLanguageScopes.CLOUD_LANGUAGE,
                                                                       CloudNaturalLanguageScopes.CLOUD_PLATFORM));
  
        CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
        LanguageServiceSettings settings = LanguageServiceSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
        LanguageServiceClient language = LanguageServiceClient.create(settings);
        
        String text = "Testing natural language API google BECAUSE IS AMAZING!";
        Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
        
        Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
        
        System.out.printf("Text: %s%n", text);
        System.out.printf("Sentiment: %s, %s%n", sentiment.getScore(), sentiment.getMagnitude());
        
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        
        new NaturalLanguageAuth().auth();
    }
    
}
