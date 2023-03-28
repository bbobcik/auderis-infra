package cz.auderis.infra.tools;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SeparatorTest {

    @ParameterizedTest
    @CsvSource(delimiter = '/', nullValues = "null", textBlock = """
            null  / 1, 2, 3, 4, 5
            ''    / 12345
            ','   / 1,2,3,4,5
            ' ::' / 1 ::2 ::3 ::4 ::5
            '\n'  / '1\n2\n3\n4\n5'
            """)
    void shouldJoinMultipleItems(String separatorText, String expectedResult) {
        // Given
        final var sep = new Separator(separatorText);
        final var buffer = new StringBuilder(512);
        // When
        for (int i=1; i<=5; ++i) {
            buffer.append(sep);
            buffer.append(i);
        }
        final var result = buffer.toString();
        // Then
        assertThat(result, is(expectedResult));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '/', nullValues = "null", textBlock = """
            null  / 1, 2, 3, 4, 5
            ''    / 12345
            ','   / 1,2,3,4,5
            ' ::' / 1 ::2 ::3 ::4 ::5
            '\n'  / '1\n2\n3\n4\n5'
            """)
    void shouldJoinMultipleItemsWritingToBuffer(String separatorText, String expectedResult) {
        // Given
        final var sep = new Separator(separatorText);
        final var buffer = new StringBuilder(512);
        // When
        for (int i=1; i<=5; ++i) {
            sep.to(buffer);
            buffer.append(i);
        }
        final var result = buffer.toString();
        // Then
        assertThat(result, is(expectedResult));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '/', nullValues = "null", textBlock = """
            x  / 1x2x3x4x5
            +  / 1+2+3+4+5
            """)
    void shouldJoinMultipleItemsWithSingleCharacter(char separatorChar, String expectedResult) {
        // Given
        final var sep = new Separator(separatorChar);
        final var buffer = new StringBuilder(512);
        // When
        for (int i=1; i<=5; ++i) {
            buffer.append(sep);
            buffer.append(i);
        }
        final var result = buffer.toString();
        // Then
        assertThat(result, is(expectedResult));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '/', nullValues = "null", textBlock = """
            null  / 1, 2, 34, 5
            ''    / 12345
            ','   / 1,2,34,5
            ' ::' / 1 ::2 ::34 ::5
            '\n'  / '1\n2\n34\n5'
            """)
    void shouldPerformCorrectSeparatorReset(String separatorText, String expectedResult) {
        // Given
        final var sep = new Separator(separatorText);
        final var buffer = new StringBuilder(512);
        // When
        int i;
        for (i=1; i<=3; ++i) {
            buffer.append(sep);
            buffer.append(i);
        }
        sep.reset();
        for (; i<=5; ++i) {
            buffer.append(sep);
            buffer.append(i);
        }
        final var result = buffer.toString();
        // Then
        assertThat(result, is(expectedResult));
    }

}
