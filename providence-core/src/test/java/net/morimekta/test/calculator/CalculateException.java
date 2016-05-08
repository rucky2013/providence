package net.morimekta.test.calculator;

@SuppressWarnings("unused")
public class CalculateException
        extends net.morimekta.providence.PException
        implements net.morimekta.providence.PMessage<CalculateException>, java.io.Serializable, Comparable<CalculateException> {
    private final static long serialVersionUID = -3144631929815376595L;

    private final String mMessage;
    private final net.morimekta.test.calculator.Operation mOperation;
    
    private volatile int tHashCode;

    private CalculateException(_Builder builder) {
        super(createMessage(builder.mMessage,
                            builder.mOperation));

        mMessage = builder.mMessage;
        mOperation = builder.mOperation;
    }

    public CalculateException(String pMessage,
                              net.morimekta.test.calculator.Operation pOperation) {
        super(createMessage(pMessage,
                            pOperation));

        mMessage = pMessage;
        mOperation = pOperation;
    }

    private static String createMessage(String pMessage,
                                        net.morimekta.test.calculator.Operation pOperation) {
        StringBuilder out = new StringBuilder();
        out.append('{');
        boolean first = true;
        if (pMessage != null) {
            first = false;
            out.append("message:")
               .append('\"')
               .append(net.morimekta.util.Strings.escape(pMessage))
               .append('\"');
        }
        if (pOperation != null) {
            if (!first) out.append(',');
            out.append("operation:")
               .append(pOperation.asString());
        }
        out.append('}');
        return out.toString();
    }

    public boolean hasMessage() {
        return mMessage != null;
    }

    /**
     * @return The field value
     */
    public String getMessage() {
        return mMessage;
    }

    public boolean hasOperation() {
        return mOperation != null;
    }

    /**
     * @return The field value
     */
    public net.morimekta.test.calculator.Operation getOperation() {
        return mOperation;
    }

    @Override
    public boolean has(int key) {
        switch(key) {
            case 1: return hasMessage();
            case 2: return hasOperation();
            default: return false;
        }
    }

    @Override
    public int num(int key) {
        switch(key) {
            case 1: return hasMessage() ? 1 : 0;
            case 2: return hasOperation() ? 1 : 0;
            default: return 0;
        }
    }

    @Override
    public Object get(int key) {
        switch(key) {
            case 1: return getMessage();
            case 2: return getOperation();
            default: return null;
        }
    }

    @Override
    public boolean compact() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof CalculateException)) return false;
        CalculateException other = (CalculateException) o;
        return java.util.Objects.equals(mMessage, other.mMessage) &&
               java.util.Objects.equals(mOperation, other.mOperation);
    }

    @Override
    public int hashCode() {
        if (tHashCode == 0) {
            tHashCode = java.util.Objects.hash(
                    CalculateException.class,
                    _Field.MESSAGE, mMessage,
                    _Field.OPERATION, mOperation);
        }
        return tHashCode;
    }

    @Override
    public String toString() {
        return "calculator.CalculateException" + asString();
    }

    @Override
    public String asString() {
        StringBuilder out = new StringBuilder();
        out.append("{");

        boolean first = true;
        if (mMessage != null) {
            first = false;
            out.append("message:")
               .append('\"')
               .append(net.morimekta.util.Strings.escape(mMessage))
               .append('\"');
        }
        if (mOperation != null) {
            if (!first) out.append(',');
            out.append("operation:")
               .append(mOperation.asString());
        }
        out.append('}');
        return out.toString();
    }

    @Override
    public int compareTo(CalculateException other) {
        int c;

        c = Boolean.compare(mMessage != null, other.mMessage != null);
        if (c != 0) return c;
        if (mMessage != null) {
            c = mMessage.compareTo(other.mMessage);
            if (c != 0) return c;
        }

        c = Boolean.compare(mOperation != null, other.mOperation != null);
        if (c != 0) return c;
        if (mOperation != null) {
            c = mOperation.compareTo(other.mOperation);
            if (c != 0) return c;
        }

        return 0;
    }

    public enum _Field implements net.morimekta.providence.descriptor.PField {
        MESSAGE(1, net.morimekta.providence.descriptor.PRequirement.REQUIRED, "message", net.morimekta.providence.descriptor.PPrimitive.STRING.provider(), null),
        OPERATION(2, net.morimekta.providence.descriptor.PRequirement.DEFAULT, "operation", net.morimekta.test.calculator.Operation.provider(), null),
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
            builder.append("CalculateException._Field(")
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
                case 1: return _Field.MESSAGE;
                case 2: return _Field.OPERATION;
                default: return null;
            }
        }

        public static _Field forName(String name) {
            switch (name) {
                case "message": return _Field.MESSAGE;
                case "operation": return _Field.OPERATION;
            }
            return null;
        }
    }

    public static net.morimekta.providence.descriptor.PExceptionDescriptorProvider<CalculateException,_Field> provider() {
        return new _Provider();
    }

    @Override
    public net.morimekta.providence.descriptor.PExceptionDescriptor<CalculateException,_Field> descriptor() {
        return kDescriptor;
    }

    public static final net.morimekta.providence.descriptor.PExceptionDescriptor<CalculateException,_Field> kDescriptor;

    private static class _Descriptor
            extends net.morimekta.providence.descriptor.PExceptionDescriptor<CalculateException,_Field> {
        public _Descriptor() {
            super("calculator", "CalculateException", new _Factory(), false);
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

    private final static class _Provider extends net.morimekta.providence.descriptor.PExceptionDescriptorProvider<CalculateException,_Field> {
        @Override
        public net.morimekta.providence.descriptor.PExceptionDescriptor<CalculateException,_Field> descriptor() {
            return kDescriptor;
        }
    }

    private final static class _Factory
            extends net.morimekta.providence.PMessageBuilderFactory<CalculateException> {
        @Override
        public _Builder builder() {
            return new _Builder();
        }
    }

    @Override
    public _Builder mutate() {
        return new _Builder(this);
    }

    /**
     * Make a calculator.CalculateException builder.
     * @return The builder instance.
     */
    public static _Builder builder() {
        return new _Builder();
    }

    public static class _Builder
            extends net.morimekta.providence.PMessageBuilder<CalculateException> {
        private java.util.BitSet optionals;

        private String mMessage;
        private net.morimekta.test.calculator.Operation mOperation;

        /**
         * Make a calculator.CalculateException builder.
         */
        public _Builder() {
            optionals = new java.util.BitSet(2);
        }

        /**
         * Make a mutating builder off a base calculator.CalculateException.
         *
         * @param base The base CalculateException
         */
        public _Builder(CalculateException base) {
            this();

            if (base.hasMessage()) {
                optionals.set(0);
                mMessage = base.mMessage;
            }
            if (base.hasOperation()) {
                optionals.set(1);
                mOperation = base.mOperation;
            }
        }

        /**
         * Sets the value of message.
         *
         * @param value The new value
         * @return The builder
         */
        public _Builder setMessage(String value) {
            optionals.set(0);
            mMessage = value;
            return this;
        }

        /**
         * Checks for presence of the message field.
         *
         * @return True iff message has been set.
         */
        public boolean isSetMessage() {
            return optionals.get(0);
        }

        /**
         * Clears the message field.
         *
         * @return The builder
         */
        public _Builder clearMessage() {
            optionals.clear(0);
            mMessage = null;
            return this;
        }

        /**
         * Sets the value of operation.
         *
         * @param value The new value
         * @return The builder
         */
        public _Builder setOperation(net.morimekta.test.calculator.Operation value) {
            optionals.set(1);
            mOperation = value;
            return this;
        }

        /**
         * Checks for presence of the operation field.
         *
         * @return True iff operation has been set.
         */
        public boolean isSetOperation() {
            return optionals.get(1);
        }

        /**
         * Clears the operation field.
         *
         * @return The builder
         */
        public _Builder clearOperation() {
            optionals.clear(1);
            mOperation = null;
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public _Builder set(int key, Object value) {
            if (value == null) return clear(key);
            switch (key) {
                case 1: setMessage((String) value); break;
                case 2: setOperation((net.morimekta.test.calculator.Operation) value); break;
            }
            return this;
        }

        @Override
        public _Builder addTo(int key, Object value) {
            switch (key) {
                default: break;
            }
            return this;
        }

        @Override
        public _Builder clear(int key) {
            switch (key) {
                case 1: clearMessage(); break;
                case 2: clearOperation(); break;
            }
            return this;
        }

        @Override
        public boolean isValid() {
            return optionals.get(0);
        }

        @Override
        public CalculateException build() {
            return new CalculateException(this);
        }
    }
}