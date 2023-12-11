CREATE TABLE tb_therapy (
        id UUID PRIMARY KEY,
        patient_id UUID,
        psychologist_id UUID,
        status VARCHAR(50),
        date TIMESTAMP,
        create_at TIMESTAMP,
        FOREIGN KEY (patient_id) REFERENCES tb_patient(id),
        FOREIGN KEY (psychologist_id) REFERENCES tb_psychologist(id)
);