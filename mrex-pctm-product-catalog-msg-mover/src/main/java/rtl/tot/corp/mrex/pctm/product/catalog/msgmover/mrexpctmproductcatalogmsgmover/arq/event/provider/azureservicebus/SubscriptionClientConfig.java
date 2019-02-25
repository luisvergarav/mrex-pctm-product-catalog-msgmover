package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider.azureservicebus;

import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

import lombok.extern.slf4j.Slf4j;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.EventHandler;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider.EventSubscriber;

import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class SubscriptionClientConfig implements EventSubscriber {

    private SubscriptionClient subscriptionClient;

    @Value("${subscriber.endpoint}")
    private String endpoint;

    @Value("${subscriber.eventType: *}")
    private String[] eventType;

    @Value("${subscriber.azure-service-bus.topic}")
    private String topic;

    @Value("${subscriber.azure-service-bus.subscription}")
    private String subscription;

    @PostConstruct
    void postConstruct() throws InterruptedException, ServiceBusException {

        final String subscription = new StringBuffer(topic)
                .append("/subscriptions/")
                .append(this.subscription)
                .toString();
        subscriptionClient =
                new SubscriptionClient(new ConnectionStringBuilder(endpoint, subscription), ReceiveMode.PEEKLOCK);
    }

    public SubscriptionClient getSubscriptionClient() {
        return subscriptionClient;
    }

    public List<String> getEventType() {
        return Arrays.asList(eventType);
    }

    @Override
    public boolean registerEventHandler(EventHandler eventHandler) {
        if (eventHandler == null) {
            return false;
        }

        boolean registeredEventHandler = false;
        try {
            subscriptionClient.registerMessageHandler(
                    new AzureEventHandler(subscriptionClient, getEventType(), eventHandler)
            );
            registeredEventHandler = true;
        } catch (InterruptedException | ServiceBusException e) {
            log.error("Error registering event handler. " + e.getMessage());
        }
        return registeredEventHandler;
    }
}
