CREATE TABLE "orders"
(
    "id"          INTEGER,
    "book_id"     INTEGER,
    "person_id"   INTEGER,
    "begin_date"  TEXT,
    "return_date" TEXT,
    "end_date"    TEXT,
    "order_type"      TEXT,
    PRIMARY KEY ("id"),
    FOREIGN KEY ("book_id") REFERENCES "book" ("id"),
    FOREIGN KEY ("person_id") REFERENCES "person" ("id")
)