package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.UUID;

import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.infra.exception.InvalidParameterException;


/**
 * EventBuilder as Steps builder
 *
 * This kind of builder guides the user in the creation of
 * an Event in a consistent fashion. Avoids common issues
 * of the traditional Builder pattern:
 *  - It does not guide the user through the creation.
 *  - A user can always call the build method in any moment,
 *    even without the needed information.
 *  - There is no way to guide the user from a creation path
 *    instead of another based on conditions.
 *  - There is always the risk to leave your object in an
 *    inconsistent state.
 *  - All methods are always available, leaving the responsibility
 *    of which to use and when to use to the user who did not write the API.
 *
 * @author Javier G. Scappini <djgayoso@falabella.cl>
 */
public class EventBuilder {

    private EventBuilder() {}

    public static EventIdStep newBuilder() {
        return new EventBuilderSteps();
    }

    /**
     * Builder steps in the order they should be called.
     */
    public interface EventIdStep {
        EventTypeStep generateEventId() throws InvalidParameterException;
        EventTypeStep eventId(String eventId) throws InvalidParameterException;
    }
    public interface EventTypeStep { EntityIdStep eventType(String eventType) throws InvalidParameterException; }
    public interface EntityIdStep { EntityTypeStep entityId(String entityId) throws InvalidParameterException; }
    public interface EntityTypeStep { DateTimeStep entityType(String entityType) throws InvalidParameterException; }
    public interface DateTimeStep {
        VersionStep currentDateTime() throws InvalidParameterException;
        VersionStep dateTime(LocalDateTime dateTime) throws InvalidParameterException;
    }
    public interface VersionStep { CountryStep version(String version) throws InvalidParameterException; }
    public interface CountryStep { CommerceStep country(String country) throws InvalidParameterException; }
    public interface CommerceStep { ChannelStep commerce(String commerce) throws InvalidParameterException; }
    public interface ChannelStep { DomainStep channel(String channel) throws InvalidParameterException; }
    public interface DomainStep { CapabilityStep domain(String domain) throws InvalidParameterException; }
    public interface CapabilityStep { MimeTypeStep capability(String capability) throws InvalidParameterException; }
    public interface MimeTypeStep { MetadataStep mimeType(String mimeType) throws InvalidParameterException; }
    public interface MetadataStep { BuildStep metadata(String metadata) throws InvalidParameterException; }
    public interface BuildStep { Event build() throws InvalidParameterException; }

