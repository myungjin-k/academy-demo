INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('246fa96f9b634a56aaac5884de186ebc', 'C', 'cate_first', '메인카테고리' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('3ebebfeb9fbe4ecfa5935f96ed308854', 'C400', 'Knitwear', '니트류', '246fa96f9b634a56aaac5884de186ebc' );

INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('3ebebfeb9fbe4ecfa5935f96ed308854', 'C400','Knitwear', '니트류' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('44e94265588b428e8e01bbc23dfc0f7e', 'C400100', 'Knit', '니트', '3ebebfeb9fbe4ecfa5935f96ed308854' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('0cba0f39833e443598f3a07d4b36dca9', 'C400200', 'Cardigan', '가디건', '3ebebfeb9fbe4ecfa5935f96ed308854' );


INSERT INTO item_master (id, item_name, category_id, price, thumbnail) VALUES ('44e94265588b428e8e01bbc23dfc0f7e', '[R택] 보더 알파카 니트', '44e94265588b428e8e01bbc23dfc0f7e', 72000, 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/itemMaster/e58bfe723dbb7e2c927887606aaf8ec7.jpg');