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

INSERT INTO member (id, user_id, password, name, email, tel, addr1, addr2, reserves, order_amount) VALUES ('3a18e633a5db4dbd8aaee218fe447fa4', 'mjkim', '$2a$10$DDspWXEeR0OuJio7QfKQJukEC55JyHwkoUl/j2Zn64XLhuXoxNkKq', '명진', 'open7894.v2@gmail.com', '010-1234-5678','XX시 XX구 XX로', '1-1111', 3000, 50000);

INSERT INTO admin (id, admin_id, password) VALUES ('3a18e633a5db4dbd8aaee218fe447fa4', 'mjkim', '$2a$10$DDspWXEeR0OuJio7QfKQJukEC55JyHwkoUl/j2Zn64XLhuXoxNkKq');

INSERT INTO item_master (id, item_name, category_id, price, thumbnail) VALUES ('8c1cbb792b8d447e9128d53920cf9366', '[R택] 보더 알파카 니트', '44e94265588b428e8e01bbc23dfc0f7e', 72000, 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/itemMaster/e58bfe723dbb7e2c927887606aaf8ec7.jpg');

INSERT INTO item_option (id, color, master_id) VALUES ('b870e35c135e4782b531299753643fba', '콘베이지', '8c1cbb792b8d447e9128d53920cf9366');
INSERT INTO item_option (id, color, master_id) VALUES ('bed5b40336c844f1a78c431f111370fa', '크림아이보리', '8c1cbb792b8d447e9128d53920cf9366');

INSERT INTO item_master (id, item_name, category_id, price, thumbnail) VALUES ('c62bb955f4f94203b31f157fa72deef2', '[R택] 그랜트 핀턱 팬츠 (2color)', '8afe84e9d3f948cf83bb1faed0175c63', 43000, 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/itemMaster/5bd7201aab3b952aeae3d0f38305ad45.jpg');

INSERT INTO item_option (id, color, size, master_id) VALUES ('fb32787a91614b978cb94b0d47d7c676', '크림', 'S', 'c62bb955f4f94203b31f157fa72deef2');
INSERT INTO item_option (id, color, size, master_id) VALUES ('fb32787b91614b978cb94b0d47d7c676', '크림', 'M', 'c62bb955f4f94203b31f157fa72deef2');
INSERT INTO item_option (id, color, size, master_id) VALUES ('af412e9968dc4f11a83f9352a251f0aa', '포그그레이', 'S', 'c62bb955f4f94203b31f157fa72deef2');
INSERT INTO item_option (id, color, size, master_id) VALUES ('af412e9968ec4f11a83f9352a251f0aa', '포그그레이', 'M', 'c62bb955f4f94203b31f157fa72deef2');

INSERT INTO item_display (id, item_id, item_display_name, sale_price, material, size, description, notice, status, detail_image) VALUES ('f23ba30a47194a2c8a3fd2ccadd952a4', 'c62bb955f4f94203b31f157fa72deef2', '그랜드 핀턱 팬츠 (2color)', 43000, '기모면(오랜 시간 원형 보존을 위해 드라이클리닝을 추천해 드립니다.) ',
 'S(26-27) 허리 33 엉덩이 49.5 허벅지 31.5 밑위 29.5 밑단 20 총길이 92
 M(28-29) 허리 35 엉덩이 50.5 허벅지 32.5 밑위 29.5 밑단 21 총길이 92',
 '데일리하면서도 멋스러운 핏감의 핀턱 팬츠입니다.
 부드러운 터칭감이 느껴지는 톡톡한 기모 원단으로
 지금부터 겨울까지 착용하기 좋은 적당한 두께감입니다.
 이중 잠금으로 히든 버튼으로 한번,
 사이드의 무늬 버튼으로 한번 클로징 가능합니다.
 사이드의 버튼 디테일은 은은하면서 포인트가 됩니다.
 과하게 오버하지 않으면서도 적당히 여유롭게 떨어지는 핏감으로
 핀턱 라인이 적당히 볼륨감 있는 아웃라인을 형성해줍니다.
 데일리로 착용하기 좋은 부드럽고 은은한 빛의 크림 색상과
 안개가 낀 듯 오묘한 색감의 차분한 포그그레이 색상준비했습니다.
 크림-부드럽고 은은한 빛의 화사한 크림
 포그그레이-안개가 낀듯 오묘한 색감의 차분하고 딥한 그레이
 신축성-없음｜계절감-가을/겨울｜비침-없음｜촉감-부드러운 편', '크림 S사이즈 상품 준비 기간이 일주일 가량 소요됩니다.', 1 ,
 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/itemDisplay/2385b9ac472e4e2d835c59ce11379512.jpeg');

INSERT INTO item_display_option (id, color, size, display_id) VALUES ('91cc1c18f11e4d018566524b51d8419a', '크림', 'S', 'f23ba30a47194a2c8a3fd2ccadd952a4');
INSERT INTO item_display_option (id, color, size, display_id) VALUES ('91cc1c18f11e5d018566524b51d8419a', '크림', 'M', 'f23ba30a47194a2c8a3fd2ccadd952a4');
INSERT INTO item_display_option (id, color, size, display_id) VALUES ('c9402883dbe540e898a417e4884845bf', '포그그레이', 'S', 'f23ba30a47194a2c8a3fd2ccadd952a4');
INSERT INTO item_display_option (id, color, size, display_id) VALUES ('c9402883dbe540e898a417e4984845bf', '포그그레이', 'M', 'f23ba30a47194a2c8a3fd2ccadd952a4');

INSERT INTO item_master (id, item_name, category_id, price, thumbnail)
VALUES ('6b5cd6e21ffa4bb08a75e270c18e8e05', '[N택] 시티 하이넥 코트 (2color)', '36f651a982274a5b95dac3e9d85b0d1a', 190000, 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/itemMaster/cb0b8f3a76e06326d54e2f5231b1c317.jpg');

INSERT INTO item_display (id, item_id, item_display_name, sale_price, material, size, description, status, detail_image) VALUES
('6bdbf6eea40b425caae4410895ca4809', '6b5cd6e21ffa4bb08a75e270c18e8e05', '시티 하이넥 코트 (2color)', 190000, '울30 나일론70',
 'one size
어깨 - 가슴 60 암홀 38.5 팔통 21 소매단 18.5 팔길이 72 총길이(넥제외) 114.5 넥길이 7.5',
 '코트 하나만으로 멋스러운 코디가 완성되는 하이넥 코트입니다.
 깔끔하면서도 포인트 되는 디테일이 매력적입니다.

 오픈클로징 디테일이 생략된 형태로
 코트깃을 여며 자연스럽게 흘러내리듯 착용하거나
 넥라인의 미니멀한 단추를 채워 하이넥으로 연출하시면 좋습니다.

 벨트를 이용해 로브 형식으로 묶어 착용해도 한층 멋스럽게 즐기실 수 있습니다.

 159 모델키 기준 종아리를 반정도 덮는 여유로운 기장감으로
 내추럴하게 흐르듯 착용되는 아웃핏이 멋스럽습니다.

 비교적 여유로운 품과 암홀라인으로
 이너로 도톰한 두께감의 니트와 함께 착용했을 때도
 끼임없이 편안하게 착용 가능합니다.

 데일리하게 활용하기 좋은 차분하고 은은한 색감의 진한 차콜그레이와
 고급스러운 색감의 진한 브라운 두가지 색상 준비했습니다.

 *소재 특성상 잡실/냄새/털빠짐/까끌거림이 있을 수 있습니다.

 COLOR
 딥브라운- 고급스러운 색감의 진한 브라운
 차콜그레이-차분하고 은은한 색감의 진한 그레이

 INFO
 신축성-없음｜두께감-적당함｜안감-있음｜무게감-약간 있음',
 1,
 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/itemDisplay/921574572f8f440caaa4d55e8c98a4ef.jpeg'
);

INSERT INTO item_display_option (id, color, display_id) VALUES ('8130cede06c04fa2bbd8bc09a29787c8', '딥브라운', '6bdbf6eea40b425caae4410895ca4809');
INSERT INTO item_display_option (id, color, display_id) VALUES ('fd778631e13944f393a73abf9e5dc5cf', '차콜그레이', '6bdbf6eea40b425caae4410895ca4809');

INSERT INTO cart (id, member_id, item_id, count) VALUES ('f4597dfc1ae649a58edcb7921002aca5', '3a18e633a5db4dbd8aaee218fe447fa4', 'c9402883dbe540e898a417e4884845bf', 1);
INSERT INTO cart (id, member_id, item_id, count) VALUES ('0a25d9eea6d94a3897e06b33e4bf5b69', '3a18e633a5db4dbd8aaee218fe447fa4', '91cc1c18f11e4d018566524b51d8419a', 1);

INSERT INTO order_master (id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2) VALUES ('03039b4535404247bfee52cfd934c779', '그랜드 핀턱 팬츠 (2color) 外 1건', '3a18e633a5db4dbd8aaee218fe447fa4', 43000 * 2 + 190000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111');
INSERT INTO order_item (id, ORDER_ID, item_id, count) VALUES ('973ee36b953a4a0c9ca5d9f5868b015c', '03039b4535404247bfee52cfd934c779', 'c9402883dbe540e898a417e4884845bf', 2);
INSERT INTO order_item (id, ORDER_ID, item_id, count) VALUES ('be9089f9bf1f4e59b712e3187b38a0d7', '03039b4535404247bfee52cfd934c779', '8130cede06c04fa2bbd8bc09a29787c8', 1);

INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2) VALUES ('cd2940ee2dfc418384eedc450be832a2', '03039b4535404247bfee52cfd934c779', 1, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111');
INSERT INTO delivery_item (id, delivery_id, item_id, count) VALUES ('d14b36612cd047a0b1e4e71d993dc9b2', 'cd2940ee2dfc418384eedc450be832a2', 'c9402883dbe540e898a417e4884845bf', 2);
UPDATE order_item SET delivery_item_id = 'd14b36612cd047a0b1e4e71d993dc9b2' WHERE id = '973ee36b953a4a0c9ca5d9f5868b015c';

INSERT INTO order_master (id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2) VALUES ('f6f50475354d49f68916eaf30ea5b266', '그랜드 핀턱 팬츠 (2color)', '3a18e633a5db4dbd8aaee218fe447fa4', 43000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111');
INSERT INTO order_item (id, ORDER_ID, item_id, count) VALUES ('c7bb4cb6efcd4f4bb388eafb6fa52fac', 'f6f50475354d49f68916eaf30ea5b266', 'c9402883dbe540e898a417e4884845bf', 1);

INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2, INVOICE_NUM) VALUES ('7cc837baca9d4510b7b9542c6a9213e2', 'f6f50475354d49f68916eaf30ea5b266', 4, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', '1234556778');
INSERT INTO delivery_item (id, delivery_id, item_id, count) VALUES ('7007fe7a1168469f969c036fcba06695', '7cc837baca9d4510b7b9542c6a9213e2', 'c9402883dbe540e898a417e4884845bf', 1);
UPDATE order_item SET delivery_item_id = '7007fe7a1168469f969c036fcba06695' WHERE id = 'c7bb4cb6efcd4f4bb388eafb6fa52fac';