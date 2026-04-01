package com.example.nplusone_join_sample.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Author {

    // 著者ID
    private Long id;

    // 著者名
    private String name;

    // その著者が執筆した書籍一覧（MyBatisのresultMapで設定される）
    private List<Book> books = new ArrayList<>();
}
