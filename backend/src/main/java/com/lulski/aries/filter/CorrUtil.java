package com.lulski.aries.filter;

import org.slf4j.MDC;
import reactor.core.publisher.Signal;
import reactor.util.context.ContextView;

public class CorrUtil {
    public static <T> void logWithCorrId(Signal<T> signal) {
        if (!signal.isOnNext())
            return;

        ContextView contextView = signal.getContextView();
        if (contextView.hasKey(CorrelationIdFilter.CORRELATION_ID_KEY)) {
            String corrId = contextView.get(CorrelationIdFilter.CORRELATION_ID_KEY);
            MDC.put("corrId", corrId);
        }
    }
}
