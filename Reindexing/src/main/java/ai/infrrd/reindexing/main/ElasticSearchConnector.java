package ai.infrrd.reindexing.main;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;


public class ElasticSearchConnector
{

    private static RestHighLevelClient client;
    private static String esHost;
    private static int esPort;

    static {

        LocalPropertyFileHandler.getInstance().prepare( EnvConstants.getProfile() );
        esHost = LocalPropertyFileHandler.getInstance().getProperty( "application", "host" ).orElse( "localhost" );

        String port = LocalPropertyFileHandler.getInstance().getProperty( "application", "port" ).orElse( "9200" );
        esPort = Integer.parseInt( port );

        client = new RestHighLevelClient( RestClient.builder( new HttpHost( esHost, esPort ) ) );

    }


    public static RestHighLevelClient getClient()
    {
        return client;
    }

}
