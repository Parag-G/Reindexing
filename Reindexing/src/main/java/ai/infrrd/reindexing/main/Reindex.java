package ai.infrrd.reindexing.main;

public class Reindex
{


    public static void main( String args[] )
    {

        System.out.println( "Starting Filter Reindexing!!!" );
        ReindexFilter.filerReindex();
        System.out.println( "Filter Reindexing Ended Successfully!!!" );

        System.out.println( "Starting Aggregation Reindexing!!!" );
        ReindexAggregation.aggregationReindex();
        System.out.println( "Aggregation Reindexing Ended Successfully!!!" );

    }


}
