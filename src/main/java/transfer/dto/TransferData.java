package transfer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description="Money Transfer description")
public class TransferData {

    private String accountFromId;

    private String accountToId;

    private BigDecimal amount;

    @Schema(type = "string", format = "UUID", description = "Sender Account unique id")
    public String getAccountFromId() {
        return accountFromId;
    }

    @Schema(type = "string", format = "UUID", description = "Receiver Account unique id")
    public String getAccountToId() {
        return accountToId;
    }

    @Schema(type = "number", description = "Transfer amount")
    public BigDecimal getAmount() {
        return amount;
    }
}
