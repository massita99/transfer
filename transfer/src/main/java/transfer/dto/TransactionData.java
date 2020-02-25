package transfer.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

@Data
public class TransactionData {

    private BigInteger id;

    private String accountFromId;

    private String accountToId;

    private BigDecimal amount;

    private Instant created;
}
