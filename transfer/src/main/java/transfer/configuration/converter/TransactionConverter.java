package transfer.configuration.converter;

import org.jooq.Field;
import org.modelmapper.AbstractConverter;
import transfer.model.Transaction;
import transfer.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

import static org.jooq.impl.DSL.field;

public class TransactionConverter extends AbstractConverter<Transaction, Map> {

    @Override
    protected Map<Field, Object> convert(Transaction source) {
        Map converted = new HashMap();
        converted.put(field(Transaction.Fields.id), source.getId());
        converted.put(field(StringUtil.fromCamelCase(Transaction.Fields.accountFromId)), source.getAccountFromId());
        converted.put(field(StringUtil.fromCamelCase(Transaction.Fields.accountToId)), source.getAccountToId());
        converted.put(field(Transaction.Fields.amount), source.getAmount());
        converted.put(field(Transaction.Fields.created), source.getCreated());
        return converted;
    }

}
