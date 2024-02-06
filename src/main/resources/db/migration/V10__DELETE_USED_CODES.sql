CREATE OR REPLACE FUNCTION delete_on_used_true()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.used = true THEN
        DELETE FROM tb_confirmation_code WHERE id = NEW.id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_delete_on_used_true
    AFTER UPDATE ON tb_confirmation_code
    FOR EACH ROW
    WHEN (NEW.used IS DISTINCT FROM OLD.used AND NEW.used = true)
EXECUTE FUNCTION delete_on_used_true();
