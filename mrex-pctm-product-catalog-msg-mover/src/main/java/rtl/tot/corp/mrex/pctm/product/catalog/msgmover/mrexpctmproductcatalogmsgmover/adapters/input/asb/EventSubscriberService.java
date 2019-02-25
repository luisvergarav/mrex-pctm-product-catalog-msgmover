package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.adapters.input.asb;

import org.springframework.stereotype.Service;

import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider.EventSubscriber;



@Service
public class EventSubscriberService {

    public EventSubscriberService(final EventSubscriber eventSubscriber,
                                  final ASBToCosmosDBEventHandler asbToCosmosDBEventHandler) {
        eventSubscriber.registerEventHandler(asbToCosmosDBEventHandler);
    }

}
