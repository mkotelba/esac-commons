package com.esacinc.commons.web.servlet.utils;

import com.esacinc.commons.net.http.HttpParameterMultimap;
import com.esacinc.commons.net.http.impl.HttpParameterMultimapImpl;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.iterators.EnumerationIterator;

public final class EsacServletUtils {
    private EsacServletUtils() {
    }

    public static HttpParameterMultimap mapQueryParameters(HttpServletRequest servletReq) {
        HttpParameterMultimap queryParams = new HttpParameterMultimapImpl();
        Map<String, String[]> queryParamArrs = servletReq.getParameterMap();

        if (!queryParamArrs.isEmpty()) {
            queryParamArrs.forEach(queryParams::putAll);
        }

        return queryParams;
    }

    public static HttpParameterMultimap mapHeaders(HttpServletResponse servletResp) {
        HttpParameterMultimap headers = new HttpParameterMultimapImpl();
        Collection<String> headerNames = servletResp.getHeaderNames();

        if (headerNames.isEmpty()) {
            return headers;
        }

        for (String headerName : headerNames) {
            headers.putAll(headerName, servletResp.getHeaders(headerName));
        }

        return headers;
    }

    public static HttpParameterMultimap mapHeaders(HttpServletRequest servletReq) {
        HttpParameterMultimap headers = new HttpParameterMultimapImpl();
        Enumeration<String> headerNames = servletReq.getHeaderNames();

        if (!headerNames.hasMoreElements()) {
            return headers;
        }

        String headerName;

        while (headerNames.hasMoreElements()) {
            headers.putAll((headerName = headerNames.nextElement()), IteratorUtils.asIterable(new EnumerationIterator<>(servletReq.getHeaders(headerName))));
        }

        return headers;
    }
}
