package org.example.Backend.BytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;
import org.example.Backend.DataToBytesConverters.ColumnType.BytesIntegerConverters;
import org.example.Backend.Exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.nio.ByteBuffer;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class BytesIntegerConvertersTest {
    private final ColumnTypeBytesConverter<Integer> bytesConverters = new BytesIntegerConverters();
    private static final byte[] NULL_BYTES = new byte[]{-1,-1,-1, -1,-1,-1, -1,-1,-1};

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void toBytes(Integer input, byte[] expectedOutput) {
        byte[] actualOutput = bytesConverters.toBytes(input);
        assertArrayEquals(expectedOutput, actualOutput, "Conversion failed for input: " + input);
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of(0, new byte[]{0}),
                Arguments.of(1, new byte[]{1}),
                Arguments.of(255, new byte[]{-1}),
                Arguments.of(256, new byte[]{1, 0}),
                Arguments.of(65535, new byte[]{-1, -1}),
                Arguments.of(65536, new byte[]{1, 0, 0}),
                Arguments.of(16777215, new byte[]{-1, -1, -1}),
                Arguments.of(16777216, new byte[]{1, 0, 0, 0}),

                Arguments.of(-1, ByteBuffer.allocate(4).putInt(-1).array()),
                Arguments.of(-255, ByteBuffer.allocate(4).putInt(-255).array()),
                Arguments.of(-256, ByteBuffer.allocate(4).putInt(-256).array()),
                Arguments.of(-65535, ByteBuffer.allocate(4).putInt(-65535).array()),
                Arguments.of(-65536, ByteBuffer.allocate(4).putInt(-65536).array()),

                Arguments.of(null, NULL_BYTES)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestDataForToData")
    public void toData(byte[] input, Integer expectedOutput) {
        Integer actualOutput = bytesConverters.toData(input);
        assertEquals(expectedOutput, actualOutput);
    }

    private static Stream<Arguments> provideTestDataForToData() {
        return Stream.of(
                Arguments.of(new byte[]{0}, 0),
                Arguments.of(new byte[]{1}, 1),
                Arguments.of(new byte[]{-1}, 255),
                Arguments.of(new byte[]{1, 0}, 256),
                Arguments.of(new byte[]{-1, -1}, 65535),
                Arguments.of(new byte[]{1, 0, 0}, 65536),
                Arguments.of(new byte[]{-1, -1, -1}, 16777215),
                Arguments.of(new byte[]{1, 0, 0, 0}, 16777216),

                Arguments.of(ByteBuffer.allocate(4).putInt(-1).array(), -1),
                Arguments.of(ByteBuffer.allocate(4).putInt(-255).array(), -255),
                Arguments.of(ByteBuffer.allocate(4).putInt(-256).array(), -256),
                Arguments.of(ByteBuffer.allocate(4).putInt(-65535).array(), -65535),
                Arguments.of(ByteBuffer.allocate(4).putInt(-65536).array(), -65536),

                Arguments.of(NULL_BYTES, null)
        );
    }

    @Test
    public void toDataWithNullOrEmptyArray() {
        assertThrows(ValidationException.class, () -> bytesConverters.toData(new byte[]{}));
        assertThrows(ValidationException.class, () -> bytesConverters.toData(null));
    }
}