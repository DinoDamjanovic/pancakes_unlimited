CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    INDEX user_id_idx (user_id ASC) VISIBLE,
    INDEX role_id_idx (role_id ASC) VISIBLE,
    CONSTRAINT user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT role_id
        FOREIGN KEY (role_id)
            REFERENCES roles (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);