package com.esacinc.commons.json.impl;

import com.esacinc.commons.utils.EsacStringUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import java.io.IOException;
import javax.annotation.Nonnegative;
import org.apache.commons.lang3.StringUtils;

public class EsacPrettyPrinter extends DefaultPrettyPrinter {
    public final static SerializedString FIELD_VALUE_DELIM = new SerializedString(": ");

    private final static long serialVersionUID = 0L;

    private int indentSize;

    public EsacPrettyPrinter(EsacPrettyPrinter prettyPrinter) {
        super(prettyPrinter);

        this.indentSize = prettyPrinter.getIndentSize();
    }

    public EsacPrettyPrinter(@Nonnegative int indentSize) {
        super(((SerializableString) null));

        this._objectIndenter = this._arrayIndenter = new DefaultIndenter(StringUtils.repeat(StringUtils.SPACE, (this.indentSize = indentSize)), StringUtils.LF);
    }

    @Override
    public void writeObjectFieldValueSeparator(JsonGenerator jsonGen) throws IOException {
        if (this._spacesInObjectEntries) {
            jsonGen.writeRaw(FIELD_VALUE_DELIM);
        } else {
            jsonGen.writeRaw(EsacStringUtils.COLON_CHAR);
        }
    }

    @Override
    public EsacPrettyPrinter createInstance() {
        return new EsacPrettyPrinter(this);
    }

    @Nonnegative
    public int getIndentSize() {
        return this.indentSize;
    }
}
