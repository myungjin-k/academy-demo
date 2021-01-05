INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('246fa96f9b634a56aaac5884de186ebc', 'C', 'cate_first', '메인카테고리' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('8afe84e9d3f948cf83bb1faed0175c63', 'C500', 'Pants', '바지', '246fa96f9b634a56aaac5884de186ebc' );

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

