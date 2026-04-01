package com.example.nplusone_join_sample.presentation.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * HTTPリクエスト中のみSQLログを出力するためのlogbackフィルター。
 * MDCにrequestTimestampがセットされていない場合（アプリ起動時など）はログを破棄する。
 */
public class RequestOnlyFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        return event.getMDCPropertyMap().containsKey(RequestLoggingFilter.MDC_KEY)
            ? FilterReply.ACCEPT
            : FilterReply.DENY;
    }
}
