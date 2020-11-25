DROP TABLE IF EXISTS code_group CASCADE;
CREATE TABLE code_group (
                            id           varchar(50) NOT NULL,
                            code         varchar(10) NOT NULL,
                            name_eng     varchar(10) NOT NULL,
                            name_kor     varchar(10) NOT NULL,
                            create_at    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
                            update_at    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
                            PRIMARY KEY (id),
                            CONSTRAINT unq_code_group_code UNIQUE (code)
);

DROP TABLE IF EXISTS common_code CASCADE;
CREATE TABLE common_code (
                             id              varchar(50) NOT NULL,
                             code            varchar(10) NOT NULL,
                             name_eng        varchar(10) NOT NULL,
                             name_kor        varchar(10) NOT NULL,
                             group_id        varchar(50) NOT NULL,
                             create_at       datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
                             update_at       datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
                             PRIMARY KEY (id),
                             CONSTRAINT unq_common_code UNIQUE (code),
                             CONSTRAINT fk_common_code_to_code_group FOREIGN KEY (group_id) REFERENCES code_group (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS member CASCADE;
CREATE TABLE member (
                             id              varchar(50) NOT NULL,
                             user_id         varchar(50) NOT NULL,
                             password        varchar(255) NOT NULL,
                             name            varchar(50) NOT NULL,
                             tel             varchar(50),
                             addr1           varchar(255),
                             addr2           varchar(255),
                             rating          char NOT NULL DEFAULT 'B',
                             reserves        number NOT NULL DEFAULT 0,
                             create_at       datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
                             update_at       datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
                             PRIMARY KEY (id),
                             CONSTRAINT unq_user_id UNIQUE (user_id)
);



