CREATE TABLE therapy_psychologist (
        psychologist_id uuid,
        therapy_id uuid,
        PRIMARY KEY (psychologist_id, therapy_id),
        FOREIGN KEY (psychologist_id) REFERENCES tb_psychologist(id),
        FOREIGN KEY (therapy_id) REFERENCES tb_therapy(id)
);