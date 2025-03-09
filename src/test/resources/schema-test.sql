CREATE TABLE IF NOT EXISTS posts (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
                                     content VARCHAR(255) NOT NULL,
                                     likes INT DEFAULT 0,
                                     image VARBINARY(1048576)
);
CREATE TABLE IF NOT EXISTS comments (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        text VARCHAR(255) NOT NULL,
                                        post_id BIGINT NOT NULL,
                                        FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tags (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    text VARCHAR(255) NOT NULL,
                                    post_id BIGINT NOT NULL,
                                    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

insert into posts (title, content, likes, image) values ('Первый пост', 'Содержимое поста', 10, NULL);
insert into posts (title, content, likes, image) values ('Второй пост', 'Содержимое поста', 10, NULL);

insert into comments (text, post_id) values ( 'comment1' , 1);
insert into comments (text, post_id) values ( 'comment2' , 1);
insert into comments (text, post_id) values ( 'comment3' , 2);

insert into tags (text, post_id) values ( 'tag1' , 1);
insert into tags (text, post_id) values ( 'tag2' , 1);
insert into tags (text, post_id) values ( 'tag3' , 2);