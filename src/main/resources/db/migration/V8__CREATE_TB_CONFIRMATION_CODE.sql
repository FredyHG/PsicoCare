CREATE TABLE tb_confirmation_code (
        id UUID PRIMARY KEY,
        therapy_id UUID,
        code VARCHAR(10),
        used boolean,
        FOREIGN KEY (therapy_id) REFERENCES tb_therapy(id)
);