CREATE TABLE code_therapy (
        confirmation_code_id uuid,
        therapy_id uuid,
        PRIMARY KEY (confirmation_code_id, therapy_id),
        FOREIGN KEY (confirmation_code_id) REFERENCES tb_confirmation_code(id) ON DELETE CASCADE,
        FOREIGN KEY (therapy_id) REFERENCES tb_therapy(id) ON DELETE CASCADE
);