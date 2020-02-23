package configuration;

import io.micronaut.test.annotation.MicronautTest;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import transfer.model.Account;

import static org.assertj.core.api.Assertions.*;

import javax.inject.Inject;

@MicronautTest
public class JooqTest {

    @Inject
    private DSLContext dsl;

    @Test
    void testDbInitialTablesExist() {
        //When
        int count = dsl.selectCount()
                .from(Account.TABLE)
                .fetchOne(0, int.class);

        //Then
        assertThat(count).isEqualTo(0);

    }
}
