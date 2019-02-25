package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event;

@FunctionalInterface
public interface EventHandler {
    void processEvent(final Event event);
}
