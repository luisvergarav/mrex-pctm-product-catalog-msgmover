package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider.azureservicebus;

import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.Message;

import lombok.extern.slf4j.Slf4j;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.Event;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.EventBuilder;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider.EventConverter;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.infra.exception.InvalidParameterException;

import org.springframework.util.Assert;

@Slf4j
public class AzureEventConverter implements EventConverter<IMessage> {

    @Override
    public IMessage toMessage(final Event event) {
        Assert.notNull(event, "event must not be null");

        final Message message =
                new Message(event.getEventId(), event.toString(), event.getMimeType());
        message.setProperties(event.asMap());

        return message;
    }

    @Override
    public Event fromMessage(IMessage message) {
        Assert.notNull(message, "message must not be null");

        // Parse received message and convert it to an Event
        Event event = null;

        try {
            event = EventBuilder.fromMap(message.getProperties()).build();
        } catch (InvalidParameterException e) {
            log.error("Cannot map received message to an event. " + e.getMessage());
        }

        return event;
    }

}
