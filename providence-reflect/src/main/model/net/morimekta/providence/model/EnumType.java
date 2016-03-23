package net.morimekta.providence.model;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import net.morimekta.providence.PMessage;
import net.morimekta.providence.PMessageBuilder;
import net.morimekta.providence.PMessageBuilderFactory;
import net.morimekta.providence.PType;
import net.morimekta.providence.descriptor.PDescriptor;
import net.morimekta.providence.descriptor.PDescriptorProvider;
import net.morimekta.providence.descriptor.PField;
import net.morimekta.providence.descriptor.PList;
import net.morimekta.providence.descriptor.PPrimitive;
import net.morimekta.providence.descriptor.PRequirement;
import net.morimekta.providence.descriptor.PStructDescriptor;
import net.morimekta.providence.descriptor.PStructDescriptorProvider;
import net.morimekta.providence.descriptor.PValueProvider;
import net.morimekta.providence.util.PTypeUtils;

/**
 * enum {
 *   (<value> ([;,])?)*
 * }
 */
@SuppressWarnings("unused")
public class EnumType
        implements PMessage<EnumType>, Serializable, Comparable<EnumType> {
    private final static long serialVersionUID = 5720337451968926862L;

    private final String mComment;
    private final String mName;
    private final List<EnumValue> mValues;
    
    private volatile int tHashCode;

    private EnumType(_Builder builder) {
        mComment = builder.mComment;
        mName = builder.mName;
        mValues = Collections.unmodifiableList(new LinkedList<>(builder.mValues));
    }

    public EnumType(String pComment,
                    String pName,
                    List<EnumValue> pValues) {
        mComment = pComment;
        mName = pName;
        mValues = Collections.unmodifiableList(new LinkedList<>(pValues));
    }

    public boolean hasComment() {
        return mComment != null;
    }

    public String getComment() {
        return mComment;
    }

    public boolean hasName() {
        return mName != null;
    }

    public String getName() {
        return mName;
    }

    public int numValues() {
        return mValues != null ? mValues.size() : 0;
    }

    public List<EnumValue> getValues() {
        return mValues;
    }

    @Override
    public boolean has(int key) {
        switch(key) {
            case 1: return hasComment();
            case 2: return hasName();
            case 3: return numValues() > 0;
            default: return false;
        }
    }

    @Override
    public int num(int key) {
        switch(key) {
            case 1: return hasComment() ? 1 : 0;
            case 2: return hasName() ? 1 : 0;
            case 3: return numValues();
            default: return 0;
        }
    }

    @Override
    public Object get(int key) {
        switch(key) {
            case 1: return getComment();
            case 2: return getName();
            case 3: return getValues();
            default: return null;
        }
    }

    @Override
    public boolean isCompact() {
        return false;
    }

    @Override
    public boolean isSimple() {
        return descriptor().isSimple();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof EnumType)) return false;
        EnumType other = (EnumType) o;
        return Objects.equals(mComment, other.mComment) &&
               Objects.equals(mName, other.mName) &&
               PTypeUtils.equals(mValues, other.mValues);
    }

    @Override
    public int hashCode() {
        if (tHashCode == 0) {
            tHashCode = Objects.hash(
                    EnumType.class,
                    _Field.COMMENT, mComment,
                    _Field.NAME, mName,
                    _Field.VALUES, PTypeUtils.hashCode(mValues));
        }
        return tHashCode;
    }

    @Override
    public String toString() {
        return "model.EnumType" + asString();
    }

    @Override
    public String asString() {
        StringBuilder out = new StringBuilder();
        out.append("{");

        boolean first = true;
        if (hasComment()) {
            first = false;
            out.append("comment:");
            out.append('\"').append(mComment).append('\"');
        }
        if (hasName()) {
            if (!first) out.append(',');
            first = false;
            out.append("name:");
            out.append('\"').append(mName).append('\"');
        }
        if (numValues() > 0) {
            if (!first) out.append(',');
            first = false;
            out.append("values:");
            out.append(PTypeUtils.toString(mValues));
        }
        out.append('}');
        return out.toString();
    }

    @Override
    public int compareTo(EnumType other) {
        int c;

        c = Boolean.compare(mComment != null, other.mComment != null);
        if (c != 0) return c;
        if (mComment != null) {
            c = mComment.compareTo(other.mComment);
            if (c != 0) return c;
        }

        c = Boolean.compare(mName != null, other.mName != null);
        if (c != 0) return c;
        if (mName != null) {
            c = mName.compareTo(other.mName);
            if (c != 0) return c;
        }

        c = Boolean.compare(mValues != null, other.mValues != null);
        if (c != 0) return c;
        if (mValues != null) {
            c = Integer.compare(mValues.hashCode(), other.mValues.hashCode());
            if (c != 0) return c;
        }

        return 0;
    }

    public enum _Field implements PField {
        COMMENT(1, PRequirement.DEFAULT, "comment", PPrimitive.STRING.provider(), null),
        NAME(2, PRequirement.REQUIRED, "name", PPrimitive.STRING.provider(), null),
        VALUES(3, PRequirement.DEFAULT, "values", PList.provider(EnumValue.provider()), null),
        ;

        private final int mKey;
        private final PRequirement mRequired;
        private final String mName;
        private final PDescriptorProvider<?> mTypeProvider;
        private final PValueProvider<?> mDefaultValue;

        _Field(int key, PRequirement required, String name, PDescriptorProvider<?> typeProvider, PValueProvider<?> defaultValue) {
            mKey = key;
            mRequired = required;
            mName = name;
            mTypeProvider = typeProvider;
            mDefaultValue = defaultValue;
        }

        @Override
        public String getComment() { return null; }

        @Override
        public int getKey() { return mKey; }

        @Override
        public PRequirement getRequirement() { return mRequired; }

        @Override
        public PType getType() { return getDescriptor().getType(); }

        @Override
        public PDescriptor<?> getDescriptor() { return mTypeProvider.descriptor(); }

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
            builder.append("EnumType._Field(")
                   .append(mKey)
                   .append(": ");
            if (mRequired != PRequirement.DEFAULT) {
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
                case 1: return _Field.COMMENT;
                case 2: return _Field.NAME;
                case 3: return _Field.VALUES;
                default: return null;
            }
        }

        public static _Field forName(String name) {
            switch (name) {
                case "comment": return _Field.COMMENT;
                case "name": return _Field.NAME;
                case "values": return _Field.VALUES;
            }
            return null;
        }
    }

    public static PStructDescriptorProvider<EnumType,_Field> provider() {
        return new _Provider();
    }

    @Override
    public PStructDescriptor<EnumType,_Field> descriptor() {
        return kDescriptor;
    }

    public static final PStructDescriptor<EnumType,_Field> kDescriptor;

    private static class _Descriptor
            extends PStructDescriptor<EnumType,_Field> {
        public _Descriptor() {
            super(null, "model", "EnumType", new _Factory(), false, false);
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

    private final static class _Provider extends PStructDescriptorProvider<EnumType,_Field> {
        @Override
        public PStructDescriptor<EnumType,_Field> descriptor() {
            return kDescriptor;
        }
    }

    private final static class _Factory
            extends PMessageBuilderFactory<EnumType> {
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
            extends PMessageBuilder<EnumType> {
        private BitSet optionals;

        private String mComment;
        private String mName;
        private List<EnumValue> mValues;


        public _Builder() {
            optionals = new BitSet(3);
            mValues = new LinkedList<>();
        }

        public _Builder(EnumType base) {
            this();

            if (base.hasComment()) {
                optionals.set(0);
                mComment = base.mComment;
            }
            if (base.hasName()) {
                optionals.set(1);
                mName = base.mName;
            }
            if (base.numValues() > 0) {
                optionals.set(2);
                mValues.addAll(base.mValues);
            }
        }

        public _Builder setComment(String value) {
            optionals.set(0);
            mComment = value;
            return this;
        }
        public _Builder clearComment() {
            optionals.set(0, false);
            mComment = null;
            return this;
        }
        public _Builder setName(String value) {
            optionals.set(1);
            mName = value;
            return this;
        }
        public _Builder clearName() {
            optionals.set(1, false);
            mName = null;
            return this;
        }
        public _Builder setValues(Collection<EnumValue> value) {
            optionals.set(2);
            mValues.clear();
            mValues.addAll(value);
            return this;
        }
        public _Builder addToValues(EnumValue... values) {
            optionals.set(2);
            for (EnumValue item : values) {
                mValues.add(item);
            }
            return this;
        }

        public _Builder clearValues() {
            optionals.set(2, false);
            mValues.clear();
            return this;
        }
        @Override
        public _Builder set(int key, Object value) {
            if (value == null) return clear(key);
            switch (key) {
                case 1: setComment((String) value); break;
                case 2: setName((String) value); break;
                case 3: setValues((List<EnumValue>) value); break;
            }
            return this;
        }

        @Override
        public _Builder addTo(int key, Object value) {
            switch (key) {
                case 3: addToValues((EnumValue) value); break;
                default: break;
            }
            return this;
        }

        @Override
        public _Builder clear(int key) {
            switch (key) {
                case 1: clearComment(); break;
                case 2: clearName(); break;
                case 3: clearValues(); break;
            }
            return this;
        }

        @Override
        public boolean isValid() {
            return optionals.get(1);
        }

        @Override
        public EnumType build() {
            return new EnumType(this);
        }
    }
}