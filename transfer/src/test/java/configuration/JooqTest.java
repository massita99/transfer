package configuration;

import io.micronaut.test.annotation.MicronautTest;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.TableRecord;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import transfer.model.Account;

import static org.assertj.core.api.Assertions.*;
import static org.jooq.impl.DSL.dropSequence;
import static org.jooq.impl.DSL.table;
import static transfer.model.Account.ACCOUNT;

import javax.inject.Inject;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@MicronautTest
public class JooqTest {

    @Inject
    private DSLContext dsl;

    @Inject
    private ModelMapper modelMapper;

    @Test
    void testDbInitialTablesExist() {
        //When
        int count = dsl.selectCount()
                .from(ACCOUNT)
                .fetchOne(0, int.class);

        //Then
        assertThat(count).isEqualTo(0);

    }

    @Test
    void testDbBasicQueries() {
        //Given
        var account = new Account();
        var record = modelMapper.map(account, Map.class);

        dsl.insertInto(table(ACCOUNT))
                .set(record)
                .execute();
        //When
        var accounts = dsl.selectFrom(ACCOUNT)
                .fetch()
                .stream()
                .map(e -> modelMapper.map(e, Account.class))
                .collect(Collectors.toList());


        //Then
        assertThat(accounts).contains(account);
        assertThat(accounts).extracting(Account::getCreated)
                .contains(account.getCreated());

    }


}
