package com.example.nplusone_join_sample.presentation;

import com.example.nplusone_join_sample.application.AuthorQueryService;
import com.example.nplusone_join_sample.application.result.QueryResult;
import com.example.nplusone_join_sample.presentation.dto.QueryResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorQueryService authorQueryService;

    @GetMapping
    public ResponseEntity<QueryResultResponse> getAuthors(
            @RequestParam boolean useJoin) {
        QueryResult result = authorQueryService.findAuthors(useJoin);
        return ResponseEntity.ok(QueryResultResponse.from(result));
    }
}
