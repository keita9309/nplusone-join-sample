package com.example.nplusone_join_sample.presentation.dto;

import com.example.nplusone_join_sample.application.data.AuthorData;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AuthorResponse {

    // 著者ID
    private final Long id;

    // 著者名
    private final String name;

    // その著者が執筆した書籍一覧
    private final List<BookResponse> books;

    public static AuthorResponse from(AuthorData authorData) {
        List<BookResponse> books = authorData.getBooks().stream()
            .map(BookResponse::from)
            .toList();
        return AuthorResponse.builder()
            .id(authorData.getId())
            .name(authorData.getName())
            .books(books)
            .build();
    }
}
