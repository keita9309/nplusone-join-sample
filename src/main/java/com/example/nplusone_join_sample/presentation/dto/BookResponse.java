package com.example.nplusone_join_sample.presentation.dto;

import com.example.nplusone_join_sample.application.data.BookData;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookResponse {

    // 書籍ID
    private final Long id;

    // 書籍タイトル
    private final String title;

    public static BookResponse from(BookData bookData) {
        return BookResponse.builder()
            .id(bookData.getId())
            .title(bookData.getTitle())
            .build();
    }
}
