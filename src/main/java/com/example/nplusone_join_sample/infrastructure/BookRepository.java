package com.example.nplusone_join_sample.infrastructure;

import com.example.nplusone_join_sample.domain.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookRepository {

    // N+1の原因クエリ: 著者1件ごとに個別発行される
    List<Book> findByAuthorId(@Param("authorId") Long authorId);

    // 起動時データ初期化用: 書籍を一括INSERT
    void insertAll(@Param("list") List<Book> books);
}
