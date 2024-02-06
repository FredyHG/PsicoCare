CREATE TABLE tb_user (
                         id UUID PRIMARY KEY,
                         username VARCHAR(100),
                         password VARCHAR(100),
                         role VARCHAR(50),
                         psychologist_id UUID,
                         FOREIGN KEY (psychologist_id) REFERENCES tb_psychologist(id)
);