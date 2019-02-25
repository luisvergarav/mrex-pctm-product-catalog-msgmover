package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider.azureservicebus;

import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.SubscriptionClient;

import lombok.extern.slf4j.Slf4j;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.Event;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.EventHandler;

import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class AzureEventHandler implements IMessageHandler {

    private final SubscriptionClient subscriptionClient;
    private final EventHandler eventHandler;
    private List<String> eventType;

    AzureEventHandler(final SubscriptionClient subscriptionClient, final List<String> eventType, final EventHandler eventHandler) {
        Assert.notNull(subscriptionClient, "subscriptionClient must not be null");
        Assert.notNull(eventHandler, "eventHandler must not be null");

        this.subscriptionClient = subscriptionClient;
        this.eventHandler = eventHandler;
        this.eventType = eventType;
    }

    @Override
    public CompletableFuture<Void> onMessageAsync(IMessage message) {
        Assert.notNull(message, "message must not be null");

        if (eventType == null
                || eventType.isEmpty()
                || eventType.contains("*")
                || eventType.contains(message.getLabel())) {

            AzureEventConverter converter = new AzureEventConverter();
            final Event event = converter.fromMessage(message);

            eventHandler.processEvent(event);
        }

        //return subscriptionClient.completeAsync(message.getLockToken());
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void notifyException(Throwable exception, ExceptionPhase phase) {
        log.error(phase + "-" + exception.getMessage());
    }

}