    private static class EventBuilderSteps
            implements  EventIdStep, EventTypeStep, EntityIdStep,
            EntityTypeStep, DateTimeStep, VersionStep, CountryStep,
            CommerceStep, ChannelStep, DomainStep, CapabilityStep,
            MimeTypeStep, MetadataStep, BuildStep {

        private static final String ID_PATTERN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
        private String eventId, eventType, entityId, entityType, version,
                       country, commerce, domain, capability, channel,
                       mimeType, metadata;
        private LocalDateTime datetime;

        public EventTypeStep generateEventId() throws InvalidParameterException {
            return eventId(UUID.randomUUID().toString());
        }

        public EventTypeStep eventId(final String eventId) throws InvalidParameterException {
            if (eventId == null || !eventId.matches(ID_PATTERN)) {
                throw new InvalidParameterException("Invalid parameter for eventId: " + eventId);
            }

            this.eventId = eventId;
            return this;
        }

        public EntityIdStep eventType(final String eventType) throws InvalidParameterException {
            if (eventType == null || eventType.isEmpty()) {
                throw new InvalidParameterException("Invalid parameter for eventType: " + eventType);
            }

            this.eventType = eventType;
            return this;
        }

        public EntityTypeStep entityId(final String entityId) throws InvalidParameterException {
            if (entityId == null || entityId.isEmpty()) {
                throw new InvalidParameterException("Invalid parameter for entityId.");
            }

            this.entityId = entityId;
            return this;
        }

        public DateTimeStep entityType(final String entityType) throws InvalidParameterException {
            if (entityType == null || entityType.isEmpty()) {
                throw new InvalidParameterException("Invalid parameter for entityType.");
            }

            this.entityType = entityType;
            return this;
        }

        public VersionStep currentDateTime() throws InvalidParameterException {
            return dateTime(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        }

        public VersionStep dateTime(final LocalDateTime datetime) throws InvalidParameterException {
            if (datetime == null) {
                throw new InvalidParameterException("Invalid parameter for datetime.");
            }

            this.datetime = datetime;
            return this;
        }

        public CountryStep version(final String version) throws InvalidParameterException {
            if (version == null || version.isEmpty()) {
                throw new InvalidParameterException("Invalid parameter for version.");
            }

            this.version = version;
            return this;
        }

        public CommerceStep country(final String country) throws InvalidParameterException {
            if (country == null || country.isEmpty()) {
                throw new InvalidParameterException("Invalid parameter for country.");
            }

            this.country = country;
            return this;
        }

        public ChannelStep commerce(final String commerce) throws InvalidParameterException {
            if (commerce == null || commerce.isEmpty()) {
                throw new InvalidParameterException("Invalid parameter for commerce.");
            }

            this.commerce = commerce;
            return this;
        }

        public DomainStep channel(final String channel) throws InvalidParameterException {
            if (channel == null || channel.isEmpty()) {
                throw new InvalidParameterException("Invalid parameter for channel.");
            }

            this.channel = channel;
            return this;
        }

        @Override
        public CapabilityStep domain(String domain) throws InvalidParameterException {
            if (domain == null || domain.isEmpty()) {
                //throw new InvalidParameterException("Invalid parameter for domain.");
            }

            //this.domain = domain;
            return this;
        }

        @Override
        public MimeTypeStep capability(String capability) throws InvalidParameterException {
            if (capability == null || capability.isEmpty()) {
                //throw new InvalidParameterException("Invalid parameter for capability.");
            }

            //this.capability = capability;
            return this;
        }

        public MetadataStep mimeType(final String mimeType) throws InvalidParameterException {
            if (mimeType == null || mimeType.isEmpty()) {
                //throw new InvalidParameterException("Invalid parameter for mimeType.");
            }

            //this.mimeType = mimeType;
            return this;
        }

        public BuildStep metadata(final String metadata) {
            this.metadata = metadata;
            return this;
        }

        public Event build() {
            return new Event(eventId, eventType, entityId, entityType, datetime,
                             version, country, commerce, channel, domain,
                             capability, mimeType, metadata);
        }

    }

    /**
     * Build an {@link Event} from a {@link Map} object
     *
     * @author Cristi&#225;n Y&#225;&#241;ez P. <ceyanezp@falabella.cl>
     */
    public static BuildStep fromMap(Map<String, String> map) {
        return new MapBuilderSteps(map);
    }

    private static class MapBuilderSteps implements BuildStep {
        private final Map<String, String> map;

        MapBuilderSteps(Map<String, String> map) {
            this.map = map;
        }

        public Event build() throws InvalidParameterException {
            if (map == null) {
                throw new InvalidParameterException("Source map can not be null.");
            }

            if (map.isEmpty()) {
                throw new InvalidParameterException("Source map can not be empty");
            }

            LocalDateTime dateTime;
            long timestamp;

            try {
                timestamp = Long.valueOf(map.get(EventProperty.TIMESTAMP.toString()));
            } catch (NumberFormatException e) {
                timestamp = 0;
            }

            try {
                dateTime = LocalDateTime.parse(map.get(EventProperty.DATETIME.toString()));
            } catch (DateTimeParseException |NullPointerException e) {
                dateTime =
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
            }

            return EventBuilder.newBuilder()
                    .eventId(map.get(EventProperty.EVENT_ID.toString()))
                    .eventType(map.get(EventProperty.EVENT_TYPE.toString()))
                    .entityId(map.get(EventProperty.ENTITY_ID.toString()))
                    .entityType(map.get(EventProperty.ENTITY_TYPE.toString()))
                    .dateTime(dateTime)
                    .version(map.get(EventProperty.VERSION.toString()))
                    .country(map.get(EventProperty.COUNTRY.toString()))
                    .commerce(map.get(EventProperty.COMMERCE.toString()))
                    .channel(map.get(EventProperty.CHANNEL.toString()))
                    .domain(map.get(EventProperty.DOMAIN.toString()))
                    .capability(map.get(EventProperty.CAPABILITY.toString()))
                    .mimeType(map.get(EventProperty.MIME_TYPE.toString()))
                    .metadata(map.get(EventProperty.METADATA.toString()))
                    .build();
        }

    }

}