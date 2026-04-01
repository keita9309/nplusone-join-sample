package com.example.nplusone_join_sample.application.data;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AuthorData {

    // 著者ID
    private final Long id;

    // 著者名
    private final String name;

    // その著者が執筆した書籍一覧
    private final List<BookData> books;
}
