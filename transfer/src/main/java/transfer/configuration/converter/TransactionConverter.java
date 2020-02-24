package transfer.configuration.converter;

import org.jooq.Field;
import org.modelmapper.AbstractConverter;
import transfer.model.Transaction;

import java.util.HashMap;
import java.util.Map;

import static org.jooq.impl.DSL.field;

public class TransactionConverter extends AbstractConverter<Transaction, Map> {

    @Override
    protected Map<Field, Object> convert(Transaction source) {
        Map converted = new HashMap();
        converted.put(field(Transaction.Fields.id), source.getId());
        converted.put(field(toCamelCase(Transaction.Fields.accountFromId)), source.getAccountFromId());
        converted.put(field(toCamelCase(Transaction.Fields.accountToId)), source.getAccountToId());
        converted.put(field(Transaction.Fields.amount), source.getAmount());
        converted.put(field(Transaction.Fields.created), source.getCreated());
        return converted;
    }

    private String toCamelCase(String from) {
        return from.replaceAll("([^_A-Z])([A-Z])", "$1_$2");
    }
}
