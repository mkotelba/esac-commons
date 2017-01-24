package com.esacinc.commons.crypto.time.impl;

import com.esacinc.commons.crypto.time.IntervalInfo;
import java.util.Date;

public class IntervalInfoImpl extends AbstractIntervalDescriptor implements IntervalInfo {
    private Date startDate;
    private Date endDate;

    public IntervalInfoImpl(Date startDate, Date endDate) {
        this.duration = ((this.endDate = endDate).getTime() - (this.startDate = startDate).getTime());
    }

    @Override
    public boolean isValid(Date date) {
        return date.after(this.startDate) && date.before(this.endDate);
    }

    @Override
    public Date getEndDate() {
        return this.endDate;
    }

    @Override
    public Date getStartDate() {
        return this.startDate;
    }
}
