package neospider.mngr.integration.server;


public final class RequestCorrelation {
    private RequestCorrelation() {

    }
    static final String CORRELATION_ID = "x-correlationId";

    private static final ThreadLocal<String> ID = new ThreadLocal<>();


    public static String getId() {
        return ID.get();
    }

    public static void setId(String correlationId) {
        ID.set(correlationId);
    }
}
