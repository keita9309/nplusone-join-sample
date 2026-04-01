package com.example.nplusone_join_sample.infrastructure;

import com.example.nplusone_join_sample.domain.Author;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthorRepository {

    // N+1問題側: 著者のみ取得（書籍は別途クエリで取得）
    List<Author> findAllAuthors();

    // N+1改善側: LEFT JOINで著者と書籍を1クエリで取得し、resultMapでマッピング
    List<Author> findAllWithBooks();

    // 起動時データ初期化用: 著者を一括INSERT
    void insertAll(@Param("list") List<Author> authors);
}
