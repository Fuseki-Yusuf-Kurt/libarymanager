CREATE TABLE "person"(
     "id" INTEGER,
    "name" TEXT NOT NULL ,
    "surname" TEXT NOT NULL ,
    "person_type" TEXT NOT NULL ,
    "email" TEXT NOT NULL UNIQUE ,
    "address" TEXT NOT NULL ,
    "birth_date" TEXT NOT NULL,
     PRIMARY KEY("id" AUTOINCREMENT )
);