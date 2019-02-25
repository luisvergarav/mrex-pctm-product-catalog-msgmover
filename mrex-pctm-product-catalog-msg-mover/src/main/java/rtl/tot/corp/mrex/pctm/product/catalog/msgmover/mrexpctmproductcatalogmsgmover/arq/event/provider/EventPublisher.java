package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider;

import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.Event;

@FunctionalInterface
public interface EventPublisher {
    boolean publish(Event event);
}
