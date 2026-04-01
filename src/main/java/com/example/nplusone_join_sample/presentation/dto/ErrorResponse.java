package com.example.nplusone_join_sample.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    // HTTPステータスコード
    private final int status;

    // エラーメッセージ
    private final String message;
}
