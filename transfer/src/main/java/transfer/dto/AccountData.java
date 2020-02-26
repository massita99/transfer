package transfer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@ToString
@Schema(description="Account description")
public class AccountData {

    private String id;

    private BigDecimal balance;

    private Instant created;

    @Schema(type = "string", format = "UUID", description = "Account unique id")
    public String getId() {
        return id;
    }

    @Schema(type = "number", description = "Account balance")
    public BigDecimal getBalance() {
        return balance;
    }

    @Schema(type = "string", format = "date-time", description = "Timestamp account was created")
    public Instant getCreated() {
        return created;
    }
}
