CREATE TABLE tb_patient (
        id UUID PRIMARY KEY,
        name VARCHAR(50),
        last_name VARCHAR(50),
        birth_date DATE,
        phone VARCHAR(24),
        cpf VARCHAR(14),
        email VARCHAR(120),
        create_at DATE
);