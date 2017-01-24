package com.esacinc.commons.config.property.json.impl;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.esacinc.commons.config.property.PropertyTrie;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class PropertyTrieJsonGenerator extends GeneratorBase {
    private final static String WRITE_MSG_PREFIX = "write ";

    private final static String WRITE_START_MSG_SUFFIX = " start";

    private final static String WRITE_ARRAY_START_MSG = WRITE_MSG_PREFIX + "array" + WRITE_START_MSG_SUFFIX;
    private final static String WRITE_OBJ_START_MSG = WRITE_MSG_PREFIX + "object" + WRITE_START_MSG_SUFFIX;

    private PropertyTrie<Object> fields;

    public PropertyTrieJsonGenerator(JsonGenerator gen, PropertyTrie<Object> fields) {
        super(gen.getFeatureMask(), gen.getCodec(), new PropertyJsonWriteContext());

        this.fields = fields;
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void writeUTF8String(byte[] value, int offset, int len) throws IOException {
        this.writeString(new String(value, offset, len, StandardCharsets.UTF_8));
    }

    @Override
    public void writeString(char[] value, int offset, int len) throws IOException {
        this.writeString(new String(value, offset, len));
    }

    @Override
    public void writeString(@Nullable String value) throws IOException {
        this.writeValue(WRITE_STRING, value);
    }

    @Override
    public void writeNumber(int value) throws IOException {
        this.writeValue(WRITE_NUMBER, value);
    }

    @Override
    public void writeNumber(long value) throws IOException {
        this.writeValue(WRITE_NUMBER, value);
    }

    @Override
    public void writeNumber(@Nullable BigInteger value) throws IOException {
        this.writeValue(WRITE_NUMBER, Objects.toString(value, StringUtils.EMPTY));
    }

    @Override
    public void writeNumber(float value) throws IOException {
        this.writeValue(WRITE_NUMBER, value);
    }

    @Override
    public void writeNumber(double value) throws IOException {
        this.writeValue(WRITE_NUMBER, value);
    }

    @Override
    public void writeNumber(@Nullable BigDecimal value) throws IOException {
        this.writeValue(WRITE_NUMBER,
            ((value != null) ? (this.isEnabled(Feature.WRITE_BIGDECIMAL_AS_PLAIN) ? value.toPlainString() : value.toString()) : null));
    }

    @Override
    public void writeNumber(@Nullable String value) throws IOException {
        this.writeValue(WRITE_NUMBER, value);
    }

    @Override
    public void writeBoolean(boolean value) throws IOException {
        this.writeValue(WRITE_BOOLEAN, value);
    }

    @Override
    public void writeNull() throws IOException {
        this.writeValue(WRITE_NULL, null);
    }

    @Override
    public void writeBinary(Base64Variant value, byte[] data, int offset, int len) throws IOException {
        this._reportUnsupportedOperation();
    }

    @Override
    public void writeRawUTF8String(byte[] value, int offset, int len) throws IOException {
        this._reportUnsupportedOperation();
    }

    @Override
    public void writeRaw(String value) throws IOException {
        this._reportUnsupportedOperation();
    }

    @Override
    public void writeRaw(String value, int offset, int len) throws IOException {
        this._reportUnsupportedOperation();
    }

    @Override
    public void writeRaw(char[] value, int offset, int len) throws IOException {
        this._reportUnsupportedOperation();
    }

    @Override
    public void writeRaw(char value) throws IOException {
        this._reportUnsupportedOperation();
    }

    @Override
    public void writeEndArray() throws IOException {
        if (!this._writeContext.inArray()) {
            this._reportError(String.format("Unable to write array end in context (desc=%s)", this._writeContext));
        }

        this._writeContext = this._writeContext.getParent();
    }

    @Override
    public void writeStartArray() throws IOException {
        this._verifyValueWrite(WRITE_ARRAY_START_MSG);

        PropertyJsonWriteContext writeContext = ((PropertyJsonWriteContext) this._writeContext).createChildArrayContext();

        this.fields.put(writeContext.getCurrentPropertyName(), writeContext.getPropertyValues());

        this._writeContext = writeContext;
    }

    @Override
    public void writeFieldName(String name) throws IOException {
        if (this._writeContext.writeFieldName(name) == JsonWriteContext.STATUS_EXPECT_VALUE) {
            this._reportError("Unable to write field name - value expected.");
        }
    }

    @Override
    public void writeEndObject() throws IOException {
        if (!this._writeContext.inObject()) {
            this._reportError(String.format("Unable to write object end in context (desc=%s)", this._writeContext));
        }

        this._writeContext = this._writeContext.getParent();
    }

    @Override
    public void writeStartObject() throws IOException {
        this._verifyValueWrite(WRITE_OBJ_START_MSG);

        this._writeContext = this._writeContext.createChildObjectContext();
    }

    @Override
    protected void _releaseBuffers() {
    }

    @Override
    protected void _verifyValueWrite(String typeMsg) throws IOException {
        if (this._writeContext.writeValue() == JsonWriteContext.STATUS_EXPECT_NAME) {
            this._reportError(String.format("Unable to %s in context (desc=%s) - field name expected.", typeMsg, this._writeContext));
        }
    }

    private void writeValue(String typeMsg, @Nullable Object value) throws IOException {
        this._verifyValueWrite(typeMsg);

        PropertyJsonWriteContext writeContext = ((PropertyJsonWriteContext) this._writeContext);

        if (writeContext.inArray()) {
            writeContext.getPropertyValues().add(value);
        } else {
            this.fields.put(writeContext.getCurrentPropertyName(), value);
        }
    }

    @Override
    public PropertyTrie<Object> getOutputTarget() {
        return this.fields;
    }
}
