# N+1 Problem vs LEFT JOIN Performance Comparison

## 📌 Overview

本リポジトリは、データアクセスにおける代表的なパフォーマンス問題である
**N+1問題**を再現し、**LEFT JOINによる改善効果を検証すること**を目的としています。

Spring Boot + MyBatis を用いて、以下の2パターンを比較します。

* ❌ N+1パターン（ループ内でクエリ発行）
* ✅ LEFT JOINパターン（単一クエリで取得）

---

## 🎯 Purpose

* N+1問題の発生メカニズムを理解する
* SQL発行回数と実行時間の違いを可視化する
* 実務でのパフォーマンスチューニングの基礎理解

---

## 🏗️ Architecture

```
src/main/java/com/example/nplusone_join_sample
├── application   # ユースケース層（QueryService）
├── domain        # ドメインモデル
├── infrastructure# DB / MyBatis / 初期データ
└── presentation  # Controller / Filter
```

👉 3層 + ドメインモデル構成を採用

---

## ⚙️ Tech Stack

* Java 21
* Spring Boot 3.x
* MyBatis
* H2 Database
* Lombok

---

## 🧪 How to Run

```bash
git clone https://github.com/keita9309/nplusone-join-sample.git
cd nplusone-join-sample

./mvnw spring-boot:run
```

---

## 🌐 API

### N+1問題の再現

```
GET /api/authors?isImprovedNPlusOne=true
```

### LEFT JOINによる改善

```
GET /api/authors?isImprovedNPlusOne=false
```

---

## 📊 Example Result

| Type      | SQL Count | Execution Time |
| --------- | --------- | -------------- |
| N+1       | 50001     | 1500ms         |
| LEFT JOIN | 1         | 300ms          |

※ 実行環境により変動

---

## 🔍 What is N+1 Problem?

N+1問題とは、
「親データ取得後に、子データをループで取得することで
SQLが大量発行される問題」です。

```java
for (Author author : authors) {
    bookRepository.findByAuthorId(author.getId());
}
```

👉 著者数 = N のとき
👉 **SQL = N + 1回**

---

## 💡 Solution (LEFT JOIN)

```sql
SELECT a.*, b.*
FROM authors a
LEFT JOIN books b ON a.id = b.author_id;
```

👉 SQLは1回
👉 DB側で結合

---

## ⚠️ Important Note

LEFT JOINは万能ではありません。

* データ件数が多い場合、JOIN結果が巨大化する
* メモリ使用量が増える可能性がある

実務では以下の選択も重要です：

* ページネーション
* バッチ取得
* キャッシュ

---

## 🔥 Key Learning

* N+1問題はパフォーマンス劣化の典型例
* SQL本数削減が重要
* JOINは有効な解決策の一つ
* ただし状況に応じた設計が必要

---

## 📈 Improvements (Future Work)

* ページネーション対応
* DTO最適化
* QueryDSL / JPA版の比較
* キャッシュ導入（Redis）

---

## 👤 Author

Keita

Backend Engineer (Java / Spring Boot)
