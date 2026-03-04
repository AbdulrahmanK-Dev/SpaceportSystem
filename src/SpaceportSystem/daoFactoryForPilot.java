package SpaceportSystem;

public class daoFactoryForPilot {

    private static final RocketDAO rocketDAO = new RocketDAO(); 
    private static final LaunchPadDAO padDAO = new LaunchPadDAO();
    private static final tripDAO tripDAO = new tripDAO(rocketDAO, padDAO);
    
    // Public methods to get the configured DAOs
    public static tripDAO getTripDAO() {
        return tripDAO;
    }


 
}

