package net.morimekta.providence.tools.data;

import net.morimekta.test.providence.CompactFields;
import net.morimekta.test.providence.Containers;
import net.morimekta.test.providence.DefaultFields;
import net.morimekta.test.providence.DefaultValues;
import net.morimekta.test.providence.ExceptionFields;
import net.morimekta.test.providence.OptionalFields;
import net.morimekta.test.providence.RequiredFields;
import net.morimekta.test.providence.UnionFields;
import net.morimekta.test.providence.Value;

/**
 * @author Stein Eldar Johnsen
 * @since 21.01.16.
 */
public class MessageGenerator {
    public static final int    KEY       = 5;
    public static final int    DATA      = 12;
    public static final double FILL      = 1.0;
    public static final int    MIN_ITEMS = 2;
    public static final int    MAX_ITEMS = 5;

    private final RandomGenerator randomGenerator;

    public MessageGenerator(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    public Value nextValue() {
        return Value.values()[randomGenerator.nextInt(Value.values().length)];
    }

    public RequiredFields nextRequiredFields() {
        return new RequiredFields(randomGenerator.nextBoolean(),
                                  randomGenerator.nextByte(),
                                  randomGenerator.nextShort(),
                                  randomGenerator.nextInt(),
                                  randomGenerator.nextLong(),
                                  randomGenerator.nextDistributedDouble(),
                                  randomGenerator.nextString(DATA),
                                  randomGenerator.nextBinary(DATA),
                                  nextValue(),
                                  nextCompactFields());
    }

    private boolean doFill() {
        return randomGenerator.byChance(FILL);
    }

    private int randomItemCount() {
        return randomGenerator.nextInt(MIN_ITEMS, MAX_ITEMS);
    }

    public DefaultFields nextDefaultFields() {
        DefaultFields._Builder builder = DefaultFields.builder();
        if (doFill()) {
            builder.setBooleanValue(randomGenerator.nextBoolean());
        }
        if (doFill()) {
            builder.setByteValue(randomGenerator.nextByte());
        }
        if (doFill()) {
            builder.setShortValue(randomGenerator.nextShort());
        }
        if (doFill()) {
            builder.setIntegerValue(randomGenerator.nextInt());
        }
        if (doFill()) {
            builder.setLongValue(randomGenerator.nextLong());
        }
        if (doFill()) {
            builder.setDoubleValue(randomGenerator.nextDistributedDouble());
        }
        if (doFill()) {
            builder.setBinaryValue(randomGenerator.nextBinary(DATA));
        }
        if (doFill()) {
            builder.setStringValue(randomGenerator.nextString(DATA));
        }
        if (doFill()) {
            builder.setEnumValue(nextValue());
        }
        if (doFill()) {
            builder.setCompactValue(nextCompactFields());
        }

        return builder.build();
    }

    private OptionalFields nextOptionalFields() {
        OptionalFields._Builder builder = OptionalFields.builder();
        if (doFill()) {
            builder.setBooleanValue(randomGenerator.nextBoolean());
        }
        if (doFill()) {
            builder.setByteValue(randomGenerator.nextByte());
        }
        if (doFill()) {
            builder.setShortValue(randomGenerator.nextShort());
        }
        if (doFill()) {
            builder.setIntegerValue(randomGenerator.nextInt());
        }
        if (doFill()) {
            builder.setLongValue(randomGenerator.nextLong());
        }
        if (doFill()) {
            builder.setDoubleValue(randomGenerator.nextDistributedDouble());
        }
        if (doFill()) {
            builder.setBinaryValue(randomGenerator.nextBinary(DATA));
        }
        if (doFill()) {
            builder.setStringValue(randomGenerator.nextString(DATA));
        }
        if (doFill()) {
            builder.setEnumValue(nextValue());
        }
        if (doFill()) {
            builder.setCompactValue(nextCompactFields());
        }

        return builder.build();
    }

    private ExceptionFields nextExceptionFields() {
        ExceptionFields._Builder builder = ExceptionFields.builder();
        if (doFill()) {
            builder.setBooleanValue(randomGenerator.nextBoolean());
        }
        if (doFill()) {
            builder.setByteValue(randomGenerator.nextByte());
        }
        if (doFill()) {
            builder.setShortValue(randomGenerator.nextShort());
        }
        if (doFill()) {
            builder.setIntegerValue(randomGenerator.nextInt());
        }
        if (doFill()) {
            builder.setLongValue(randomGenerator.nextLong());
        }
        if (doFill()) {
            builder.setDoubleValue(randomGenerator.nextDistributedDouble());
        }
        if (doFill()) {
            builder.setBinaryValue(randomGenerator.nextBinary(DATA));
        }
        if (doFill()) {
            builder.setStringValue(randomGenerator.nextString(DATA));
        }
        if (doFill()) {
            builder.setEnumValue(nextValue());
        }
        if (doFill()) {
            builder.setCompactValue(nextCompactFields());
        }

        return builder.build();
    }

    private DefaultValues nextDefaultValues() {
        DefaultValues._Builder builder = DefaultValues.builder();
        if (doFill()) {
            builder.setBooleanValue(randomGenerator.nextBoolean());
        }
        if (doFill()) {
            builder.setByteValue(randomGenerator.nextByte());
        }
        if (doFill()) {
            builder.setShortValue(randomGenerator.nextShort());
        }
        if (doFill()) {
            builder.setIntegerValue(randomGenerator.nextInt());
        }
        if (doFill()) {
            builder.setLongValue(randomGenerator.nextLong());
        }
        if (doFill()) {
            builder.setDoubleValue(randomGenerator.nextDistributedDouble());
        }
        if (doFill()) {
            builder.setBinaryValue(randomGenerator.nextBinary(DATA));
        }
        if (doFill()) {
            builder.setStringValue(randomGenerator.nextString(DATA));
        }
        if (doFill()) {
            builder.setEnumValue(nextValue());
        }
        if (doFill()) {
            builder.setCompactValue(nextCompactFields());
        }

        return builder.build();
    }

    public UnionFields nextUnionFields() {
        UnionFields._Builder builder = UnionFields.builder();
        int pos = randomGenerator.nextInt(UnionFields._Field.values().length);
        UnionFields._Field field = UnionFields._Field.values()[pos];
        switch (field) {
            case BOOLEAN_VALUE:
                builder.setBooleanValue(randomGenerator.nextBoolean());
                break;
            case BYTE_VALUE:
                builder.setByteValue(randomGenerator.nextByte());
                break;
            case SHORT_VALUE:
                builder.setShortValue(randomGenerator.nextShort());
                break;
            case INTEGER_VALUE:
                builder.setIntegerValue(randomGenerator.nextInt());
                break;
            case LONG_VALUE:
                builder.setLongValue(randomGenerator.nextLong());
                break;
            case DOUBLE_VALUE:
                builder.setDoubleValue(randomGenerator.nextDistributedDouble());
                break;
            case STRING_VALUE:
                builder.setStringValue(randomGenerator.nextString(DATA));
                break;
            case BINARY_VALUE:
                builder.setBinaryValue(randomGenerator.nextBinary(DATA));
                break;
            case ENUM_VALUE:
                builder.setEnumValue(nextValue());
                break;
            case COMPACT_VALUE:
                builder.setCompactValue(nextCompactFields());
                break;
        }

        return builder.build();
    }

    private CompactFields nextCompactFields() {
        return new CompactFields(randomGenerator.nextString(KEY),
                                 randomGenerator.nextInt(1000),
                                 randomGenerator.byChance(0.33) ? randomGenerator.nextString(KEY) : null);
    }

    public Containers nextContainers() {
        Containers._Builder containers = Containers.builder();

        // --- LISTS ---

        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToBooleanList(randomGenerator.nextBoolean());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToByteList(randomGenerator.nextByte());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToShortList(randomGenerator.nextShort());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToIntegerList(randomGenerator.nextInt());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToLongList(randomGenerator.nextLong());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToDoubleList(randomGenerator.nextDistributedDouble());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToBinaryList(randomGenerator.nextBinary(DATA));
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToStringList(randomGenerator.nextString(DATA));
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToEnumList(nextValue());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToMessageList(nextDefaultFields());
            }
        }

