CREATE TABLE therapy_patient (
        patient_id uuid,
        therapy_id uuid,
        PRIMARY KEY (patient_id, therapy_id),
        FOREIGN KEY (patient_id) REFERENCES tb_patient(id),
        FOREIGN KEY (therapy_id) REFERENCES tb_therapy(id)
);