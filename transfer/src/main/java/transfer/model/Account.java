package transfer.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import static java.time.Instant.now;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldNameConstants
public class Account {

    public static final String ACCOUNT = "ACCOUNT";

    @EqualsAndHashCode.Include
    private String id;

    private BigDecimal balance;

    private Instant created;

    public Account() {
        this.id = UUID.randomUUID().toString();
        this.balance = BigDecimal.ZERO;
        this.created = now();
    }

}
