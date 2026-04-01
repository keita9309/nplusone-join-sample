package com.example.nplusone_join_sample.infrastructure;

import com.example.nplusone_join_sample.domain.Author;
import com.example.nplusone_join_sample.domain.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * アプリ起動時にN+1問題の効果が出やすいよう大量のテストデータを投入する。
 * 著者50,000件、各著者に1〜10冊の書籍をランダムに生成する。
 * メモリ節約のためCHUNK_SIZE件ずつ分割して保存する。
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    // 著者の総件数
    private static final int AUTHOR_COUNT = 50_000;

    // 著者1人あたりの最大書籍数
    private static final int MAX_BOOKS_PER_AUTHOR = 10;

    // 一度にメモリへ載せる著者の件数（メモリ節約のため分割処理）
    private static final int CHUNK_SIZE = 1_000;

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) {
        for (int start = 1; start <= AUTHOR_COUNT; start += CHUNK_SIZE) {
            int end = Math.min(start + CHUNK_SIZE - 1, AUTHOR_COUNT);
            saveChunk(start, end);
        }
    }

    /**
     * start〜end番の著者と、その著者に紐づく書籍を一括保存する。
     * チャンクごとにトランザクションを分けることでメモリ使用量を一定に保つ。
     *
     * @param start チャンク内の著者番号の開始値
     * @param end   チャンク内の著者番号の終了値
     */
    @Transactional
    public void saveChunk(int start, int end) {
        List<Author> authors = new ArrayList<>(end - start + 1);
        for (int i = start; i <= end; i++) {
            Author author = new Author();
            author.setName(String.format("Author_%05d", i));
            authors.add(author);
        }
        authorRepository.insertAll(authors);

        List<Book> books = new ArrayList<>();
        for (Author author : authors) {
            int bookCount = ThreadLocalRandom.current().nextInt(1, MAX_BOOKS_PER_AUTHOR + 1);
            for (int j = 1; j <= bookCount; j++) {
                Book book = new Book();
                book.setTitle(String.format("%s_Book_%02d", author.getName(), j));
                book.setAuthorId(author.getId());
                books.add(book);
            }
        }
        bookRepository.insertAll(books);
    }
}
