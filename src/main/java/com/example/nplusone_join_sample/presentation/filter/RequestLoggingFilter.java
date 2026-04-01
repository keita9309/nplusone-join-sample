package com.example.nplusone_join_sample.presentation.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * リクエストごとにMDCへタイムスタンプをセットするフィルター。
 * logbackのSiftingAppenderがこのキーを読み取り、リクエストごとに別ファイルへSQLを出力する。
 */
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    static final String MDC_KEY = "requestTimestamp";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        MDC.put(MDC_KEY, LocalDateTime.now().format(FORMATTER));
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }
}
