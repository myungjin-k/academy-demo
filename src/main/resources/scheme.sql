DROP TABLE IF EXISTS code_group CASCADE;
CREATE TABLE code_group (
                            id           varchar(50) NOT NULL,
                            code         varchar(10) NOT NULL,
                            name_eng     varchar(10) NOT NULL,
                            name_kor     varchar(10) NOT NULL,
                            create_at    datetime DEFAULT CURRENT_TIMESTAMP(),
                            update_at    datetime DEFAULT CURRENT_TIMESTAMP(),
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
                             create_at       datetime DEFAULT CURRENT_TIMESTAMP(),
                             update_at       datetime DEFAULT CURRENT_TIMESTAMP(),
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
                             email           varchar(100) NOT NULL,
                             tel             varchar(50) NOT NULL,
                             addr1           varchar(255),
                             addr2           varchar(255),
                             rating          char DEFAULT 'B',
                             reserves        number DEFAULT 0,
                             create_at       datetime DEFAULT CURRENT_TIMESTAMP(),
                             update_at       datetime DEFAULT CURRENT_TIMESTAMP(),
                             PRIMARY KEY (id),
                             CONSTRAINT unq_user_id UNIQUE (user_id),
                             CONSTRAINT unq_email UNIQUE (email),
                             CONSTRAINT unq_tel UNIQUE (tel)
);


DROP TABLE IF EXISTS admin CASCADE;
CREATE TABLE admin (
                        id              varchar(50) NOT NULL,
                        admin_id        varchar(50) NOT NULL,
                        password        varchar(255) NOT NULL,
                        create_at       datetime DEFAULT CURRENT_TIMESTAMP(),
                        update_at       datetime DEFAULT CURRENT_TIMESTAMP(),
                        PRIMARY KEY (id),
                        CONSTRAINT unq_admin_id UNIQUE (admin_id)
);



DROP TABLE IF EXISTS item_master CASCADE;
CREATE TABLE item_master (
                       id                   varchar(50) NOT NULL,
                       item_name            varchar(50) NOT NULL,
                       main_category_id     varchar(255) NOT NULL,
                       sub_category_id      varchar(255) NOT NULL,
                       price                number DEFAULT 0,
                       detail_image_url     varchar(255),
                       status               number DEFAULT 0,
                       create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                       update_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                       PRIMARY KEY (id),
                       CONSTRAINT unq_item_name UNIQUE (item_name)
);
DROP TABLE IF EXISTS item_display CASCADE;
CREATE TABLE item_display (
                             id                   varchar(50) NOT NULL,
                             item_id              varchar(50) NOT NULL,
                             sale_price           number not null,
                             material             varchar(255),
                             description          varchar(1000),
                             notice               varchar(1000),
                             create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                             update_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                             PRIMARY KEY (id),
                             CONSTRAINT fk_item_display_to_item_master FOREIGN KEY (item_id) REFERENCES item_master (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS item_option CASCADE;
CREATE TABLE item_option (
                           id                   varchar(50) NOT NULL,
                           size                 varchar(10) DEFAULT 'FREE',
                           color                varchar(10) DEFAULT 'ONE COLOR',
                           master_id            varchar(50) NOT NULL,
                           create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                           update_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                           PRIMARY KEY (id),
                           CONSTRAINT fk_item_option_to_item_master FOREIGN KEY (master_id) REFERENCES item_master (id) ON DELETE CASCADE ON UPDATE RESTRICT
);
