package net.morimekta.test.calculator;

@SuppressWarnings("unused")
public class Operation
        implements net.morimekta.providence.PMessage<Operation>, java.io.Serializable, Comparable<Operation> {
    private final static long serialVersionUID = -2122462501055525645L;

    private final net.morimekta.test.calculator.Operator mOperator;
    private final java.util.List<net.morimekta.test.calculator.Operand> mOperands;
    
    private volatile int tHashCode;

    private Operation(_Builder builder) {
        mOperator = builder.mOperator;
        if (builder.isSetOperands()) {
            mOperands = builder.mOperands.build();
        } else {
            mOperands = null;
        }
    }

    public Operation(net.morimekta.test.calculator.Operator pOperator,
                     java.util.List<net.morimekta.test.calculator.Operand> pOperands) {
        mOperator = pOperator;
        if (pOperands != null) {
            mOperands = com.google.common.collect.ImmutableList.copyOf(pOperands);
        } else {
            mOperands = null;
        }
    }

    public boolean hasOperator() {
        return mOperator != null;
    }

    public net.morimekta.test.calculator.Operator getOperator() {
        return mOperator;
    }

    public int numOperands() {
        return mOperands != null ? mOperands.size() : 0;
    }

    public boolean hasOperands() {
        return mOperands != null;
    }

    public java.util.List<net.morimekta.test.calculator.Operand> getOperands() {
        return mOperands;
    }

    @Override
    public boolean has(int key) {
        switch(key) {
            case 1: return hasOperator();
            case 2: return numOperands() > 0;
            default: return false;
        }
    }

    @Override
    public int num(int key) {
        switch(key) {
            case 1: return hasOperator() ? 1 : 0;
            case 2: return numOperands();
            default: return 0;
        }
    }

    @Override
    public Object get(int key) {
        switch(key) {
            case 1: return getOperator();
            case 2: return getOperands();
            default: return null;
        }
    }

    @Override
    public boolean compact() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Operation)) return false;
        Operation other = (Operation) o;
        return java.util.Objects.equals(mOperator, other.mOperator) &&
               java.util.Objects.equals(mOperands, other.mOperands);
    }

    @Override
    public int hashCode() {
        if (tHashCode == 0) {
            tHashCode = java.util.Objects.hash(
                    Operation.class,
                    _Field.OPERATOR, mOperator,
                    _Field.OPERANDS, mOperands);
        }
        return tHashCode;
    }

    @Override
    public String toString() {
        return "calculator.Operation" + asString();
    }

    @Override
    public String asString() {
        StringBuilder out = new StringBuilder();
        out.append("{");

        boolean first = true;
        if (hasOperator()) {
            first = false;
            out.append("operator:");
            out.append(mOperator.getName());
        }
        if (numOperands() > 0) {
            if (!first) out.append(',');
            first = false;
            out.append("operands:");
            out.append(net.morimekta.util.Strings.asString(mOperands));
        }
        out.append('}');
        return out.toString();
    }

    @Override
    public int compareTo(Operation other) {
        int c;

        c = Boolean.compare(mOperator != null, other.mOperator != null);
        if (c != 0) return c;
        if (mOperator != null) {
            c = Integer.compare(mOperator.getValue(), mOperator.getValue());
            if (c != 0) return c;
        }

        c = Boolean.compare(mOperands != null, other.mOperands != null);
        if (c != 0) return c;
        if (mOperands != null) {
            c = Integer.compare(mOperands.hashCode(), other.mOperands.hashCode());
            if (c != 0) return c;
        }

        return 0;
    }

    public enum _Field implements net.morimekta.providence.descriptor.PField {
        OPERATOR(1, net.morimekta.providence.descriptor.PRequirement.DEFAULT, "operator", net.morimekta.test.calculator.Operator.provider(), null),
        OPERANDS(2, net.morimekta.providence.descriptor.PRequirement.DEFAULT, "operands", net.morimekta.providence.descriptor.PList.provider(net.morimekta.test.calculator.Operand.provider()), null),
        ;

        private final int mKey;
        private final net.morimekta.providence.descriptor.PRequirement mRequired;
        private final String mName;
        private final net.morimekta.providence.descriptor.PDescriptorProvider mTypeProvider;
        private final net.morimekta.providence.descriptor.PValueProvider<?> mDefaultValue;

        _Field(int key, net.morimekta.providence.descriptor.PRequirement required, String name, net.morimekta.providence.descriptor.PDescriptorProvider typeProvider, net.morimekta.providence.descriptor.PValueProvider<?> defaultValue) {
            mKey = key;
            mRequired = required;
            mName = name;
            mTypeProvider = typeProvider;
            mDefaultValue = defaultValue;
        }

        @Override
        public int getKey() { return mKey; }

        @Override
        public net.morimekta.providence.descriptor.PRequirement getRequirement() { return mRequired; }

        @Override
        public net.morimekta.providence.PType getType() { return getDescriptor().getType(); }

        @Override
        public net.morimekta.providence.descriptor.PDescriptor getDescriptor() { return mTypeProvider.descriptor(); }

        @Override
        public String getName() { return mName; }

        @Override
        public boolean hasDefaultValue() { return mDefaultValue != null; }

        @Override
        public Object getDefaultValue() {
            return hasDefaultValue() ? mDefaultValue.get() : null;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Operation._Field(")
                   .append(mKey)
                   .append(": ");
            if (mRequired != net.morimekta.providence.descriptor.PRequirement.DEFAULT) {
                builder.append(mRequired.label).append(" ");
            }
            builder.append(getDescriptor().getQualifiedName(null))
                   .append(' ')
                   .append(mName)
                   .append(')');
            return builder.toString();
        }

        public static _Field forKey(int key) {
            switch (key) {
                case 1: return _Field.OPERATOR;
                case 2: return _Field.OPERANDS;
                default: return null;
            }
        }

        public static _Field forName(String name) {
            switch (name) {
                case "operator": return _Field.OPERATOR;
                case "operands": return _Field.OPERANDS;
            }
            return null;
        }
    }

    public static net.morimekta.providence.descriptor.PStructDescriptorProvider<Operation,_Field> provider() {
        return new _Provider();
    }

    @Override
    public net.morimekta.providence.descriptor.PStructDescriptor<Operation,_Field> descriptor() {
        return kDescriptor;
    }

    public static final net.morimekta.providence.descriptor.PStructDescriptor<Operation,_Field> kDescriptor;

    private static class _Descriptor
            extends net.morimekta.providence.descriptor.PStructDescriptor<Operation,_Field> {
        public _Descriptor() {
            super("calculator", "Operation", new _Factory(), false, false);
        }

        @Override
        public _Field[] getFields() {
            return _Field.values();
        }

        @Override
        public _Field getField(String name) {
            return _Field.forName(name);
        }

        @Override
        public _Field getField(int key) {
            return _Field.forKey(key);
        }
    }

    static {
        kDescriptor = new _Descriptor();
    }

    private final static class _Provider extends net.morimekta.providence.descriptor.PStructDescriptorProvider<Operation,_Field> {
        @Override
        public net.morimekta.providence.descriptor.PStructDescriptor<Operation,_Field> descriptor() {
            return kDescriptor;
        }
    }

    private final static class _Factory
            extends net.morimekta.providence.PMessageBuilderFactory<Operation> {
        @Override
        public _Builder builder() {
            return new _Builder();
        }
    }

    @Override
    public _Builder mutate() {
        return new _Builder(this);
    }

    public static _Builder builder() {
        return new _Builder();
    }

    public static class _Builder
            extends net.morimekta.providence.PMessageBuilder<Operation> {
        private java.util.BitSet optionals;

        private net.morimekta.test.calculator.Operator mOperator;
        private net.morimekta.providence.descriptor.PList.Builder mOperands;


        public _Builder() {
            optionals = new java.util.BitSet(2);
            mOperands = new net.morimekta.providence.descriptor.PList.ImmutableListBuilder<>();
        }

        public _Builder(Operation base) {
            this();

            if (base.hasOperator()) {
                optionals.set(0);
                mOperator = base.mOperator;
            }
            if (base.numOperands() > 0) {
                optionals.set(1);
                mOperands.addAll(base.mOperands);
            }
        }

        public _Builder setOperator(net.morimekta.test.calculator.Operator value) {
            optionals.set(0);
            mOperator = value;
            return this;
        }
        public boolean isSetOperator() {
            return optionals.get(0);
        }
        public _Builder clearOperator() {
            optionals.set(0, false);
            mOperator = null;
            return this;
        }
        public _Builder setOperands(java.util.Collection<net.morimekta.test.calculator.Operand> value) {
            optionals.set(1);
            mOperands.clear();
            mOperands.addAll(value);
            return this;
        }
        public _Builder addToOperands(net.morimekta.test.calculator.Operand... values) {
            optionals.set(1);
            for (net.morimekta.test.calculator.Operand item : values) {
                mOperands.add(item);
            }
            return this;
        }

        public boolean isSetOperands() {
            return optionals.get(1);
        }
        public _Builder clearOperands() {
            optionals.set(1, false);
            mOperands.clear();
            return this;
        }
        @Override
        public _Builder set(int key, Object value) {
            if (value == null) return clear(key);
            switch (key) {
                case 1: setOperator((net.morimekta.test.calculator.Operator) value); break;
                case 2: setOperands((java.util.List<net.morimekta.test.calculator.Operand>) value); break;
            }
            return this;
        }

        @Override
        public _Builder addTo(int key, Object value) {
            switch (key) {
                case 2: addToOperands((net.morimekta.test.calculator.Operand) value); break;
                default: break;
            }
            return this;
        }

        @Override
        public _Builder clear(int key) {
            switch (key) {
                case 1: clearOperator(); break;
                case 2: clearOperands(); break;
            }
            return this;
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public Operation build() {
            return new Operation(this);
        }
    }
}