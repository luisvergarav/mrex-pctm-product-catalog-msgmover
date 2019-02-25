package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event;

public enum EventProperty {
    EVENT_ID ("eventId"),
    EVENT_TYPE ("eventType"),
    ENTITY_ID ("entityId"),
    ENTITY_TYPE ("entityType"),
    TIMESTAMP ("timestamp"),
    DATETIME ("datetime"),
    VERSION ("version"),
    COUNTRY ("country"),
    COMMERCE ("commerce"),
    CHANNEL ("channel"),
    DOMAIN ("domain"),
    CAPABILITY ("capability"),
    MIME_TYPE ("mimeType"),
    METADATA ("metadata");

    private final String name;

    EventProperty(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}