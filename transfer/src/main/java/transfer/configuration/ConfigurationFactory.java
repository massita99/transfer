package transfer.configuration;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jooq.RecordValueReader;
import transfer.configuration.converter.AccountConverter;
import transfer.configuration.converter.InstantConverter;
import transfer.configuration.converter.TransactionConverter;

@Factory
public class ConfigurationFactory {

    /**
     * Created {@link ModelMapper} that can:
     * create POJO from {@link org.jooq.Record}
     * create {@link java.util.Map<org.jooq.Field, Object>} from Pojo to persist it
     * could convert {@link java.sql.Timestamp} to {@link java.time.Instant}
     *
     * @return A {@link ModelMapper}
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.CAMEL_CASE)
                .addValueReader(new RecordValueReader());

        mapper.addConverter(new AccountConverter());
        mapper.addConverter(new TransactionConverter());
        mapper.getConfiguration().getConverters().add(new InstantConverter());

        return mapper;
    }

}
