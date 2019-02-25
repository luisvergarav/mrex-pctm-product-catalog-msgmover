package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider;

import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.Event;

public interface EventConverter<T> {
    T toMessage(Event event);
    Event fromMessage(T message);
}
