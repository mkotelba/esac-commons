package com.esacinc.commons.xml.xpath;

import com.esacinc.commons.xml.DynamicXmlTransformOptions;
import javax.annotation.Nullable;
import net.sf.saxon.om.Item;

public interface DynamicXpathOptions extends DynamicXmlTransformOptions<DynamicXpathOptions> {
    public boolean hasContextItem();

    @Nullable
    public Item getContextItem();

    public DynamicXpathOptions setContextItem(@Nullable Item contextItem);
}
