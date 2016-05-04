package net.morimekta.providence.mio;

import net.morimekta.providence.PMessage;
import net.morimekta.providence.PServiceCall;
import net.morimekta.providence.serializer.Serializer;
import net.morimekta.providence.serializer.SerializerException;
import net.morimekta.providence.streams.MessageStreams;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A writer helper class for matching a serializer with an output stream.
 */
public class IOMessageWriter implements MessageWriter {
    private final OutputStream out;
    private final Serializer   serializer;

    public IOMessageWriter(OutputStream out, Serializer serializer) {
        this.out = out;
        this.serializer = serializer;
    }

    @Override
    public <T extends PMessage<T>> int write(T message) throws IOException, SerializerException {
        int ret = serializer.serialize(out, message);
        if (!serializer.binaryProtocol()) {
            out.write(MessageStreams.READABLE_ENTRY_SEP);
        }
        return ret;
    }

    @Override
    public <T extends PMessage<T>> int write(PServiceCall<T> call) throws IOException, SerializerException {
        int ret = serializer.serialize(out, call);
        if (!serializer.binaryProtocol()) {
            out.write(MessageStreams.READABLE_ENTRY_SEP);
        }
        return ret;
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
