--liquibase formatted sql

--changeset dolgaia:2

insert into POSTS (title, content, likes, image) values ('Первый пост', 'Содержимое поста', 10, NULL);
insert into POSTS (title, content, likes, image) values ('Второй пост', 'Содержимое поста', 10, NULL);

insert into COMMENTS (text, post_id) values ( 'comment1' , 1);
insert into COMMENTS (text, post_id) values ( 'comment2' , 1);
insert into COMMENTS (text, post_id) values ( 'comment3' , 2);

insert into TAGS (text, post_id) values ( 'tag1' , 1);
insert into TAGS (text, post_id) values ( 'tag2' , 1);
insert into TAGS (text, post_id) values ( 'tag3' , 2);

--rollback delete * from POSTS;
--rollback delete * from TAGS;
--rollback delete * from COMMENTS;