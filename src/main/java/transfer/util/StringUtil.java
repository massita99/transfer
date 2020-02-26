package transfer.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {

    public static String fromCamelCase(String from) {
        return from.replaceAll("([^_A-Z])([A-Z])", "$1_$2");
    }
}
