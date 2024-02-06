CREATE TABLE tb_user_token (
                               id UUID PRIMARY KEY,
                               token VARCHAR(255),
                               token_type VARCHAR(50),
                               revoked BOOLEAN,
                               expired BOOLEAN,
                               user_id UUID,
                               FOREIGN KEY (user_id) REFERENCES tb_user(id)
);