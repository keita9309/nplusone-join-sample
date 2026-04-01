package com.example.nplusone_join_sample.infrastructure;

import com.example.nplusone_join_sample.application.SqlQueryCounter;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

/**
 * MyBatisのSQLクエリ発行をインターセプトし、SqlQueryCounterをインクリメントする。
 * SELECT文のみをカウント対象とする。
 */
@Component
@Intercepts({
    @Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, org.apache.ibatis.cache.CacheKey.class, BoundSql.class})
})
public class SqlQueryCountInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        SqlQueryCounter.increment();
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
