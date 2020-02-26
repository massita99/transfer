package configuration;

import io.micronaut.test.annotation.MicronautTest;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import transfer.model.Account;

import javax.inject.Inject;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static transfer.model.Account.ACCOUNT;

@MicronautTest
public class JooqTest {

    @Inject
    private DSLContext dsl;

    @Inject
    private ModelMapper modelMapper;

    @Test
    void testDbInitialTablesExist() {
        //Just do not throw
        dsl.selectCount()
                .from(ACCOUNT)
                .fetchOne(0, int.class);


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

    @Test
    void testDbLockingQueries() {
        //Given
        var account = new Account();
        var record = modelMapper.map(account, Map.class);
        dsl.transaction(configuration -> {
            DSL.using(configuration).insertInto(table(ACCOUNT))
                    .set(record)
                    .execute();
        });

        dsl.transaction(configuration -> {
            //When
            //Lock in current transaction
            DSL.using(configuration).selectFrom(ACCOUNT)
                    .where(field(Account.Fields.id).eq(account.getId()))
                    .forUpdate()
                    .fetch();

            //Then
            //Try to get the same recs in different transaction
            assertThatThrownBy(() -> dsl.selectFrom(ACCOUNT)
                    .where(field(Account.Fields.id).eq(account.getId()))
                    .forUpdate()
                    .fetch());

            // Implicit commit executed here
        });
    }


}
