package com.esacinc.commons.crypto.cert.json.impl;

import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldNames;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class CertificateJsonSerializer extends StdSerializer<X509Certificate> {
    private final static long serialVersionUID = 0L;

    public CertificateJsonSerializer() {
        super(X509Certificate.class);
    }

    @Override
    public void serialize(X509Certificate cert, JsonGenerator jsonGen, SerializerProvider serializerProv) throws IOException {
        jsonGen.writeStartObject();

        int certVersion = cert.getVersion();
        String certIssuerDnStr = cert.getIssuerX500Principal().toString(), certSerialNumStr = cert.getSerialNumber().toString(),
            certSubjDnStr = cert.getSubjectX500Principal().toString();
        long certIntervalStartTimestamp = cert.getNotBefore().getTime(), certIntervalEndTimestamp = cert.getNotAfter().getTime();

        try {
            jsonGen.writeStringField(ElasticsearchFieldNames.CONTENT, Base64.getEncoder().encodeToString(cert.getEncoded()));
        } catch (CertificateEncodingException e) {
            throw new IOException(
                String.format("Unable to get certificate (version=%d, issuerDn={%s}, subjDn={%s}, serialNum=%s, interval={start=%d, end=%d}) encoded content.",
                    certVersion, certIssuerDnStr, certSubjDnStr, certSerialNumStr, certIntervalStartTimestamp, certIntervalEndTimestamp),
                e);
        }

        jsonGen.writeObjectFieldStart(ElasticsearchFieldNames.INTERVAL);
        jsonGen.writeNumberField(ElasticsearchFieldNames.END, certIntervalEndTimestamp);
        jsonGen.writeNumberField(ElasticsearchFieldNames.START, certIntervalStartTimestamp);
        jsonGen.writeEndObject();

        jsonGen.writeObjectFieldStart(ElasticsearchFieldNames.ISSUER);
        jsonGen.writeStringField(ElasticsearchFieldNames.DN, certIssuerDnStr);
        jsonGen.writeEndObject();

        jsonGen.writeStringField(ElasticsearchFieldNames.SERIAL_NUM, certSerialNumStr);

        jsonGen.writeObjectFieldStart(ElasticsearchFieldNames.SUBJECT);
        jsonGen.writeStringField(ElasticsearchFieldNames.DN, certSubjDnStr);
        jsonGen.writeEndObject();

        jsonGen.writeNumberField(ElasticsearchFieldNames.VERSION, certVersion);

        jsonGen.writeEndObject();
    }
}
