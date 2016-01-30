/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package net.morimekta.providence.serializer;

import net.morimekta.providence.PEnumBuilder;
import net.morimekta.providence.PEnumValue;
import net.morimekta.providence.PMessage;
import net.morimekta.providence.PMessageBuilder;
import net.morimekta.providence.PType;
import net.morimekta.providence.PUnion;
import net.morimekta.providence.descriptor.PContainer;
import net.morimekta.providence.descriptor.PDescriptor;
import net.morimekta.providence.descriptor.PEnumDescriptor;
import net.morimekta.providence.descriptor.PField;
import net.morimekta.providence.descriptor.PMap;
import net.morimekta.providence.descriptor.PStructDescriptor;
import net.morimekta.util.Binary;
import net.morimekta.util.io.BinaryReader;
import net.morimekta.util.io.BinaryWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;

/**
 * Compact binary serializer. This uses the most compact binary format
 * allowable.
 * <p/>
 * See data definition file <code>docs/fast-binary.md</code> for format spec.
 */
public class PFastBinarySerializer extends PSerializer {
    protected final boolean readStrict;

    /**
     * Construct a serializer instance.
     */
    public PFastBinarySerializer() {
        this(false);
    }

    /**
     * Construct a serializer instance.
     */
    public PFastBinarySerializer(boolean readStrict) {
        this.readStrict = readStrict;
    }

    @Override
    public int serialize(OutputStream os, PMessage<?> message) throws IOException, PSerializeException {
        BinaryWriter out = new BinaryWriter(os);
        return writeMessage(out, message);
    }

    @Override
    public <T> int serialize(OutputStream os, PDescriptor<T> descriptor, T value)
            throws IOException, PSerializeException {
        try {
            BinaryWriter out = new BinaryWriter(os);
            if (PType.MESSAGE == descriptor.getType()) {
                return writeMessage(out, (PMessage<?>) value);
            } else {
                return writeFieldValue(out, 0, descriptor, value);
            }
        } catch (IOException e) {
            throw new PSerializeException(e, "Unable to write to stream");
        }
    }

    @Override
    public <T> T deserialize(InputStream is, PDescriptor<T> descriptor) throws PSerializeException, IOException {
        BinaryReader in = new BinaryReader(is);
        if (PType.MESSAGE == descriptor.getType()) {
            return cast((Object) readMessage(in, (PStructDescriptor<?, ?>) descriptor));
        } else {
            // Assume it consists of a single field.
            int tag = in.readIntVarint();
            if (tag > 0) {
                return readFieldValue(in, tag & 0x0f, descriptor);
            } else if (readStrict) {
                throw new PSerializeException("");
            }
            return null;
        }
    }

    // --- MESSAGE ---

    /**
     * @param out     Writer to write to.
     * @param message Message to write.
     * @return Number of bytes written.
     *
     * @throws PSerializeException When serialization failed.
     * @throws IOException         When unable to write to stream as expected.
     */
    protected int writeMessage(BinaryWriter out, PMessage<?> message) throws IOException, PSerializeException {
        int len = 0;
        if (message instanceof PUnion) {
            PField field = ((PUnion) message).unionField();
            if (field != null) {
                len += writeFieldValue(out, field.getKey(), field.getDescriptor(), message.get(field.getKey()));
            }
        } else {
            for (PField<?> field : message.descriptor()
                                          .getFields()) {
                if (message.has(field.getKey())) {
                    len += writeFieldValue(out, field.getKey(), field.getDescriptor(), message.get(field.getKey()));
                }
            }
        }
        // write STOP field.
        return len + out.writeVarint(0);
    }

