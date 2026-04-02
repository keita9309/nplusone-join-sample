package com.example.nplusone_join_sample.application;

import com.example.nplusone_join_sample.application.data.AuthorData;
import com.example.nplusone_join_sample.application.data.BookData;
import com.example.nplusone_join_sample.application.result.QueryResult;
import com.example.nplusone_join_sample.domain.Author;
import com.example.nplusone_join_sample.domain.Book;
import com.example.nplusone_join_sample.infrastructure.AuthorRepository;
import com.example.nplusone_join_sample.infrastructure.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorQueryService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    /**
     * 著者と書籍の一覧を取得する。
     * useJoin=false でN+1問題、true でLEFT JOINを実行し、
     * 実行時間とSQL本数を計測して返す。
     *
     * @param useJoin false=N+1問題、true=LEFT JOIN最適化
     * @return クエリ種別・SQL本数・実行時間・著者一覧を含む結果
     */
    @Transactional(readOnly = true)
    public QueryResult findAuthors(boolean useJoin) {
        SqlQueryCounter.reset();
        long startTime = System.currentTimeMillis();
        List<AuthorData> authors;

        if (useJoin) {
            // 改善後: LEFT JOINで著者と書籍を1クエリで取得
            // MyBatisのresultMapがフラットなJOIN結果をAuthor + List<Book>に変換する
            authors = mapToAuthorDataList(authorRepository.findAllWithBooks());
        } else {
            // 改善前: 著者N件 + 各著者の書籍N回 = N+1クエリが発行される
            authors = loadBooksPerAuthor(authorRepository.findAllAuthors());
        }

        long executionTimeMs = System.currentTimeMillis() - startTime;
        long sqlQueryCount = SqlQueryCounter.get();

        log.info("[{}] SQL発行本数: {}件, 実行時間: {}ms",
            useJoin ? "LEFT JOIN最適化（改善後）" : "N+1問題（改善前）",
            sqlQueryCount,
            executionTimeMs);

        if (useJoin) {
            return QueryResult.builder()
                .queryType("LEFT JOIN最適化（改善後）")
                .description("LEFT JOINにより著者と書籍を1回のSQLで取得します。著者数に関わらずSQLは常に1回です。")
                .executionTimeMs(executionTimeMs)
                .sqlQueryCount(sqlQueryCount)
                .authors(authors)
                .build();
        } else {
            return QueryResult.builder()
                .queryType("N+1問題（改善前）")
                .description("著者一覧を1回取得後、各著者の書籍を個別に取得するため著者数+1回のSQLが発行されます。")
                .executionTimeMs(executionTimeMs)
                .sqlQueryCount(sqlQueryCount)
                .authors(authors)
                .build();
        }
    }

    /**
     * N+1問題の再現。著者ごとに個別SQLで書籍を取得するため、著者N件に対してN+1本のSQLが発行される。
     *
     * @param authors 著者エンティティの一覧
     * @return 書籍を含む著者データの一覧
     */
    private List<AuthorData> loadBooksPerAuthor(List<Author> authors) {
        return authors.stream()
            .map(author -> {
                List<Book> books = bookRepository.findByAuthorId(author.getId());
                return mapToAuthorData(author, books);
            })
            .toList();
    }

    /**
     * LEFT JOINで取得済みの著者一覧をデータクラスに変換する。
     * MyBatisのresultMapがJOIN結果をAuthor.booksに設定済みのため、追加SQLは発行されない。
     *
     * @param authors 書籍を含む著者の一覧
     * @return 書籍を含む著者データの一覧
     */
    private List<AuthorData> mapToAuthorDataList(List<Author> authors) {
        return authors.stream()
            .map(author -> mapToAuthorData(author, author.getBooks()))
            .toList();
    }

    /**
     * 著者・書籍をアプリ層のデータクラスに詰め替える。
     * ドメインオブジェクトをそのまま上位層に渡さないための変換処理。
     *
     * @param author 著者
     * @param books  その著者に紐づく書籍の一覧（nullの場合は空リストとして扱う）
     * @return 書籍を含む著者データ
     */
    private AuthorData mapToAuthorData(Author author, List<Book> books) {
        List<BookData> bookDataList = Optional.ofNullable(books).orElse(List.of()).stream()
            .map(book -> BookData.builder()
                .id(book.getId())
                .title(book.getTitle())
                .build())
            .toList();
        return AuthorData.builder()
            .id(author.getId())
            .name(author.getName())
            .books(bookDataList)
            .build();
    }
}
