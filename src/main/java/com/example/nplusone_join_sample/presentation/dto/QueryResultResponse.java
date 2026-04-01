package com.example.nplusone_join_sample.presentation.dto;

import com.example.nplusone_join_sample.application.result.QueryResult;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QueryResultResponse {

    // クエリの種別（例: "N+1問題（改善前）"、"JOIN最適化（改善後）"）
    private final String queryType;

    // クエリの説明文
    private final String description;

    // 処理にかかった時間（ミリ秒）
    private final long executionTimeMs;

    // 実際に発行されたSQLの本数
    private final long sqlQueryCount;

    // 取得した著者の件数
    private final int authorCount;

    public static QueryResultResponse from(QueryResult result) {
        return QueryResultResponse.builder()
            .queryType(result.getQueryType())
            .description(result.getDescription())
            .executionTimeMs(result.getExecutionTimeMs())
            .sqlQueryCount(result.getSqlQueryCount())
            .authorCount(result.getAuthors().size())
            .build();
    }
}
