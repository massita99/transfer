package transfer.configuration.converter;

import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * Provide ability to convert data from {@link Record} that store in Database as Timestamp to Instant
 * {@link ModelMapper} dont provide builtin possibility to map database timestamp field to Instant field
 * Assume that all DB Timestamp Fields mapped to Instant otherwise throw {@link UnsupportedOperationException}
 */
public class InstantConverter implements ConditionalConverter<Object, Instant> {

    @Override
    public Instant convert(MappingContext<Object, Instant> context) {
        if (context.getSource() instanceof Timestamp) {
            return ((Timestamp) context.getSource()).toInstant();
        }
        throw new UnsupportedOperationException(String.format("Field %s should be Instant types",
                context.getTypeMapName()));
    }

    @Override
    public MatchResult match(Class<?> sourceType, Class<?> destinationType) {
        return destinationType.equals(Instant.class) ? MatchResult.PARTIAL : MatchResult.NONE;
    }
}
