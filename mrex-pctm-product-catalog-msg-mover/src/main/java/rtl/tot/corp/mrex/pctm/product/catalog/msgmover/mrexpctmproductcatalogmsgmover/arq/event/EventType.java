package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event;

public enum EventType {
    PRODUCT_DELETED("productDeleted"),
    PRODUCT_CREATED("productCreated"),
    PRODUCT_UPDATED("productUpdated"),
    PRODUCT_STATEUPDATED("productStateUpdated");
    private final String name;

    EventType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

