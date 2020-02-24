package transfer.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

import static java.time.Instant.now;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldNameConstants
public class Transaction {

    public static final String TRANSACTION = "TRANSACTION";
    public static final String TRANSACTION_SEQ = "TRANSACTION_SEQ";

    @EqualsAndHashCode.Include
    private BigInteger id;

    private String accountFromId;

    private String accountToId;

    private BigDecimal amount;

    private Instant created;

    public Transaction(String fromId, String toId, BigDecimal amount) {
        this.accountToId = toId;
        this.accountFromId = fromId;
        this.amount = amount;
        this.created = now();
    }

}
