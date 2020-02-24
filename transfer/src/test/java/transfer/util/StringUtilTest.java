package transfer.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static transfer.util.StringUtil.fromCamelCase;


class StringUtilTest {


    @ParameterizedTest
    @CsvSource({
            "test,test",
            "testTest,test_Test",
            "accountId,account_Id"
    })
    void testFromCamelCase(String input, String expectedResult) {
        //When
        var actual = fromCamelCase(input);
        assertThat(actual).isEqualTo(expectedResult);
    }

}