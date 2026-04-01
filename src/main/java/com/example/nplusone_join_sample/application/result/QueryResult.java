package com.example.nplusone_join_sample.application.result;

import com.example.nplusone_join_sample.application.data.AuthorData;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QueryResult {

    // クエリの種別（例: "N+1問題（改善前）"、"JOIN最適化（改善後）"）
    private final String queryType;

    // クエリの説明文
    private final String description;

    // 処理にかかった時間（ミリ秒）
    private final long executionTimeMs;

    // 実際に発行されたSQLの本数
    private final long sqlQueryCount;

    // 取得した著者一覧
    private final List<AuthorData> authors;
}
