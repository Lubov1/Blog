CREATE TABLE IF NOT EXISTS POSTS (
                                     ID serial PRIMARY KEY,                                     TITLE VARCHAR(255) NOT NULL,
                                     CONTENT VARCHAR(255) NOT NULL,
                                     LIKES INT DEFAULT 0,
                                     IMAGE bytea
);
CREATE TABLE IF NOT EXISTS COMMENTS (
                                        ID serial PRIMARY KEY,
                                        text VARCHAR(255) NOT NULL,
                                        post_id BIGINT NOT NULL,
                                        FOREIGN KEY (post_id) REFERENCES POSTS(ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TAGS (
                                    ID serial PRIMARY KEY,
                                    text VARCHAR(255) NOT NULL,
                                    post_id BIGINT NOT NULL,
                                    FOREIGN KEY (post_id) REFERENCES POSTS(ID) ON DELETE CASCADE
);
insert into POSTS (title, content, likes, image) values ('Второй пост', 'Содержимое поста', 10, NULL);

insert into COMMENTS (text, post_id) values ( 'comment1' , 1);
insert into COMMENTS (text, post_id) values ( 'comment2' , 1);
insert into COMMENTS (text, post_id) values ( 'comment3' , 2);
insert into COMMENTS (TEXT, POST_ID) values ( 'comment2' , 1);

insert into TAGS (text, post_id) values ( 'tag1' , 1);
insert into TAGS (text, post_id) values ( 'tag2' , 1);
insert into TAGS (text, post_id) values ( 'tag3' , 2);