        // --- SETS ---

        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToBooleanSet(randomGenerator.nextBoolean());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToByteSet(randomGenerator.nextByte());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToShortSet(randomGenerator.nextShort());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToIntegerSet(randomGenerator.nextInt());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToLongSet(randomGenerator.nextLong());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToDoubleSet(randomGenerator.nextDistributedDouble());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToBinarySet(randomGenerator.nextBinary(DATA));
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToStringSet(randomGenerator.nextString(DATA));
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToEnumSet(nextValue());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.addToMessageSet(nextDefaultFields());
            }
        }

        // --- MAPS ---

        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.putInBooleanMap(randomGenerator.nextBoolean(), randomGenerator.nextBoolean());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.putInByteMap(randomGenerator.nextByte(), randomGenerator.nextByte());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.putInShortMap(randomGenerator.nextShort(), randomGenerator.nextShort());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.putInIntegerMap(randomGenerator.nextInt(), randomGenerator.nextInt());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.putInLongMap(randomGenerator.nextLong(), randomGenerator.nextLong());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.putInDoubleMap(randomGenerator.nextDistributedDouble(),
                                          randomGenerator.nextDistributedDouble());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.putInBinaryMap(randomGenerator.nextBinary(KEY), randomGenerator.nextBinary(DATA));
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.putInStringMap(randomGenerator.nextString(KEY), randomGenerator.nextString(DATA));
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.putInEnumMap(nextValue(), nextValue());
            }
        }
        if (doFill()) {
            final int items = randomItemCount();
            for (int i = 0; i < items; ++i) {
                containers.putInMessageMap(randomGenerator.nextString(KEY), nextDefaultFields());
            }
        }

        // Direct fields.
        if (doFill()) {
            containers.setRequiredFields(nextRequiredFields());
        }
        if (doFill()) {
            containers.setDefaultFields(nextDefaultFields());
        }
        if (doFill()) {
            containers.setOptionalFields(nextOptionalFields());
        }
        if (doFill()) {
            containers.setUnionFields(nextUnionFields());
        }
        if (doFill()) {
            containers.setExceptionFields(nextExceptionFields());
        }
        if (doFill()) {
            containers.setDefaultValues(nextDefaultValues());
        }

        return containers.build();
    }
}
