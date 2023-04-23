--liquibase formatted sql

--changeset maxzhukoff:1
CREATE OR REPLACE FUNCTION delete_link_if_not_in_link_chat()
    RETURNS TRIGGER AS
'
BEGIN
    DELETE
    FROM link
    WHERE id = OLD.link_id
      AND NOT EXISTS (SELECT link_id FROM link_chat WHERE link_id = OLD.link_id);
    RETURN OLD;
END;
'
    LANGUAGE plpgsql;

CREATE TRIGGER link_chat_delete_trigger
    AFTER DELETE
    ON link_chat
    FOR EACH ROW
EXECUTE FUNCTION delete_link_if_not_in_link_chat();