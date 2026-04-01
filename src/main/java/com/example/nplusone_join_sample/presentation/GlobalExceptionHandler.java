package com.example.nplusone_join_sample.presentation;

import com.example.nplusone_join_sample.presentation.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 必須クエリパラメータが未指定の場合（例: isImprovedNPlusOne が未指定）。
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .message("必須パラメータが不足しています: " + e.getParameterName())
            .build());
    }

    /**
     * 予期しないエラーが発生した場合の汎用ハンドラー。
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.internalServerError().body(ErrorResponse.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("予期しないエラーが発生しました: " + e.getMessage())
            .build());
    }
}
