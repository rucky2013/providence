package org.apache.test.number;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.thrift.j2.TMessage;
import org.apache.thrift.j2.TMessageBuilder;
import org.apache.thrift.j2.TMessageBuilderFactory;
import org.apache.thrift.j2.descriptor.TDefaultValueProvider;
import org.apache.thrift.j2.descriptor.TField;
import org.apache.thrift.j2.descriptor.TPrimitive;
import org.apache.thrift.j2.descriptor.TStructDescriptor;
import org.apache.thrift.j2.descriptor.TStructDescriptorProvider;
import org.apache.thrift.j2.util.TTypeUtils;

public class Imaginary
        implements TMessage<Imaginary>, Serializable, Parcelable {
    private final static double kDefaultV = 0.0d;
    private final static double kDefaultI = 0.0d;

    private final Double mV;
    private final Double mI;

    private Imaginary(Builder builder) {
        mV = builder.mV;
        mI = builder.mI;
    }

    public boolean hasV() {
        return mV != null;
    }

    public double getV() {
        return hasV() ? mV : kDefaultV;
    }

    public boolean hasI() {
        return mI != null;
    }

    public double getI() {
        return hasI() ? mI : kDefaultI;
    }

    @Override
    public boolean has(int key) {
        switch(key) {
            case 1: return hasV();
            case 2: return hasI();
            default: return false;
        }
    }

    @Override
    public int num(int key) {
        switch(key) {
            case 1: return hasV() ? 1 : 0;
            case 2: return hasI() ? 1 : 0;
            default: return 0;
        }
    }

    @Override
    public Object get(int key) {
        switch(key) {
            case 1: return getV();
            case 2: return getI();
            default: return null;
        }
    }

    @Override
    public boolean compact() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Imaginary)) return false;
        Imaginary other = (Imaginary) o;
        return TTypeUtils.equals(mV, other.mV) &&
               TTypeUtils.equals(mI, other.mI);
    }

    @Override
    public int hashCode() {
        return Imaginary.class.hashCode() +
               TTypeUtils.hashCode(mV) +
               TTypeUtils.hashCode(mI);
    }

    @Override
    public String toString() {
        return descriptor().getQualifiedName(null) + TTypeUtils.toString(this);
    }

    @Override
    public boolean isValid() {
        return mV != null;
    }

    @Override
    public TStructDescriptor<Imaginary> descriptor() {
        return DESCRIPTOR;
    }

    public static final TStructDescriptor<Imaginary> DESCRIPTOR = _createDescriptor();

    private final static class _Factory
            extends TMessageBuilderFactory<Imaginary> {
        @Override
        public Imaginary.Builder builder() {
            return new Imaginary.Builder();
        }
    }

    private static TStructDescriptor<Imaginary> _createDescriptor() {
        List<TField<?>> fieldList = new LinkedList<>();
        fieldList.add(new TField<>(null, 1, true, "v", TPrimitive.DOUBLE.provider(), null));
        fieldList.add(new TField<>(null, 2, false, "i", TPrimitive.DOUBLE.provider(), new TDefaultValueProvider<>(kDefaultI)));
        return new TStructDescriptor<>(null, "number", "Imaginary", fieldList, new _Factory(), false);
    }

    public static TStructDescriptorProvider<Imaginary> provider() {
        return new TStructDescriptorProvider<Imaginary>() {
            @Override
            public TStructDescriptor<Imaginary> descriptor() {
                return DESCRIPTOR;
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (hasV()) {
            dest.writeInt(1);
            dest.writeDouble(mV);
        }
        if (hasI()) {
            dest.writeInt(2);
            dest.writeDouble(mI);
        }
        dest.writeInt(0);
    }

    public static final Parcelable.Creator<Imaginary> CREATOR = new Parcelable.Creator<Imaginary>() {
        @Override
        public Imaginary createFromParcel(Parcel source) {
            Imaginary.Builder builder = new Imaginary.Builder();
            loop: while (source.dataAvail() > 0) {
                int field = source.readInt();
                switch (field) {
                    case 0: break loop;
                    case 1:
                        builder.setV(source.readDouble());
                        break;
                    case 2:
                        builder.setI(source.readDouble());
                        break;
                    default: throw new IllegalArgumentException("Unknown field ID: " + field);
                }
            }

            return builder.build();
        }

        @Override
        public Imaginary[] newArray(int size) {
            return new Imaginary[size];
        }
    };

    @Override
    public Imaginary.Builder mutate() {
        return new Imaginary.Builder(this);
    }

    public static Imaginary.Builder builder() {
        return new Imaginary.Builder();
    }

    public static class Builder
            extends TMessageBuilder<Imaginary> {
        private Double mV;
        private Double mI;

        public Builder() {
        }

        public Builder(Imaginary base) {
            this();

            mV = base.mV;
            mI = base.mI;
        }

        public Builder setV(double value) {
            mV = value;
            return this;
        }

        public Builder clearV() {
            mV = null;
            return this;
        }

        public Builder setI(double value) {
            mI = value;
            return this;
        }

        public Builder clearI() {
            mI = null;
            return this;
        }

        @Override
        public Builder set(int key, Object value) {
            switch (key) {
                case 1: setV((double) value); break;
                case 2: setI((double) value); break;
            }
            return this;
        }

        @Override
        public boolean isValid() {
            return mV != null;
        }

        @Override
        public Imaginary build() {
            return new Imaginary(this);
        }
    }
}
