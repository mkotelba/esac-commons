package com.esacinc.commons.crypto.utils;

import com.esacinc.commons.utils.EsacStringUtils;
import com.esacinc.commons.crypto.PemType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public final class EsacPemUtils {
    private static class EsacPemWriter extends PemWriter {
        private ByteArrayOutputStream outStream;

        public EsacPemWriter(ByteArrayOutputStream outStream) {
            super(new OutputStreamWriter(outStream));

            this.outStream = outStream;
        }

        public byte[] writeObject(PemObject obj) throws IOException {
            try {
                this.writeObject(() -> obj);

                this.flush();
            } finally {
                this.close();
            }

            return ArrayUtils.toPrimitive(
                Stream.of(ArrayUtils.toObject(this.outStream.toByteArray())).filter(dataItem -> (dataItem != EsacStringUtils.CR_CHAR)).toArray(Byte[]::new));
        }

        @Override
        public void newLine() throws IOException {
            this.write(EsacStringUtils.LF_CHAR);
        }
    }

    private EsacPemUtils() {
    }

    public static byte[] encode(PemType type, byte ... data) throws IOException {
        return encode(new PemObject(type.getName(), data));
    }

    public static byte[] encode(PemObject obj) throws IOException {
        return new EsacPemWriter(new ByteArrayOutputStream()).writeObject(obj);
    }
}
