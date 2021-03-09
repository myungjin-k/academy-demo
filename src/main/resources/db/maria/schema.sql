SET FOREIGN_KEY_CHECKS=0;
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
                             CONSTRAINT fk_common_code_to_code_group FOREIGN KEY (group_id) REFERENCES code_group (id) ON DELETE CASCADE ON UPDATE CASCADE
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
                        order_amount    int default 0,
                        reserves        int DEFAULT 0,
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
                             price                int DEFAULT 0,
                             thumbnail            varchar(255),
                             create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                             update_at            datetime DEFAULT null,
                             PRIMARY KEY (id),
                             CONSTRAINT unq_item_name UNIQUE (item_name),
                             CONSTRAINT fk_item_master_to_common_code FOREIGN KEY (category_id) REFERENCES COMMON_CODE (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS item_display CASCADE;
CREATE TABLE item_display (
                              id                   varchar(50) NOT NULL,
                              item_id              varchar(50) NOT NULL,
                              item_display_name    varchar(50) NOT NULL,
                              sale_price           int not null,
                              material             varchar(255),
                              size                 varchar(255),
                              description          varchar(2000),
                              notice               varchar(1000),
                              detail_image         varchar(255),
                              status               int DEFAULT 0,
                              create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                              update_at            datetime DEFAULT null,
                              PRIMARY KEY (id),
                              CONSTRAINT fk_item_display_to_item_master FOREIGN KEY (item_id) REFERENCES item_master (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS item_display_option CASCADE;
CREATE TABLE item_display_option (
                                     id                   varchar(50) NOT NULL,
                                     size                 varchar(10) DEFAULT 'ONE SIZE',
                                     color                varchar(10) DEFAULT 'ONE COLOR',
                                     display_id           varchar(50) NOT NULL,
                                     status               int DEFAULT 0,
                                     create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                                     update_at            datetime DEFAULT null,
                                     PRIMARY KEY (id),
                                     CONSTRAINT fk_item_display_option_to_item_display FOREIGN KEY (display_id) REFERENCES item_display (id) ON DELETE CASCADE ON UPDATE CASCADE
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
                             CONSTRAINT fk_item_option_to_item_master FOREIGN KEY (master_id) REFERENCES item_master (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS cart CASCADE;
CREATE TABLE cart (
                      id                   varchar(50) NOT NULL,
                      member_id            varchar(50),
                      item_id              varchar(50)  NOT NULL,
                      count                int not null,
                      create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                      update_at            datetime DEFAULT null,
                      PRIMARY KEY (id, member_id),
                      CONSTRAINT fk_cart_to_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE ON UPDATE CASCADE,
                      CONSTRAINT fk_cart_to_item_display_option FOREIGN KEY (item_id) REFERENCES item_display_option (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS order_master CASCADE;
CREATE TABLE order_master (
                              id                   varchar(50) NOT NULL,
                              abbr_items_name      varchar(255),
                              member_id            varchar(50),
                              total_amount         int not null,
                              point_used           int default 0,
                              order_name           varchar(50) NOT NULL,
                              order_tel            varchar(50) NOT NULL,
                              order_email          varchar(50),
                              order_addr1          varchar(255),
                              order_addr2          varchar(255),
                              payment_uid          varchar(50),
                              is_cancelled         boolean default false,
                              create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                              update_at            datetime DEFAULT null,
                              PRIMARY KEY (id),
                              CONSTRAINT fk_order_to_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS delivery CASCADE;
CREATE TABLE delivery (
                          id                   varchar(50) NOT NULL,
                          order_id             varchar(50) NOT NULL,
                          receiver_name        varchar(50) NOT NULL,
                          receiver_tel         varchar(50) NOT NULL,
                          receiver_addr1       varchar(255),
                          receiver_addr2       varchar(255),
                          message              varchar(255),
                          status               int NOT NULL,
                          invoice_num          varchar(50),
                          ext_delivery_id      varchar(50),
                          create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                          update_at            datetime DEFAULT null,
                          PRIMARY KEY (id),
                          CONSTRAINT unq_ext_delivery_id UNIQUE (ext_delivery_id),
                          CONSTRAINT fk_delivery_to_order FOREIGN KEY (order_id) REFERENCES order_master (id) ON DELETE CASCADE ON UPDATE CASCADE
);


DROP TABLE IF EXISTS delivery_item CASCADE;
CREATE TABLE delivery_item (
                               id                   varchar(50) NOT NULL,
                               delivery_id          varchar(50) NOT NULL,
                               item_id              varchar(50) NOT NULL,
                               count                int not null,
                               create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                               update_at            datetime DEFAULT null,
                               PRIMARY KEY (id),
                               CONSTRAINT fk_delivery_item_to_delivery FOREIGN KEY (delivery_id) REFERENCES delivery (id) ON DELETE CASCADE ON UPDATE CASCADE,
                               CONSTRAINT fk_delivery_item_to_item_display_option FOREIGN KEY (item_id) REFERENCES item_display_option (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS order_item CASCADE;
CREATE TABLE order_item (
                            id                   varchar(50) NOT NULL,
                            order_id             varchar(50) NOT NULL,
                            item_id              varchar(50) NOT NULL,
                            delivery_item_id     varchar(50),
                            count                int not null,
                            create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                            update_at            datetime DEFAULT null,
                            PRIMARY KEY (id),
                            CONSTRAINT fk_order_item_to_order FOREIGN KEY (order_id) REFERENCES order_master (id) ON DELETE CASCADE ON UPDATE CASCADE,
                            CONSTRAINT fk_order_item_to_item_display_option FOREIGN KEY (item_id) REFERENCES item_display_option (id) ON DELETE CASCADE ON UPDATE CASCADE,
                            CONSTRAINT fk_order_item_to_delivery_item FOREIGN KEY (delivery_item_id) REFERENCES delivery_item (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS review CASCADE;
CREATE TABLE review (
                        id                   varchar(50) NOT NULL,
                        member_id            varchar(50) NOT NULL,
                        item_id              varchar(50) NOT NULL,
                        order_item_id        varchar(50) NOT NULL,
                        score                int default 5,
                        content              varchar(2000) NOT NULL,
                        review_img           varchar(255),
                        status               int default 1,
                        reserves_paid        boolean NOT NULL DEFAULT false,
                        create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                        update_at            datetime DEFAULT null,
                        PRIMARY KEY (id),
                        CONSTRAINT fk_review_to_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE ON UPDATE CASCADE,
                        CONSTRAINT fk_review_to_item_display FOREIGN KEY (item_id) REFERENCES item_display (id) ON DELETE CASCADE ON UPDATE CASCADE,
                        CONSTRAINT fk_review_to_order_item FOREIGN KEY (order_item_id) REFERENCES order_item (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS review_comment CASCADE;
CREATE TABLE review_comment (
                                id                   varchar(50) NOT NULL,
                                admin_id             varchar(50) NOT NULL,
                                review_id            varchar(50) NOT NULL,
                                content              varchar(2000) NOT NULL,
                                create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                                update_at            datetime DEFAULT null,
                                PRIMARY KEY (id),
                                CONSTRAINT fk_review_comment_to_admin FOREIGN KEY (admin_id) REFERENCES admin (id) ON DELETE CASCADE ON UPDATE CASCADE,
                                CONSTRAINT fk_review_comment_to_item_display FOREIGN KEY (review_id) REFERENCES review (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS received_delivery_status CASCADE;
CREATE TABLE received_delivery_status (
                                          id                   varchar(50) NOT NULL,
                                          ext_delivery_id      varchar(50) NOT NULL,
                                          seq                  int NOT NULL,
                                          status               int NOT NULL,
                                          apply_yn             char default 'N',
                                          create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                                          update_at            datetime DEFAULT null,
                                          PRIMARY KEY (ext_delivery_id, seq),
                                          CONSTRAINT fk_received_delivery_status_to_delivery FOREIGN KEY (ext_delivery_id) REFERENCES delivery (ext_delivery_id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS top_seller CASCADE;
CREATE TABLE top_seller (
                            id                   varchar(50) NOT NULL,
                            item_id              varchar(50) NOT NULL,
                            create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                            update_at            datetime DEFAULT null,
                            PRIMARY KEY (id),
                            CONSTRAINT fk_top_seller_to_item_display FOREIGN KEY (item_id) REFERENCES item_display (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS reserves_history CASCADE;
CREATE TABLE reserves_history (
                                  id                   varchar(50) NOT NULL,
                                  member_id            varchar(50) NOT NULL,
                                  amount               int not null,
                                  type                 varchar(50) NOT NULL,
                                  ref_id               varchar(50),
                                  create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                                  update_at            datetime DEFAULT null,
                                  PRIMARY KEY (id),
                                  CONSTRAINT fk_reserves_history_to_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS event CASCADE;
CREATE TABLE event (
                       seq                  int auto_increment,
                       name                 varchar(255) not null,
                       type                 varchar(10) not null,
                       status               int default 0,
                       discount_ratio       int default 0,
                       discount_amount      int default 0,
                       start_at             datetime DEFAULT CURRENT_TIMESTAMP(),
                       end_at               datetime DEFAULT CURRENT_TIMESTAMP(),
                       create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                       update_at            datetime DEFAULT null,
                       PRIMARY KEY (seq)
);


DROP TABLE IF EXISTS event_item CASCADE;
CREATE TABLE event_item (
                            id                   varchar(50) not null,
                            item_id              varchar(50) not null,
                            event_seq            int not null,
                            create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                            update_at            datetime DEFAULT null,
                            PRIMARY KEY (id),
                            CONSTRAINT fk_event_item_to_item_display FOREIGN KEY (item_id) REFERENCES item_display (id) ON DELETE CASCADE ON UPDATE CASCADE,
                            CONSTRAINT fk_event_item_to_event FOREIGN KEY (event_seq) REFERENCES event (seq) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS item_display_price_history CASCADE;
CREATE TABLE item_display_price_history (
                                            id                   varchar(50) not null,
                                            item_id              varchar(50) not null,
                                            seq                  int not null,
                                            sale_price           int not null,
                                            ref                  varchar(255),
                                            create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                                            update_at            datetime DEFAULT null,
                                            PRIMARY KEY (id),
                                            CONSTRAINT fk_item_display_price_history_to_item_display FOREIGN KEY (item_id) REFERENCES item_display (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS coupon CASCADE;
CREATE TABLE coupon (
                        id                   varchar(50) not null,
                        event_seq            int not null,
                        member_id            varchar(50) not null,
                        used_yn              char default 'N',
                        expired_yn           char default 'N',
                        create_at            datetime DEFAULT CURRENT_TIMESTAMP(),
                        update_at            datetime DEFAULT null,
                        PRIMARY KEY (id),
                        CONSTRAINT coupon_to_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE ON UPDATE CASCADE,
                        CONSTRAINT coupon_to_event FOREIGN KEY (event_seq) REFERENCES event (seq) ON DELETE CASCADE ON UPDATE CASCADE
);

SET FOREIGN_KEY_CHECKS=1;