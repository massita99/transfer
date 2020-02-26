package transfer.configuration.converter;

import org.jooq.Field;
import org.modelmapper.AbstractConverter;
import transfer.model.Account;

import java.util.HashMap;
import java.util.Map;

import static org.jooq.impl.DSL.field;

public class AccountConverter extends AbstractConverter<Account, Map> {

    @Override
    protected Map<Field, Object> convert(Account source) {
        Map converted = new HashMap();
        converted.put(field(Account.Fields.id), source.getId());
        converted.put(field(Account.Fields.balance), source.getBalance());
        converted.put(field(Account.Fields.created), source.getCreated());
        return converted;
    }
}
