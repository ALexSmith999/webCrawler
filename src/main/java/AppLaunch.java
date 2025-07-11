import org.apache.logging.log4j.Logger;

public class AppLaunch {
    ;
    static public void start (Logger logger, int NUM_OF_THREADS, int DEPTH, int TIME){
        logger.info("Web server has been started");
        logger.info("The number of threads is equal to : {}", NUM_OF_THREADS);
        logger.info("The hierarchy depth tree is equal to : {}", DEPTH);
        logger.info("Response time has been limited to : {} seconds", TIME);
    }
    static public void stop (Logger logger){
        logger.info("Web server has been stopped");
    }
}
