package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider.azureservicebus;

import com.microsoft.azure.servicebus.primitives.ServiceBusException;

import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider.EventPublisher;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider.EventSubscriber;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class AzureServiceBusConfig {

    @Lazy
    @Bean
    @ConditionalOnProperty(prefix = "publisher", name = "azure-service-bus.topic")
    public EventPublisher asbPublisher() throws ServiceBusException, InterruptedException {
        return new TopicClientConfig();
    }

    @Lazy
    @Bean
    @ConditionalOnProperty(prefix = "subscriber", name = "azure-service-bus.topic")
    public EventSubscriber asbSubscriber() throws ServiceBusException, InterruptedException {
        return new SubscriptionClientConfig();
    }
}
