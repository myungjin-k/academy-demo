INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('246fa96f9b634a56aaac5884de186ebc', 'C', 'cate_first', '메인카테고리' );

INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('0a08e9fc691642ee9c270d056ec28e6d', 'C100', 'side_dish', '밑반찬' );

INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('19a8a63adaf8449489058cda44df35f3', 'C200', 'soup', '국·찌개' );
INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('fa52f7959c2647f3a902980c92ea9789', 'C300', 'main_dish', '메인반찬' );
INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('a190c0438d8d421d99efa24e11cf27bb', 'C400', 'fresh', '신선·가공' );
INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('8739c42e49644dd988a6c63a9fa2bb85', 'C500', 'snack', '간식' );



INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('a77275df422a439f97b22e95d7223260', 'C100100', 'seasoning', '무침', '0a08e9fc691642ee9c270d056ec28e6d' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('88b09a7b0a7d4390a67ab3adf1d6d378', 'C100200', 'stir_fried', '볶음', '0a08e9fc691642ee9c270d056ec28e6d' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('26020f22b15d4a7f9d78c2b7fc19f832', 'C100300', 'fried', '튀김', '0a08e9fc691642ee9c270d056ec28e6d' );

INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('a06e93c2fbec4e82a7c6a0c1e31a6025', 'C200100', 'soup', '탕', '19a8a63adaf8449489058cda44df35f3' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('02049afe906d4be595aeb782c11a780e', 'C200200', 'stew', '찌개', '19a8a63adaf8449489058cda44df35f3' );
