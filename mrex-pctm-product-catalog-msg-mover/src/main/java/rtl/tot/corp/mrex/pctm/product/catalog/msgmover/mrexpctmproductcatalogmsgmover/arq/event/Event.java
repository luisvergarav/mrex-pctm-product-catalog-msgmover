package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Event implements Comparable<Event> {

    private String eventId;
    private String eventType;
    private String entityId;
    private String entityType;
    private long timestamp;
    private LocalDateTime datetime;
    private String version;
    private String country;
    private String commerce;
    private String channel;
    private String domain;
    private String capability;
    private String mimeType;
    private String metadata;

    private final ObjectMapper mapper = new ObjectMapper()
                                            .registerModule(new ParameterNamesModule())
                                            .registerModule(new Jdk8Module())
                                            .registerModule(new JavaTimeModule());

    Event(final String eventId, final String eventType, final String entityId,
                 final String entityType, final LocalDateTime datetime,
                 final String version, final String country, final String commerce,
                 final String channel, final String domain, final String capability,
                 final String mimeType, final String metadata) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.entityId = entityId;
        this.entityType = entityType;
        this.datetime = datetime;
        this.timestamp = datetime.atZone(ZoneOffset.UTC).toEpochSecond();
        this.version = version;
        this.country = country;
        this.commerce = commerce;
        this.channel = channel;
        this.domain = domain;
        this.capability = capability;
        this.mimeType = mimeType;
        this.metadata = metadata;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public String getVersion() {
        return version;
    }

    public String getCountry() {
        return country;
    }

    public String getCommerce() {
        return commerce;
    }

    public String getChannel() {
        return channel;
    }

    public String getDomain() {
        return domain;
    }

    public String getCapability() {
        return capability;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getMetadata() {
        return metadata;
    }

    public Map<String, String> asMap() {
        Map<String, String> eventMap = new HashMap<>();
        eventMap.put(EventProperty.EVENT_ID.toString(), getEventId());
        eventMap.put(EventProperty.EVENT_TYPE.toString(), getEventType());
        eventMap.put(EventProperty.ENTITY_ID.toString(), getEntityId());
        eventMap.put(EventProperty.ENTITY_TYPE.toString(), getEntityType());
        eventMap.put(EventProperty.TIMESTAMP.toString(), String.valueOf(getTimestamp()));
        eventMap.put(EventProperty.DATETIME.toString(), getDatetime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        eventMap.put(EventProperty.VERSION.toString(), getVersion());
        eventMap.put(EventProperty.COUNTRY.toString(), getCountry());
        eventMap.put(EventProperty.COMMERCE.toString(), getCommerce());
        eventMap.put(EventProperty.CHANNEL.toString(), getChannel());
        eventMap.put(EventProperty.DOMAIN.toString(), getDomain());
        eventMap.put(EventProperty.CAPABILITY.toString(), getCapability());
        eventMap.put(EventProperty.MIME_TYPE.toString(), getMimeType());
        eventMap.put(EventProperty.METADATA.toString(), getMetadata());
        return eventMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return getTimestamp() == event.getTimestamp() &&
                Objects.equals(getEventId(), event.getEventId()) &&
                Objects.equals(getEventType(), event.getEventType()) &&
                Objects.equals(getEntityId(), event.getEntityId()) &&
                Objects.equals(getEntityType(), event.getEntityType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventId(), getEventType(), getEntityId(), getEntityType(), getTimestamp());
    }

    @Override
    public String toString() {
        return toJSON();
    }

    private String toJSON() {
        try {

            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }

    @Override
    public int compareTo(final Event event) {
        if (event == null) {
            return Integer.MIN_VALUE;
        }

        return Long.compare(timestamp, event.timestamp);
    }
}
