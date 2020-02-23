package transfer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.now;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

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
