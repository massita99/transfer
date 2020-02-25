package transfer.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferData {

    private String accountFromId;

    private String accountToId;

    private BigDecimal amount;
}
