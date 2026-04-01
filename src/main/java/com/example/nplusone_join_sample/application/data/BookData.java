package com.example.nplusone_join_sample.application.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookData {

    // 書籍ID
    private final Long id;

    // 書籍タイトル
    private final String title;
}
