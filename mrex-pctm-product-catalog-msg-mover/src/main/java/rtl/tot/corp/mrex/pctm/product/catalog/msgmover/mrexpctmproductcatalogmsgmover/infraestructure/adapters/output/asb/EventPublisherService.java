package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.infraestructure.adapters.output.asb;
import lombok.extern.slf4j.Slf4j;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.Event;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.EventBuilder;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.EventType;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider.EventPublisher;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.infra.exception.InvalidParameterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Slf4j
@Component
public class EventPublisherService {

    
    private final RestTemplate restTemplate;
    private final String apiGCPUrl;

    @Autowired
    public EventPublisherService(
                           final RestTemplate restTemplate,
                           @Value("${api.pubsub.gcp.endpoint}") final String apiGCPUrl) {
        this.apiGCPUrl = apiGCPUrl;
        this.restTemplate = restTemplate;
    }
    
    
  
    public boolean publish(final String event) {
     
        try {
        	//restTemplate.
            log.info("Event published: " + event);
        } catch (Exception e) {
            log.error("Event could not be send. Cause: " + e.getMessage());
        }
        return true;
    }

}
