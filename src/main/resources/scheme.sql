DROP TABLE IF EXISTS code_group CASCADE;
CREATE TABLE code_group (
                            id           varchar(50) NOT NULL,
                            code         varchar(10) NOT NULL,
                            name_eng     varchar(50) NOT NULL,
                            name_kor     varchar(50) NOT NULL,
                            create_at    datetime DEFAULT CURRENT_TIMESTAMP(),
                            update_at    datetime DEFAULT null,
                            PRIMARY KEY (id),
                            CONSTRAINT unq_code_group_code UNIQUE (code)
);

DROP TABLE IF EXISTS common_code CASCADE;
CREATE TABLE common_code (
                             id              varchar(50) NOT NULL,
                             code            varchar(10) NOT NULL,
                             name_eng        varchar(50) NOT NULL,
                             name_kor        varchar(50) NOT NULL,
                             group_id        varchar(50) NOT NULL,
                             create_at       datetime DEFAULT CURRENT_TIMESTAMP(),
                             update_at       datetime DEFAULT null,
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
                             update_at       datetime DEFAULT null,
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
                        update_at       datetime DEFAULT null,
                        PRIMARY KEY (id),
                        CONSTRAINT unq_admin_id UNIQUE (admin_id)
);



DROP TABLE IF EXISTS item_master CASCADE;
CREATE TABLE item_master (
                       id                   varchar(50) NOT NULL,
                       item_name            varchar(50) NOT NULL,
                       category_id          varchar(255) NOT NULL,
                       price                number DEFAULT 0,
                       thumbnail            varchar(255),
                       create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                       update_at            datetime DEFAULT null,
                       PRIMARY KEY (id),
                       CONSTRAINT unq_item_name UNIQUE (item_name),
                       CONSTRAINT fk_item_master_to_common_code FOREIGN KEY (category_id) REFERENCES COMMON_CODE (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS item_display CASCADE;
CREATE TABLE item_display (
                             id                   varchar(50) NOT NULL,
                             item_id              varchar(50) NOT NULL,
                             item_display_name    varchar(50) NOT NULL,
                             sale_price           number not null,
                             material             varchar(255),
                             size                 varchar(255),
                             description          varchar(2000),
                             notice               varchar(1000),
                             detail_image         varchar(255),
                             status               number DEFAULT 0,
                             create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                             update_at            datetime DEFAULT null,
                             PRIMARY KEY (id),
                             CONSTRAINT fk_item_display_to_item_master FOREIGN KEY (item_id) REFERENCES item_master (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS item_display_option CASCADE;
CREATE TABLE item_display_option (
                                     id                   varchar(50) NOT NULL,
                                     size                 varchar(10) DEFAULT 'ONE SIZE',
                                     color                varchar(10) DEFAULT 'ONE COLOR',
                                     display_id           varchar(50) NOT NULL,
                                     status               number DEFAULT 0,
                                     create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                                     update_at            datetime DEFAULT null,
                                     PRIMARY KEY (id),
                                     CONSTRAINT fk_item_display_option_to_item_display FOREIGN KEY (display_id) REFERENCES item_display (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS item_option CASCADE;
CREATE TABLE item_option (
                           id                   varchar(50) NOT NULL,
                           size                 varchar(10) DEFAULT 'ONE SIZE',
                           color                varchar(10) DEFAULT 'ONE COLOR',
                           master_id            varchar(50) NOT NULL,
                           create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                           update_at            datetime DEFAULT null,
                           PRIMARY KEY (id),
                           CONSTRAINT fk_item_option_to_item_master FOREIGN KEY (master_id) REFERENCES item_master (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS cart CASCADE;
CREATE TABLE cart (
                             id                   varchar(50) NOT NULL,
                             member_id            varchar(50),
                             item_id              varchar(50)  NOT NULL,
                             create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                             update_at            datetime DEFAULT null,
                             PRIMARY KEY (id, member_id),
                             CONSTRAINT fk_cart_to_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE ON UPDATE RESTRICT,
                             CONSTRAINT fk_cart_to_item_display_option FOREIGN KEY (item_id) REFERENCES item_display_option (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS order_master CASCADE;
CREATE TABLE order_master (
                      id                   varchar(50) NOT NULL,
                      abbr_items_name      varchar(255),
                      member_id            varchar(50),
                      total_amount         number not null,
                      order_name           varchar(50) NOT NULL,
                      order_tel            varchar(50) NOT NULL,
                      order_addr1          varchar(255),
                      order_addr2          varchar(255),
                      create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                      update_at            datetime DEFAULT null,
                      PRIMARY KEY (id),
                      CONSTRAINT fk_order_to_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS order_item CASCADE;
CREATE TABLE order_item (
                         id                   varchar(50) NOT NULL,
                         order_id             varchar(50) NOT NULL,
                         item_id              varchar(50) NOT NULL,
                         create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                         update_at            datetime DEFAULT null,
                         PRIMARY KEY (id),
                         CONSTRAINT fk_order_item_to_order FOREIGN KEY (order_id) REFERENCES order_master (id) ON DELETE CASCADE ON UPDATE RESTRICT,
                         CONSTRAINT fk_order_item_to_item_display_option FOREIGN KEY (item_id) REFERENCES item_display_option (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS delivery CASCADE;
CREATE TABLE delivery (
                         id                   varchar(50) NOT NULL,
                         order_id             varchar(50),
                         receiver_name        varchar(50) NOT NULL,
                         receiver_tel         varchar(50) NOT NULL,
                         receiver_addr1       varchar(255),
                         receiver_addr2       varchar(255),
                         message              varchar(255),
                         status               number NOT NULL,
                         invoice_num          number,
                         create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                         update_at            datetime DEFAULT null,
                         PRIMARY KEY (id),
                         CONSTRAINT fk_delivery_to_order FOREIGN KEY (order_id) REFERENCES order_master (id) ON DELETE CASCADE ON UPDATE RESTRICT
);