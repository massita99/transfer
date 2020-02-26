package transfer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

@Data
@Schema(description="Transaction description")
public class TransactionData {

    private BigInteger id;

    private String accountFromId;

    private String accountToId;

    private BigDecimal amount;

    private Instant created;

    @Schema(type = "string", format = "UUID", description = "Transaction unique id")
    public BigInteger getId() {
        return id;
    }

    @Schema(type = "string", format = "UUID", description = "Sender Account unique id")
    public String getAccountFromId() {
        return accountFromId;
    }

    @Schema(type = "string", format = "UUID", description = "Receiver Account unique id")
    public String getAccountToId() {
        return accountToId;
    }

    @Schema(type = "number", description = "Transaction amount")
    public BigDecimal getAmount() {
        return amount;
    }

    @Schema(type = "string", format = "date-time", description = "Timestamp transaction was created")
    public Instant getCreated() {
        return created;
    }
}
