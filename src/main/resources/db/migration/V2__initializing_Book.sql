CREATE TABLE "book"
(
    "id"           INTEGER,
    "title"        TEXT NOT NULL,
    "author"       TEXT,
    "genre"        TEXT,
    "release_date" TEXT,
    "busy_date" TEXT,

    PRIMARY KEY ("id")
);