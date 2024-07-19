CREATE TABLE "book"
(
    "id"            INTEGER,
    "title"         TEXT NOT NULL,
    "author"        TEXT,
    "genre"         TEXT,
    "release_date"  TEXT,
    "lend_date"     TEXT,
    "reserved_date" TEXT,
    "reserved"      INTEGER,

    PRIMARY KEY ("id")
);