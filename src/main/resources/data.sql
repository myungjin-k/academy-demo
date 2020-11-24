INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('246fa96f9b634a56aaac5884de186ebc', 'C', 'cate_first', '메인카테고리' );
INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('0a08e9fc691642ee9c270d056ec28e6d', 'C100', 'outer', '아우터' );
INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('19a8a63adaf8449489058cda44df35f3', 'C200', 'top', '상의' );
INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('fa52f7959c2647f3a902980c92ea9789', 'C300', 'bottom', '하의' );
INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('a190c0438d8d421d99efa24e11cf27bb', 'C400', 'shoes', '신발' );
INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('8739c42e49644dd988a6c63a9fa2bb85', 'C500', 'accessories', '악세서리' );


INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('36f651a982274a5b95dac3e9d85b0d1a', 'C100', 'outer', '아우터', '246fa96f9b634a56aaac5884de186ebc' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('5105a5a02b754b4f9975975c1f1f58ea', 'C200', 'top', '상의', '246fa96f9b634a56aaac5884de186ebc' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('0b304ee35d124ce49f0266214dee9f3d', 'C300', 'bottom', '하의', '246fa96f9b634a56aaac5884de186ebc' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('3ebebfeb9fbe4ecfa5935f96ed308854', 'C400', 'shoes', '신발', '246fa96f9b634a56aaac5884de186ebc' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('8afe84e9d3f948cf83bb1faed0175c63', 'C500', 'accessories', '악세서리', '246fa96f9b634a56aaac5884de186ebc' );

INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('a77275df422a439f97b22e95d7223260', 'C100100', 'coat', '코트', '0a08e9fc691642ee9c270d056ec28e6d' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('88b09a7b0a7d4390a67ab3adf1d6d378', 'C100200', 'jumper', '점퍼', '0a08e9fc691642ee9c270d056ec28e6d' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('26020f22b15d4a7f9d78c2b7fc19f832', 'C100300', 'jacket', '자켓', '0a08e9fc691642ee9c270d056ec28e6d' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('14c39ed0110f451e8b37c9f400946039', 'C100400', 'cardigan', '가디건', '0a08e9fc691642ee9c270d056ec28e6d' );

INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('73fad711fbc548be85c10c9e0e8b0f1c', 'C200100', 't-shirts', '티셔츠', '19a8a63adaf8449489058cda44df35f3' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('a06e93c2fbec4e82a7c6a0c1e31a6025', 'C200100', 'shirts', '셔츠·블라우스', '19a8a63adaf8449489058cda44df35f3' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('02049afe906d4be595aeb782c11a780e', 'C200200', 'knit', '니트', '19a8a63adaf8449489058cda44df35f3' );

INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('b4c6320a278040daa407eb57b3775253', 'C300100', 'pants', '바지', '19a8a63adaf8449489058cda44df35f3' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('fa9c27d308d7448e84665a7ee06e7544', 'C300200', 'skirts', '치마', '19a8a63adaf8449489058cda44df35f3' );

INSERT INTO member (id, user_id, password, name, tel, addr1, addr2) VALUES ('3a18e633a5db4dbd8aaee218fe447fa4', 'mjkim', '$2a$10$tp2h2O2jG62pITCh3dhvwuAANA0Uk/TAh.UgTFS0LJaXqydqKm7jm', '명진','010-1234-5678','XX시 XX구 XX로', '1-1111');
