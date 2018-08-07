package ai.infrrd.reindexing.main;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonElement;


public class ReindexAggregation
{

    private static String fromESAggregationIndexName;
    private static String toESAggregationIndexName;
    private static String esIndexType;

    static {
        LocalPropertyFileHandler.getInstance().prepare( EnvConstants.getProfile() );

        fromESAggregationIndexName = LocalPropertyFileHandler.getInstance()
            .getProperty( "application", "fromESAggregationIndexName" ).orElse( null );
        toESAggregationIndexName = LocalPropertyFileHandler.getInstance()
            .getProperty( "application", "toESAggregationIndexName" ).orElse( null );
        esIndexType = LocalPropertyFileHandler.getInstance().getProperty( "application", "esIndexType" ).orElse( "docs" );
    }


    public static void aggregationReindex()
    {


        if ( fromESAggregationIndexName == null || toESAggregationIndexName == null ) {
            System.out.println( "Index name is null, Exiting" );
            System.exit( 0 );
        }
        final Scroll scroll = new Scroll( TimeValue.timeValueMinutes( 1L ) );
        SearchRequest searchRequest = new SearchRequest( fromESAggregationIndexName );
        searchRequest.types( esIndexType );
        searchRequest.scroll( scroll );

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query( QueryBuilders.existsQuery( "status" ) );

        searchRequest.source( sourceBuilder ).scroll();

        SearchResponse searchResponse;
        String scrollId = null;

        RestHighLevelClient client = ElasticSearchConnector.getClient();

        try {
            searchResponse = client.search( searchRequest );
            try {
                scrollId = searchResponse.getScrollId();
            } catch ( Exception e ) {
                System.out.println( "Scroll id not found " + e );
            }
            SearchHit[] hits = searchResponse.getHits().getHits();
            int i = 1;
            while ( hits != null && hits.length > 0 ) {
                for ( SearchHit hit : hits ) {
                    System.out.println( i );
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                    System.out.println( sourceAsMap.get( "scanRequestId" ) );

                    JsonElement ele = new Gson().toJsonTree( sourceAsMap );

                    try {
                        IndexRequest request = new IndexRequest( toESAggregationIndexName, esIndexType,
                            sourceAsMap.get( "scanRequestId" ).toString() );
                        request.source( new Gson().toJson( ele ), XContentType.JSON );

                        client.index( request );
                    } catch ( Exception e ) {
                        System.out.println( "Error while putting data " + e );
                    }
                    i++;
                }
                try {
                    SearchScrollRequest searchScrollRequest = new SearchScrollRequest( scrollId );
                    searchScrollRequest.scroll( scroll );
                    searchResponse = client.searchScroll( searchScrollRequest );
                    scrollId = searchResponse.getScrollId();
                } catch ( Exception e ) {
                    System.out.println( "Error while scrolling " + e );
                }
                hits = searchResponse.getHits().getHits();
            }
        } catch ( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
