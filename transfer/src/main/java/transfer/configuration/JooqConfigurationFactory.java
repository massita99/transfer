package transfer.configuration;

import io.micronaut.context.annotation.EachBean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Parameter;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;

import javax.sql.DataSource;

@Factory
public class JooqConfigurationFactory {

    /**
     * Creates jOOQ {@link Configuration}.
     * It will configure it with available jOOQ provider beans with the same qualifier.
     *
     * @param name                   The data source name
     * @param dataSource             The {@link DataSource}

     * @return A {@link Configuration}
     */
    @EachBean(DataSource.class)
    public Configuration jooqConfiguration(
            @Parameter String name,
            DataSource dataSource
    ) {
        DefaultConfiguration configuration = new DefaultConfiguration();
        configuration.setDataSource(dataSource);
        return configuration;
    }

    /**
     * Created {@link DSLContext} based on {@link Configuration}
     *
     * @param configuration The {@link Configuration}
     * @return A {@link DSLContext}
     */
    @EachBean(Configuration.class)
    public DSLContext dslContext(Configuration configuration) {
        return new DefaultDSLContext(configuration);
    }

}
