package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.adapters.input.asb;

import lombok.extern.slf4j.Slf4j;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.Event;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.EventHandler;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.infraestructure.adapters.output.asb.EventPublisherService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ASBToCosmosDBEventHandler implements EventHandler {

	@Autowired
	EventPublisherService publisher;

	public ASBToCosmosDBEventHandler() {
	}

	@Override
	public void processEvent(Event event) {

		try {
			if (publisher.publish(event)) {
				log.info("[" + event.getEntityType() + " (" + event.getEntityId() + ")]: " + event.getEventType());
				log.info("Published Product Created" + event.toString());
			} else {
				log.error("Unable to send Event: " + event);
			}			

		} catch (Exception e) {
			log.error("Error publishing Event" + event.getMetadata());
		}

	}

}
