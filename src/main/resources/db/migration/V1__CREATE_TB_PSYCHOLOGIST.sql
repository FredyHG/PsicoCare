CREATE TABLE tb_psychologist (
        id UUID PRIMARY KEY,
        name VARCHAR(50),
        last_name VARCHAR(50),
        crp VARCHAR(20),
        email VARCHAR(120),
        phone VARCHAR(20),
        create_at DATE
);