    /**
     * @param in         The reader to read from.
     * @param descriptor Descriptor of the message to read.
     * @return Message read.
     *
     * @throws PSerializeException When deserialization failed.
     * @throws IOException         When unable to read from stream as expected.
     */
    private <T extends PMessage<T>> T readMessage(BinaryReader in, PStructDescriptor<T, ?> descriptor)
            throws PSerializeException, IOException {
        PMessageBuilder<T> builder = descriptor.factory()
                                               .builder();
        int tag;
        while ((tag = in.readIntVarint()) > 0) {
            int id = tag >>> 3;
            int type = tag & 0x07;
            PField<?> field = descriptor.getField(id);
            if (field != null) {
                Object value = readFieldValue(in, type, field.getDescriptor());
                builder.set(field.getKey(), value);
            } else {
                if (readStrict) {
                    throw new PSerializeException(
                            "Unknown field " + id + " for type" + descriptor.getQualifiedName(null));
                }
                readFieldValue(in, tag, null);
            }
        }
        return builder.build();
    }

    // --- FIELD VALUE ---

    @SuppressWarnings("unchecked")
    private int writeFieldValue(BinaryWriter out, int key, PDescriptor<?> descriptor, Object value)
            throws IOException, PSerializeException {
        switch (descriptor.getType()) {
            case BOOL: {
                return out.writeVarint(key << 3 | ((Boolean) value ? TRUE : NONE));
            }
            case BYTE: {
                int len = out.writeVarint(key << 3 | VARINT);
                return len + out.writeZigzag((byte) value);
            }
            case I16: {
                int len = out.writeVarint(key << 3 | VARINT);
                return len + out.writeZigzag((short) value);
            }
            case I32: {
                int len = out.writeVarint(key << 3 | VARINT);
                return len + out.writeZigzag((int) value);
            }
            case I64: {
                int len = out.writeVarint(key << 3 | VARINT);
                return len + out.writeZigzag((long) value);
            }
            case DOUBLE: {
                int len = out.writeVarint(key << 3 | FIXED_64);
                return len + out.writeDouble((Double) value);
            }
            case STRING: {
                byte[] bytes = ((String) value).getBytes(StandardCharsets.UTF_8);
                int len = out.writeVarint(key << 3 | BINARY);
                len += out.writeVarint(bytes.length);
                out.write(bytes);
                return len + bytes.length;
            }
            case BINARY: {
                Binary bytes = (Binary) value;
                int len = out.writeVarint(key << 3 | BINARY);
                len += out.writeVarint(bytes.length());
                bytes.write(out);
                return len + bytes.length();
            }
            case ENUM: {
                int len = out.writeVarint(key << 3 | VARINT);
                return len + out.writeZigzag(((PEnumValue) value).getValue());
            }
            case MESSAGE: {
                int len = out.writeVarint(key << 3 | MESSAGE);
                return len + writeMessage(out, (PMessage<?>) value);
            }
            case MAP: {
                int len = out.writeVarint(key << 3 | COLLECTION);

                Map<Object, Object> map = (Map<Object, Object>) value;
                PMap<?, ?> desc = (PMap<?, ?>) descriptor;

                len += out.writeVarint(map.size() * 2);
                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                    len += writeFieldValue(out, 1, desc.keyDescriptor(), entry.getKey());
                    len += writeFieldValue(out, 2, desc.itemDescriptor(), entry.getValue());
                }
                return len;
            }
            case SET:
            case LIST: {
                int len = out.writeVarint(key << 3 | COLLECTION);

                Collection<Object> coll = (Collection<Object>) value;
                PContainer<?, ?> desc = (PContainer<?, ?>) descriptor;

                len += out.writeVarint(coll.size());
                for (Object item : coll) {
                    len += writeFieldValue(out, 0, desc.itemDescriptor(), item);
                }
                return len;
            }
            default:
                throw new PSerializeException("");
        }
    }

    /**
     * Read a field value from stream.
     *
     * @param in         The stream to consume.
     * @param type       The encoded type.
     * @param descriptor The type descriptor to generate content for.
     * @return The field value, or null if no type.
     *
     * @throws IOException If unable to read from stream or invalid field type.
     */
    @SuppressWarnings("unchecked")
    protected <T> T readFieldValue(BinaryReader in, int type, PDescriptor<T> descriptor)
            throws IOException, PSerializeException {
        switch (type) {
            case VARINT: {
                if (descriptor == null) {
                    if (readStrict) {
                        throw new PSerializeException("");
                    }
                    in.readLongVarint();
                    return null;
                }
                switch (descriptor.getType()) {
                    case BYTE:
                        return cast((byte) in.readIntZigzag());
                    case I16:
                        return cast((short) in.readIntZigzag());
                    case I32:
                        return cast((int) in.readIntZigzag());
                    case I64:
                        return cast(in.readLongZigzag());
                    case ENUM: {
                        PEnumBuilder<?> builder = ((PEnumDescriptor<?>) descriptor).factory()
                                                                                   .builder();
                        builder.setByValue(in.readIntZigzag());
                        return cast(builder.build());
                    }
                    default: {
                        throw new PSerializeException("");
                    }
                }
            }
            case FIXED_64:
                return cast(in.readDouble());
            case BINARY: {
                int len = in.readIntVarint();
                byte[] data = in.readBytes(len);
                if (descriptor != null) {
                    switch (descriptor.getType()) {
                        case STRING:
                            return cast(new String(data, StandardCharsets.UTF_8));
                        case BINARY:
                            return cast(Binary.wrap(data));
                        default:
                            throw new PSerializeException("");
                    }
                } else {
                    if (readStrict) {
                        throw new PSerializeException("");
                    }
                    return null;
                }
            }
            case MESSAGE:
                return cast((T) readMessage(in, (PStructDescriptor<?, ?>) descriptor));
            case COLLECTION:
                if (descriptor == null) {
                    if (readStrict) {
                        throw new PSerializeException("");
                    }
                    final int len = in.readIntVarint();
                    for (int i = 0; i < len; ++i) {
                        readFieldValue(in, in.readIntVarint() & 0x07, null);
                    }
                    return null;
                } else if (descriptor.getType() == PType.MAP) {
                    PMap<?, ?> ct = (PMap) descriptor;
                    PDescriptor<?> kt = ct.keyDescriptor();
                    PDescriptor<?> vt = ct.itemDescriptor();

                    Map<Object, Object> out = new LinkedHashMap<>();
                    final int len = in.readIntVarint();
                    for (int i = 0; i < len; ++i, ++i) {
                        Object key = readFieldValue(in, in.readIntVarint() & 0x07, kt);
                        Object value = readFieldValue(in, in.readIntVarint() & 0x07, vt);
                        out.put(key, value);
                    }
                    return cast(out);
                } else {
                    PContainer<?, ?> ct = (PContainer) descriptor;
                    PDescriptor<?> it = ct.itemDescriptor();
                    // TODO: Make sure I can always use LinkedList to reduce overhead.
                    Collection<Object> out =
                            descriptor.getType() == PType.SET ? new LinkedHashSet<>() : new LinkedList<>();
                    final int len = in.readIntVarint();
                    for (int i = 0; i < len; ++i) {
                        int tag = in.readIntVarint();
                        out.add(readFieldValue(in, tag & 0x07, it));
                    }
                    return cast(out);
                }
            case NONE:
                return cast(false);
            case TRUE:
                return cast(true);
        }

        return null;
    }

    protected static final int VARINT     = 0x00;  // -> zigzag encoded base-128 number (byte, i16, i32, i64).
    protected static final int FIXED_64   = 0x01;  // -> double
    protected static final int BINARY     = 0x02;  // -> varint len + binary data.
    protected static final int MESSAGE    = 0x03;  // -> messages, terminated with field-ID 0.
    protected static final int COLLECTION = 0x04;  // -> varint len + N * (tag + field).
    // ----------------------  FIXED_32   = 0x05;
    protected static final int NONE       = 0x06;  // 0, false, empty.
    protected static final int TRUE       = 0x07;  // 1, true.
}
