INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('246fa96f9b634a56aaac5884de186ebc', 'C', 'cate_first', '메인카테고리' );
INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('36f651a982274a5b95dac3e9d85b0d1a', 'C100','Outer', '아우터' );

INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('36f651a982274a5b95dac3e9d85b0d1a', 'C100', 'Outer', '아우터', '246fa96f9b634a56aaac5884de186ebc' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('5105a5a02b754b4f9975975c1f1f58ea', 'C200', 'Tops', '상의', '246fa96f9b634a56aaac5884de186ebc' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('0b304ee35d124ce49f0266214dee9f3d', 'C300', 'Shirts,Blouse', '셔츠,블라우스', '246fa96f9b634a56aaac5884de186ebc' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('3ebebfeb9fbe4ecfa5935f96ed308854', 'C400', 'Knitwear', '니트류', '246fa96f9b634a56aaac5884de186ebc' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('8afe84e9d3f948cf83bb1faed0175c63', 'C500', 'Pants', '바지', '246fa96f9b634a56aaac5884de186ebc' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('a77275df422a439f97b22e95d7223260', 'C600', 'Skirts,Dress', '치마,원피스', '246fa96f9b634a56aaac5884de186ebc' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('88b09a7b0a7d4390a67ab3adf1d6d378', 'C700', 'Acc', '악세서리', '246fa96f9b634a56aaac5884de186ebc' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('26020f22b15d4a7f9d78c2b7fc19f832', 'C800', 'Bag,Shoes', '잡화', '246fa96f9b634a56aaac5884de186ebc' );


INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('979143570fc54210a83655db48a923dd', 'C100100', 'Coat', '코트', '36f651a982274a5b95dac3e9d85b0d1a' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('58735a53d92c40d6a5f440a5d8e0aa5d', 'C100200', 'Jacket', '자켓', '36f651a982274a5b95dac3e9d85b0d1a' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('bea264233aee482c8808a116d1bfd321', 'C100300', 'Jumper', '점퍼', '36f651a982274a5b95dac3e9d85b0d1a' );


INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('44e94265588b428e8e01bbc23dfc0f7e', 'C400100', 'Knit', '니트', '3ebebfeb9fbe4ecfa5935f96ed308854' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('0cba0f39833e443598f3a07d4b36dca9', 'C400200', 'Cardigan', '가디건', '3ebebfeb9fbe4ecfa5935f96ed308854' );

INSERT INTO member (id, user_id, password, name, email, tel, addr1, addr2) VALUES ('3a18e633a5db4dbd8aaee218fe447fa4', 'mjkim', '$2a$10$DDspWXEeR0OuJio7QfKQJukEC55JyHwkoUl/j2Zn64XLhuXoxNkKq', '명진', 'open7894.v2@gmail.com', '010-1234-5678','XX시 XX구 XX로', '1-1111');

INSERT INTO admin (id, admin_id, password) VALUES ('3a18e633a5db4dbd8aaee218fe447fa4', 'mjkim', '$2a$10$DDspWXEeR0OuJio7QfKQJukEC55JyHwkoUl/j2Zn64XLhuXoxNkKq');


INSERT INTO item_master (id, item_name, category_id, price, thumbnail) VALUES ('8c1cbb792b8d447e9128d53920cf9366', '보더 알파카 니트', '44e94265588b428e8e01bbc23dfc0f7e', 72000, 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/itemMaster/e58bfe723dbb7e2c927887606aaf8ec7.jpg');
INSERT INTO item_display (id, item_id, sale_price, material, description, notice, status) VALUES ('f0abb4e0bb1e4a0caa6dc8ace5eb29ca', '8c1cbb792b8d447e9128d53920cf9366', 72000*0.8, '알파카', '예뻐유', '배송지연', 1 );
INSERT INTO item_option (id, color, master_id) VALUES ('fb32787a91614b978cb94b0d47d7c676', '콘베이지', '8c1cbb792b8d447e9128d53920cf9366');
INSERT INTO item_option (id, color, master_id) VALUES ('af412e9968dc4f11a83f9352a251f0aa', '크림아이보리', '8c1cbb792b8d447e9128d53920cf9366');