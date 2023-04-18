--liquibase formatted sql

--changeset maxzhukoff:1
CREATE TABLE IF NOT EXISTS link
(
    id            BIGSERIAL PRIMARY KEY,
    url           VARCHAR(255) UNIQUE NOT NULL,
    updated_at    TIMESTAMP    NOT NULL,
    last_check_at TIMESTAMP    NOT NULL
);

--changeset maxzhukoff:2
CREATE TABLE IF NOT EXISTS chat
(
    id  BIGINT PRIMARY KEY
);

--changeset maxzhukoff:3
CREATE TABLE IF NOT EXISTS link_chat
(
    link_id BIGINT NOT NULL REFERENCES link(id) ON DELETE CASCADE,
    chat_id BIGINT NOT NULL REFERENCES chat(id) ON DELETE CASCADE,
    PRIMARY KEY (link_id, chat_id)
);
