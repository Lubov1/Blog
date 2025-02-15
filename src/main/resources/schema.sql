

CREATE TABLE IF NOT EXISTS POSTS (
                                     ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     TITLE VARCHAR(255) NOT NULL,
                                     CONTENT VARCHAR(255) NOT NULL,  -- ✅ Вместо `TEXT`
                                     LIKES INT DEFAULT 0,
                                     IMAGE VARBINARY(1048576)  -- ✅ Вместо `VARBINARY(MAX)` (1 MB)
);
CREATE TABLE IF NOT EXISTS COMMENTS (
                                     ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     TEXT VARCHAR(255) NOT NULL,
                                     POST_ID BIGINT NOT NULL,
                                     FOREIGN KEY (POST_ID) REFERENCES POSTS(ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TAGS (
                                        ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        TEXT VARCHAR(255) NOT NULL,
                                        POST_ID BIGINT NOT NULL,
                                        FOREIGN KEY (POST_ID) REFERENCES POSTS(ID) ON DELETE CASCADE
);
insert into POSTS (title, content, likes, image) values ('Первый пост', 'Содержимое поста', 10, NULL);
insert into POSTS (title, content, likes, image) values ('Второй пост', 'Текст второго поста', 5, NULL);

