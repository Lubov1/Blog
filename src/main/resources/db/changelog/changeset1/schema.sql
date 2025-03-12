--liquibase formatted sql

--changeset dolgaia:1

CREATE TABLE IF NOT EXISTS POSTS (
                                     ID serial PRIMARY KEY,
                                     TITLE VARCHAR(255) NOT NULL,
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

--rollback drop table POSTS;
--rollback drop table TAGS;
--rollback drop table COMMENTS;