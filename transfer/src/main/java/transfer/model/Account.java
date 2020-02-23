package transfer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.now;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldNameConstants
public class Account {

    public static final String TABLE = "ACCOUNT";

    @EqualsAndHashCode.Include
    final private UUID id;

    private BigDecimal balance;

    private final Instant created;


    public Account() {
        this.id = UUID.randomUUID();
        this.balance = BigDecimal.ZERO;
        this.created = now();
    }

}
