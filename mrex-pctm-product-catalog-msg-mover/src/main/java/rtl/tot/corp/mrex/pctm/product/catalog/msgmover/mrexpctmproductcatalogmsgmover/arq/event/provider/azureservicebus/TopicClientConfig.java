package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider.azureservicebus;

import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.TopicClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

import lombok.extern.slf4j.Slf4j;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.Event;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider.EventPublisher;

import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

@Slf4j
public class TopicClientConfig implements EventPublisher {

    private TopicClient topicClient;

    @Value("${publisher.endpoint}")
    private String endpoint;

    @Value("${publisher.azure-service-bus.topic}")
    private String topic;


    @PostConstruct
    void postConstruct() throws ServiceBusException, InterruptedException {
        //Assert.hasText(endpoint, "invalid endpoint");
        //Assert.hasText(topic, "invalid topic");

        topicClient = new TopicClient(new ConnectionStringBuilder(endpoint, topic));
    }

    public TopicClient getTopicClient() {
        return topicClient;
    }

    @Override
    public boolean publish(Event event) {
        if (event == null) {
            return false;
        }

        boolean emitted = false;
        try {
            topicClient.send(transformEventToMessage(event));

            emitted = true;
        } catch (InterruptedException | ServiceBusException e) {
            log.error("Error sending event. " + e.getMessage());
        }
        return emitted;
    }

    protected IMessage transformEventToMessage(final Event event) {
        final AzureEventConverter converter = new AzureEventConverter();
        final IMessage message = converter.toMessage(event);

        message.setLabel(event.getEventType());

        return message;
    }
}
