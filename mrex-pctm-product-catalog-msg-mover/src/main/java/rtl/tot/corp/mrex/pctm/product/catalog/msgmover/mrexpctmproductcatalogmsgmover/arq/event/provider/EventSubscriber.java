package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider;

import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.EventHandler;

@FunctionalInterface
public interface EventSubscriber {
    boolean registerEventHandler(EventHandler eventHandler);
}
