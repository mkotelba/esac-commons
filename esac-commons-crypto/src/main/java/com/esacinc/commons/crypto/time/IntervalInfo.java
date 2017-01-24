package com.esacinc.commons.crypto.time;

import com.esacinc.commons.beans.InfoBean;
import java.util.Date;

public interface IntervalInfo extends InfoBean, IntervalDescriptor {
    public boolean isValid(Date date);

    public Date getEndDate();

    public Date getStartDate();
}
