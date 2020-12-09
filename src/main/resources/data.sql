INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('246fa96f9b634a56aaac5884de186ebc', 'C', 'cate_first', '메인카테고리' );
INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('36f651a982274a5b95dac3e9d85b0d1a', 'C100','Outer', '아우터' );
INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('3ebebfeb9fbe4ecfa5935f96ed308854', 'C400','Knitwear', '니트류' );

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
INSERT INTO item_master (id, item_name, category_id, price, thumbnail) VALUES ('c62bb955f4f94203b31f157fa72deef2', '그랜트 핀턱 팬츠 (2color)', '8afe84e9d3f948cf83bb1faed0175c63', 43000, 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/itemMaster/5bd7201aab3b952aeae3d0f38305ad45.jpg');
INSERT INTO item_display (id, item_id, sale_price, material, size, description, notice, status, detail_image) VALUES ('f23ba30a47194a2c8a3fd2ccadd952a4', 'c62bb955f4f94203b31f157fa72deef2', 43000, '기모면(오랜 시간 원형 보존을 위해 드라이클리닝을 추천해 드립니다.) ',
'S(26-27) 허리 33 엉덩이 49.5 허벅지 31.5 밑위 29.5 밑단 20 총길이 92
M(28-29) 허리 35 엉덩이 50.5 허벅지 32.5 밑위 29.5 밑단 21 총길이 92',
'데일리하면서도 멋스러운 핏감의 핀턱 팬츠입니다.
부드러운 터칭감이 느껴지는 톡톡한 기모 원단으로
지금부터 겨울까지 착용하기 좋은 적당한 두께감입니다.
이중 잠금으로 히든 버튼으로 한번, 
사이드의 무늬 버튼으로 한번 클로징 가능합니다.
일반적인 후크 클로징과 차별화된
사이드의 버튼 디테일은 은은하면서 포인트가 됩니다.
과하게 오버하지 않으면서도 적당히 여유롭게 떨어지는 핏감으로
핀턱 라인이 적당히 볼륨감 있는 아웃라인을 형성해줍니다.
데일리로 착용하기 좋은 부드럽고 은은한 빛의 크림 색상과
안개가 낀 듯 오묘한 색감의 차분한 포그그레이 색상준비했습니다.
COLOR
크림-부드럽고 은은한 빛의 화사한 크림
포그그레이-안개가 낀듯 오묘한 색감의 차분하고 딥한 그레이
INFO 
신축성-없음｜계절감-가을/겨울｜비침-없음｜촉감-부드러운 편', '크림 S사이즈 상품 준비 기간이 일주일 가량 소요됩니다.', 0 ,
'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/itemDisplay/2385b9ac472e4e2d835c59ce11379512.jpeg');
INSERT INTO item_option (id, color, master_id) VALUES ('fb32787a91614b978cb94b0d47d7c676', '콘베이지', '8c1cbb792b8d447e9128d53920cf9366');
INSERT INTO item_option (id, color, master_id) VALUES ('af412e9968dc4f11a83f9352a251f0aa', '크림아이보리', '8c1cbb792b8d447e9128d53920cf9366');