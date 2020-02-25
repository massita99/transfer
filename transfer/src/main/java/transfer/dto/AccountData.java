package transfer.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class AccountData {

    private String id;

    private BigDecimal balance;

    private Instant created;
}
