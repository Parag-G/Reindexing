package ai.infrrd.reindexing.main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

//import ai.infrrd.resultanalyzer.exception.RuntimeParamParsingException;


/**
 * Functionality on environment constants
 * @author nishit
 *
 */
public class EnvConstants
{

    //    private static final Logger LOG = LoggerFactory.getLogger( EnvConstants.class );

    public static final String PROFILE = "profile";
    public static final String CLUSTER = "cluster";

    public static final String SUBMIT_TOPOLOGY = "submit-topo";
    public static final String LOCAL_TOPOLOGY = "local-topo";

    public static final String PROFILE_LOCAL = "local";
    public static final String PROFILE_DEV = "dev";
    public static final String PROFILE_DEMO = "demo";
    public static final String PROFILE_STAGE = "stage";
    public static final String PROFILE_PRELIVE = "prelive";
    public static final String PROFILE_PROD = "prod";

    private static Map<String, String> runtimeParamsHashmap = new HashMap<String, String>();

    private static final List<String> AVAILABLE_PROFILES = Arrays.asList( PROFILE_LOCAL, PROFILE_DEV, PROFILE_DEMO,
        PROFILE_STAGE, PROFILE_PRELIVE, PROFILE_PROD );


    private EnvConstants()
    {}


    public static String getProfile()
    {
        return runtimeParamsHashmap.get( PROFILE );
    }


    public static String getCluster()
    {
        return runtimeParamsHashmap.get( CLUSTER );
    }


    public static void runtimeParams( String... params )
    {
        if ( runtimeParamsHashmap.isEmpty() ) {
            if ( params.length > 0 ) {
                // first param should be environment
                if ( !AVAILABLE_PROFILES.contains( params[0] ) ) {
                    //                    LOG.warn( "Invalid profile passed {}", params[0] );
                    //                    throw new RuntimeParamParsingException( "Invalid profile " + params[0] );
                }
                runtimeParamsHashmap.put( PROFILE, params[0] );
                // Second param should be for cluster deployment
                if ( params.length > 1 ) {
                    if ( params[1].equals( SUBMIT_TOPOLOGY ) ) {
                        runtimeParamsHashmap.put( CLUSTER, SUBMIT_TOPOLOGY );
                    } else {
                        runtimeParamsHashmap.put( CLUSTER, LOCAL_TOPOLOGY );
                    }
                } else {
                    runtimeParamsHashmap.put( CLUSTER, LOCAL_TOPOLOGY );
                }
            } else {
                runtimeParamsHashmap.put( PROFILE, PROFILE_LOCAL );
                runtimeParamsHashmap.put( CLUSTER, LOCAL_TOPOLOGY );
            }
        }
    }


    public static Map<String, String> getRuntimeParamsMap()
    {
        return runtimeParamsHashmap;
    }
}