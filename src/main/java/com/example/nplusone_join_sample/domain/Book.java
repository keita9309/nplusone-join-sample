package com.example.nplusone_join_sample.domain;

import lombok.Data;

@Data
public class Book {

    // 書籍ID
    private Long id;

    // 書籍タイトル
    private String title;

    // この書籍を執筆した著者のID
    private Long authorId;
}
