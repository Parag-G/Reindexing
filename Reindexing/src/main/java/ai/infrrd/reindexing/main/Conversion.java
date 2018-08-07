package ai.infrrd.reindexing.main;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


public class Conversion
{

    public static JsonElement convert( JsonElement jsonTree )
    {
        if ( null != jsonTree.getAsJsonObject().getAsJsonArray( "lineitems" ) ) {
            JsonArray asJsonArray = jsonTree.getAsJsonObject().getAsJsonArray( "lineitems" );
            jsonTree.getAsJsonObject().remove( "lineitems" );
            jsonTree.getAsJsonObject().add( "lineitems", transform( asJsonArray ) );
        }
        return jsonTree;
    }


    private static JsonArray transform( JsonArray array )
    {
        JsonArray arrayToSend = new JsonArray();
        for ( JsonElement element : array ) {
            Map<String, String> cols = new Gson().fromJson( element.getAsJsonObject(), Map.class );
            Map<String, Object> obj = new HashMap<String, Object>();
            int counter = 1;
            for ( Map.Entry<String, String> entry : cols.entrySet() ) {
                InvoiceColumns col = new InvoiceColumns();
                col.setName( String.valueOf( entry.getKey() ) );
                col.setValue( String.valueOf( entry.getValue() ) );
                obj.put( "column" + counter, col );
                counter++;
            }
            arrayToSend.add( new Gson().toJsonTree( obj ) );
        }
        return arrayToSend;
    }

}
