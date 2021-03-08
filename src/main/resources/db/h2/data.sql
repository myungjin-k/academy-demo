INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('246fa96f9b634a56aaac5884de186ebc', 'C', 'cate_first', '메인카테고리' );
INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('36f651a982274a5b95dac3e9d85b0d1a', 'C100','Outer', '아우터' );
INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('3ebebfeb9fbe4ecfa5935f96ed308854', 'C400','Knitwear', '니트류' );
INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('ce0b9ee00c384a888ff5aad5b32350d1', 'ORDEREXCEL','OrderCol', '엑셀주문컬럼' );

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

INSERT INTO code_group (id, code, name_eng, name_kor) VALUES ('3ebebfeb9fbeabcde5935f96ed308854', 'SIZE','SIZE', '사이즈' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('979143570fc54211a83655db48a923dd', 'SIZE01', 'S', '', '3ebebfeb9fbeabcde5935f96ed308854' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('58735a53d92c40d2a5f440a5d8e0aa5d', 'SIZE02', 'M', '', '3ebebfeb9fbeabcde5935f96ed308854' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('bea264233aee48238808a116d1bfd321', 'SIZE03', 'L', '', '3ebebfeb9fbeabcde5935f96ed308854' );

INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('569fdd821927456fbabeac28156e32ba', '0', 'itemId', '상품ID', 'ce0b9ee00c384a888ff5aad5b32350d1' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('9eb16818d274481cbcaa15972797068f', '4', 'count', '수량', 'ce0b9ee00c384a888ff5aad5b32350d1' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('d248036523fc42b78c6f1145adf6fa1e', '5', 'name', '주문자명', 'ce0b9ee00c384a888ff5aad5b32350d1' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('ffbb229b495449eb83a932ad6d50f86f', '6', 'tel', '주문자연락처', 'ce0b9ee00c384a888ff5aad5b32350d1' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('388a3e659f8d41ba8133890fef10b856', '7', 'addr1', '주문자주소1', 'ce0b9ee00c384a888ff5aad5b32350d1' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('57a2cc19db7d4e52af2b75b8fe0937dd', '8', 'addr2', '주문자주소2', 'ce0b9ee00c384a888ff5aad5b32350d1' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('69fe34c552804cb6bae8dfdd3cd53004', '9', 'receiverName', '수신자명', 'ce0b9ee00c384a888ff5aad5b32350d1' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('df7922d739d64830aa7782646ec24541', '10', 'receiverTel', '수신자연락처', 'ce0b9ee00c384a888ff5aad5b32350d1' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('961df1f5df22493aaea7dee8141ace37', '11', 'receiverAddr1', '배송주소1', 'ce0b9ee00c384a888ff5aad5b32350d1' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('8435d4536f0c432c812904e47b012c8b', '12', 'receiverAddr2', '배송주소2', 'ce0b9ee00c384a888ff5aad5b32350d1' );
INSERT INTO common_code (id, code, name_eng, name_kor, group_id) VALUES ('ac553731de5e4f61961a0c016efc120e', '13', 'message', '배송메세지', 'ce0b9ee00c384a888ff5aad5b32350d1' );


INSERT INTO member (id, user_id, password, name, email, tel, addr1, addr2, reserves, order_amount) VALUES ('3a18e633a5db4dbd8aaee218fe447fa4', 'mjkim', '$2a$10$DDspWXEeR0OuJio7QfKQJukEC55JyHwkoUl/j2Zn64XLhuXoxNkKq', '명진', 'open7894.v2@gmail.com', '010-1234-5678','XX시 XX구 XX로', '1-1111', 300000, 50000);
INSERT INTO member (id, user_id, password, name, email, tel, addr1, addr2, reserves, order_amount) VALUES ('d408036ed8134e0bb0c3775e3f6dde8f', 'mjkim2', '$2a$10$DDspWXEeR0OuJio7QfKQJukEC55JyHwkoUl/j2Zn64XLhuXoxNkKq', '명진2', 'rla_mj@naver.com', '010-1111-1111','XX시 XX구 XX로', '1-1111', 300000, 0);

INSERT INTO admin (id, admin_id, password) VALUES ('3a18e633a5db4dbd8aaee218fe447fa4', 'mjkim', '$2a$10$DDspWXEeR0OuJio7QfKQJukEC55JyHwkoUl/j2Zn64XLhuXoxNkKq');

INSERT INTO item_master (id, item_name, category_id, price, thumbnail) VALUES ('8c1cbb792b8d447e9128d53920cf9366', '[R택] 보더 알파카 니트', '44e94265588b428e8e01bbc23dfc0f7e', 72000, 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/dev/itemMaster/4101950c-b655-49ce-b235-a482ce7a1773.jpg');

INSERT INTO item_option (id, color, master_id) VALUES ('b870e35c135e4782b531299753643fba', '콘베이지', '8c1cbb792b8d447e9128d53920cf9366');
INSERT INTO item_option (id, color, master_id) VALUES ('bed5b40336c844f1a78c431f111370fa', '크림아이보리', '8c1cbb792b8d447e9128d53920cf9366');

INSERT INTO item_master (id, item_name, category_id, price, thumbnail) VALUES ('c62bb955f4f94203b31f157fa72deef2', '[R택] 그랜트 핀턱 팬츠 (2color)', '8afe84e9d3f948cf83bb1faed0175c63', 43000, 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/dev/itemMaster/d9d9f106-d032-4e7b-a3c8-0ba9ef51ee91.jpg');

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
 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/dev/itemDisplay/e7bfe6ab-76b7-4df6-a557-51c83e30599d.jpg');

INSERT INTO item_display_option (id, color, size, status, display_id) VALUES ('91cc1c18f11e4d018566524b51d8419a', '크림', 'S', '1', 'f23ba30a47194a2c8a3fd2ccadd952a4');
INSERT INTO item_display_option (id, color, size, status, display_id) VALUES ('91cc1c18f11e5d018566524b51d8419a', '크림', 'M', '1', 'f23ba30a47194a2c8a3fd2ccadd952a4');
INSERT INTO item_display_option (id, color, size, status, display_id) VALUES ('c9402883dbe540e898a417e4884845bf', '포그그레이', 'S', '1', 'f23ba30a47194a2c8a3fd2ccadd952a4');
INSERT INTO item_display_option (id, color, size, status, display_id) VALUES ('c9402883dbe540e898a417e4984845bf', '포그그레이', 'M', '1', 'f23ba30a47194a2c8a3fd2ccadd952a4');

INSERT INTO item_master (id, item_name, category_id, price, thumbnail)
VALUES ('6b5cd6e21ffa4bb08a75e270c18e8e05', '[N택] 시티 하이넥 코트 (2color)', '979143570fc54210a83655db48a923dd', 190000, 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/dev/itemMaster/095748bf-a3ba-4c9a-a239-aadbed7ac65d.jpg');

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
 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/dev/itemDisplay/f3427d05-9e39-4f2e-998b-ab1af4326986.jpg'
);

INSERT INTO item_display_option (id, color, status, display_id) VALUES ('8130cede06c04fa2bbd8bc09a29787c8', '딥브라운', '1', '6bdbf6eea40b425caae4410895ca4809');
INSERT INTO item_display_option (id, color, status, display_id) VALUES ('fd778631e13944f393a73abf9e5dc5cf', '차콜그레이', '1', '6bdbf6eea40b425caae4410895ca4809');

INSERT INTO item_master (id, item_name, category_id, price, thumbnail) VALUES ('cc068e14469a4d0a958f8ff072009f04', '[R택] 로뎀 블랙 팬츠', '8afe84e9d3f948cf83bb1faed0175c63', 72000, 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/dev/itemMaster/5bc15c96-d6d4-4a45-8186-c707a47deef8.jpg');

INSERT INTO item_option (id, size, master_id) VALUES ('97d1a59bc83045ef99c633f4c36ac10c', 'S', 'cc068e14469a4d0a958f8ff072009f04');
INSERT INTO item_option (id, size, master_id) VALUES ('ca3720d375374d8cb0a11cd867aeb510', 'M', 'cc068e14469a4d0a958f8ff072009f04');

INSERT INTO item_display (id, item_id, item_display_name, sale_price, material, size, description, status, detail_image) VALUES ('839ebdcc2b2843a6ae681d9cb82c6a7c', 'cc068e14469a4d0a958f8ff072009f04', '로뎀 블랙 팬츠', 35000,
 '면100',
 'S(26-27) 허리 32 엉덩이 46 허벅지 26 밑위 29 밑단 15 총길이 86
M(28-29) 허리 34 엉덩이 47 허벅지 27 밑위 29 밑단 16 총길이 86',
 '경쾌한 기장으로 깔끔한 핏의 블랙 팬츠 입니다.

어디든 매치하기 좋은 스탠다드한 일자 핏입니다.

적당히 슬림하면서도 깔끔하게 일자로 툭 떨어집니다.

발목이 살짝 드러나는 경쾌한 기장으로
양말과 함께 코디하기에도 좋고
부츠와 매치하기도 알맞습니다.

딥한 블랙 데님으로
워싱감이 거의 느껴지지 않아 깔끔하고
실버 버튼으로 어디든 쉽게 매치 하기 좋답니다.
여기저기 매치하기 좋아 다양하게 활용하게될 아이템입니다.

COLOR
워싱감이 적은 블랙 데님

INFO
신축성-없음｜촉감-적당함｜계절감-봄/가을', 1 ,
 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/dev/itemDisplay/26e6c731-f9ae-4474-af8a-cfea6d3dbd92.jpg');

INSERT INTO item_display_option (id, size, status, display_id) VALUES ('15b84d4afc6644fcbc64ef8c2e6212bd', 'S', '1', '839ebdcc2b2843a6ae681d9cb82c6a7c');
INSERT INTO item_display_option (id, size, status, display_id) VALUES ('2a6866f21c4f4a37b1e2f80057884792', 'M', '1', '839ebdcc2b2843a6ae681d9cb82c6a7c');

INSERT INTO item_master (id, item_name, category_id, price, thumbnail) VALUES ('abe14fe1e242449886adf0e78088e847', '[N택] 마론 라쿤 골지 가디건', '0cba0f39833e443598f3a07d4b36dca9', 61000, 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/dev/itemMaster/9b1ab150-7c71-42b6-b672-76ffd42a5b84.jpg');

INSERT INTO item_option (id, color, master_id) VALUES ('8812e33b92464834b0e4ff394acb89e8', '라떼베이지', 'abe14fe1e242449886adf0e78088e847');
INSERT INTO item_option (id, color, master_id) VALUES ('d95a3d3508e04688a41b8824e61f1c27', '시나몬브라운', 'abe14fe1e242449886adf0e78088e847');
INSERT INTO item_option (id, color, master_id) VALUES ('3e9032e8a494447ca384156112f2d75f', '스페이스블루', 'abe14fe1e242449886adf0e78088e847');

INSERT INTO item_display (id, item_id, item_display_name, sale_price, material, size, description, status, detail_image) VALUES ('fd0b906391ac4636b7984ab756006144', 'abe14fe1e242449886adf0e78088e847', '마론 라쿤 골지 가디건', 50000,
 '라쿤30 울30 나일론40',
 '어깨 39.5 가슴 47 암홀 21 팔통 14.5 소매단 7.5 팔길이 57 총길이 56',
 '심플하게 착용하기 좋은 라쿤 골지 가디건입니다.

울과 라쿤 혼방의 라이트한 무게감의 원단으로
라쿤 소재 특유의 포슬한 헤어감이 느껴집니다.

은은한 짜임의 잔잔한 골지 무늬와
미니멀한 크기의 자개 단추는 깔끔하면서 포인트가 됩니다.

너무 오버하지도 핏 되지도 않는 베이직한 실루엣으로
넣어입거나 빼어 입기 모두 좋은 기장감입니다.

적당한 깊이로 파인 브이넥으로
모델과 같이 단추를 모두 잠궈 니트처럼 활용하기 좋습니다.

부드러운 라떼빛이 감도는 따뜻한 베이지,
약간의 붉은빛이 감도는 깊은 브라운,
그리고 고급스러운 청록빛이 감도는 딥한 블루 색상 준비했습니다.
세 컬러 모두 베이직하면서도 특별한, 소장가치 있는 색상으로 빠짐없이 추천드립니다.

*소재 특성상 잡실/털빠짐/냄새/까끌거림이 있을 수 있습니다.

COLOR
라떼베이지-부드러운 라떼빛이 감도는 따뜻한 베이지
시나몬브라운-약간의 붉은빛이 감도는 깊은 브라운
스페이스블루-고급스러운 청록빛이 감도는 딥한 블루

INFO
신축성-있음｜계절감-가을/겨울｜비침- 없음｜촉감-적당함', 1 ,
 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/dev/itemDisplay/26e6c731-f9ae-4474-af8a-cfea6d3dbd92.jpg');

INSERT INTO item_display_option (id, color, status, display_id) VALUES ('fd0b906391ac4636b7984ab756006144', '라떼베이지', '1', 'fd0b906391ac4636b7984ab756006144');
INSERT INTO item_display_option (id, color, status, display_id) VALUES ('3d190d6970ff423c803445d7f6270895', '시나몬브라운', '1', 'fd0b906391ac4636b7984ab756006144');
INSERT INTO item_display_option (id, color, status, display_id) VALUES ('595ceb8327384a098ec17de8121c77d9', '스페이스블루', '1', 'fd0b906391ac4636b7984ab756006144');
INSERT INTO item_master (id, item_name, category_id, price, thumbnail) VALUES ('2f120d7aa59f464ea35527c2900e6a57', '[N택] 브론테 바이 문 머플러', '88b09a7b0a7d4390a67ab3adf1d6d378', 61000, 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/dev/itemMaster/2f018400-b2bf-489e-bfe5-891102674837.jpg');

INSERT INTO item_option (id, color, master_id) VALUES ('92b0a37bbc0a411ea8363b4c7857d3dd', '모스그린', '2f120d7aa59f464ea35527c2900e6a57');
INSERT INTO item_option (id, color, master_id) VALUES ('4a4b3309e8d44eaa92ebdbc8a5e358c9', '카라멜브라운', '2f120d7aa59f464ea35527c2900e6a57');

INSERT INTO item_display (id, item_id, item_display_name, sale_price, material, size, description, status, detail_image) VALUES ('f9f3f6f31e0c4b889ff5bd020459d014', '2f120d7aa59f464ea35527c2900e6a57', '브론테 바이 문 머플러 (2color)', 54000,
 '영국산 메리노울 100',
 '너비 25 총길이(프린지제외) 174',
 '영국을 대표하는 프리미엄 브랜드
bronte by moon사 원단으로 제작된 머플러입니다.

브론테 바이 문은 180년 이상을 이어온 세계 3대 직물 제조 명가
아브라함 문의 홈패션 및 패션 소품 전문 브랜드로,
디자인부터 직물의 생산 염색까지
모든 공정을 직접 진행하며 엄격하게 관리하고 있습니다.

-

영국산 메리노울 100 소재로 일반 양모 대비 부드럽고 유연한
어린 양털을 사용하여 통기성과 보온성이 우수합니다.

데일리하게 착용하기 좋은 가벼운 중량감으로
자연스럽게 두어번 둘러 매어 착용하시거나
머플러를 반으로 접어 클래식하게 착용하셔도 멋스럽습니다.

대체적으로 무게감이 느껴지는 겨울 아이템들 속에서
조금 더 편안하고 라이트한 포인트 아이템을 찾으셨던 분들께 추천드립니다.
-

끝단의 내추럴한 프린지 디테일은 은은하게 포인트 됩니다.
고급스러운 짜임과 컬러 구성 또한 매력적입니다.

비교적 단조로운 색감의 겨울 옷들 사이에서
은근하게 포인트가 되어줄 2가지 색상 준비했습니다.

오묘한 녹색 빛의 모스그린
은은한 카라멜 빛의 깊은 카라멜브라운
각기 다른 매력으로 룩을 더욱 빛나게 해줄 매력적인 컬러들입니다.
-
클래식하면서도 고급스럽게 착용하기 좋은 시즌 아이템으로
옷장에 하나쯤 소장하신다면
매년 겨울 꺼내 입기 좋은 소장 가치 있는 머플러로 추천드립니다.

COLOR
모스그린-여러색의 원사가 섞인 오묘한 녹색 빛
카라멜브라운-은은한 카라멜빛의 깊은 브라운', 1 ,
'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/dev/itemDisplay/b7c35a29-a187-4d9e-acb6-31b1e1eb0fa2.jpg');

INSERT INTO item_display_option (id, color, status, display_id) VALUES ('86987dc60e6041b4a3966709f71ee7e2', '모스그린', '1', 'f9f3f6f31e0c4b889ff5bd020459d014');
INSERT INTO item_display_option (id, color, status, display_id) VALUES ('651213add24840afb2a4be6657c2b687', '카라멜브라운', '1', 'f9f3f6f31e0c4b889ff5bd020459d014');
INSERT INTO item_master (id, item_name, category_id, price, thumbnail) VALUES ('b193a871f08c46649f7fcd724e4b042d', '[N택] 린크 홀가먼트 니트', '44e94265588b428e8e01bbc23dfc0f7e', 61000, 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/dev/itemMaster/13ccd6f8-8a65-482c-b84c-f730451860ac.jpg');

INSERT INTO item_option (id, color, master_id) VALUES ('abef2dc914794775809597d91ffa5e08', '블랙', 'b193a871f08c46649f7fcd724e4b042d');
INSERT INTO item_option (id, color, master_id) VALUES ('a9868a3ea5054aeda5a4be68da41c301', '그레이', 'b193a871f08c46649f7fcd724e4b042d');
INSERT INTO item_option (id, color, master_id) VALUES ('003787b9a782423da236faa2924b4e60', '카키브라운', 'b193a871f08c46649f7fcd724e4b042d');

INSERT INTO item_display (id, item_id, item_display_name, sale_price, material, size, description, status, detail_image) VALUES ('a8853254ada64200b9821b4dde6b02bb', 'b193a871f08c46649f7fcd724e4b042d', '린크 홀가먼트 니트 ', 41000,
 '캐시미어5 파인울25 바셀린울15 캐시나일론55',
 '어깨 - 가슴 50.5 암홀 30 팔통 19 소매단 10.5 팔길이 68 총길이 65.5',
 '데일리하게 착용하기 좋은 홀가먼트 니트입니다.

봉제선이 없는 홀가먼트 방식으로 제작되어 착용감이 편안하며
어깨 라인을 타고 자연스럽게 흐르는 내추럴한 실루엣이 매력적입니다.

밑단과 넥라인의 적당한 너비의 골지 짜임은
심심하지 않으면서 은은하게 포인트 됩니다.

캐시미어와 울이 블랜딩된 적당한 두께감의 원단으로
드라이하면서도 유연한 촉감이 느껴집니다.

여유로운 품과 기장감으로 비교적 체형 구애 없이 착용 가능하며
단품으로는 물론 아우터 안에 이너로 착용하기도 좋은,
베이직하면서 데일리하게 손이 갈 아이템으로 추천드립니다.

*소재 특성상 잡실/털빠짐/냄새 등이 있을 수 있습니다.

COLOR
블랙-정석블랙
그레이-적당한 밝기의 그레이
카키브라운-카키빛이 감도는 차분하고 따뜻한 브라운

INFO
신축성-있음｜두께감-적당함｜비침- 없음｜촉감- 적당함', 1 ,
 'https://myungjin-mall.s3.ap-northeast-2.amazonaws.com/dev/itemDisplay/a790eb72-c3f2-47c9-b80f-572a98da04d1.jpg');

INSERT INTO item_display_option (id, color, status, display_id) VALUES ('f8c0e9297e624113ae85c3cdd8a3e7f5', '블랙', '1', 'a8853254ada64200b9821b4dde6b02bb');
INSERT INTO item_display_option (id, color, status, display_id) VALUES ('e3550d96ded84daaa5078f2e0685e015', '그레이', '1', 'a8853254ada64200b9821b4dde6b02bb');
INSERT INTO item_display_option (id, color, status, display_id) VALUES ('ed764c2577694c1788a122a0438524d1', '카키브라운', '1', 'a8853254ada64200b9821b4dde6b02bb');

INSERT INTO cart (id, member_id, item_id, count) VALUES ('f4597dfc1ae649a58edcb7921002aca5', '3a18e633a5db4dbd8aaee218fe447fa4', 'c9402883dbe540e898a417e4884845bf', 1);
INSERT INTO cart (id, member_id, item_id, count) VALUES ('0a25d9eea6d94a3897e06b33e4bf5b69', '3a18e633a5db4dbd8aaee218fe447fa4', '91cc1c18f11e4d018566524b51d8419a', 1);

INSERT INTO order_master (order_email, id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2) VALUES ('open7894.v2@gmail.com', '03039b4535404247bfee52cfd934c779', '그랜드 핀턱 팬츠 (2color) 外 1건', '3a18e633a5db4dbd8aaee218fe447fa4', 43000 * 2 + 190000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111');
INSERT INTO order_item (id, ORDER_ID, item_id, count) VALUES ('973ee36b953a4a0c9ca5d9f5868b015c', '03039b4535404247bfee52cfd934c779', 'c9402883dbe540e898a417e4884845bf', 2);
INSERT INTO order_item (id, ORDER_ID, item_id, count) VALUES ('be9089f9bf1f4e59b712e3187b38a0d7', '03039b4535404247bfee52cfd934c779', '8130cede06c04fa2bbd8bc09a29787c8', 1);

INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2) VALUES ('cd2940ee2dfc418384eedc450be832a2', '03039b4535404247bfee52cfd934c779', 1, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111');
INSERT INTO delivery_item (id, delivery_id, item_id, count) VALUES ('d14b36612cd047a0b1e4e71d993dc9b2', 'cd2940ee2dfc418384eedc450be832a2', 'c9402883dbe540e898a417e4884845bf', 2);
UPDATE order_item SET delivery_item_id = 'd14b36612cd047a0b1e4e71d993dc9b2' WHERE id = '973ee36b953a4a0c9ca5d9f5868b015c';

INSERT INTO order_master (order_email, id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2) VALUES ('open7894.v2@gmail.com', 'f6f50475354d49f68916eaf30ea5b266', '그랜드 핀턱 팬츠 (2color)', '3a18e633a5db4dbd8aaee218fe447fa4', 43000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111');
INSERT INTO order_item (id, ORDER_ID, item_id, count) VALUES ('c7bb4cb6efcd4f4bb388eafb6fa52fac', 'f6f50475354d49f68916eaf30ea5b266', 'c9402883dbe540e898a417e4884845bf', 1);

INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2, INVOICE_NUM) VALUES ('7cc837baca9d4510b7b9542c6a9213e2', 'f6f50475354d49f68916eaf30ea5b266', 4, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', '1234556778');
INSERT INTO delivery_item (id, delivery_id, item_id, count) VALUES ('7007fe7a1168469f969c036fcba06695', '7cc837baca9d4510b7b9542c6a9213e2', 'c9402883dbe540e898a417e4884845bf', 1);
UPDATE order_item SET delivery_item_id = '7007fe7a1168469f969c036fcba06695' WHERE id = 'c7bb4cb6efcd4f4bb388eafb6fa52fac';

INSERT INTO review (id, member_id, item_id, order_item_id, content) VALUES ('43f217fd86c34ce0a305e02b9972a29e', '3a18e633a5db4dbd8aaee218fe447fa4', 'f23ba30a47194a2c8a3fd2ccadd952a4', 'c7bb4cb6efcd4f4bb388eafb6fa52fac', '예뻐요');
INSERT INTO review_comment(id, admin_id, review_id, content) values ('c2859d0c42974981b6a1a04f39f68bb1', '3a18e633a5db4dbd8aaee218fe447fa4', '43f217fd86c34ce0a305e02b9972a29e', '리뷰 감사드립니다. 500원 적립 도와드리겠습니다~!');


INSERT INTO order_master (order_email, id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2, create_at) VALUES
('open7894.v2@gmail.com', '1c787e3c4f6a404194016698d8c760a2','그랜드 핀턱 팬츠 (2color)', '3a18e633a5db4dbd8aaee218fe447fa4', 43000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO order_item (id, ORDER_ID, item_id, count, create_at) VALUES ('c2f7602adc44444fb0824f6be5260a0b', '1c787e3c4f6a404194016698d8c760a2', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2, ext_delivery_id, create_at) VALUES
('2ea7f983db2e486e9f6483b9d4bc99c0', '1c787e3c4f6a404194016698d8c760a2', 1, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', 'EXT001', dateadd('hour', -3, current_timestamp));
INSERT INTO delivery_item (id, delivery_id, item_id, count, create_at) VALUES ('2701a20da18a4f35b870e52cd6c588ac', '2ea7f983db2e486e9f6483b9d4bc99c0', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
UPDATE order_item SET delivery_item_id = '2701a20da18a4f35b870e52cd6c588ac' WHERE id = 'c2f7602adc44444fb0824f6be5260a0b';

INSERT INTO order_master (order_email, id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2, create_at) VALUES
('open7894.v2@gmail.com', 'd74e0c8f7db24f349196a385bebeaa9c','그랜드 핀턱 팬츠 (2color)', '3a18e633a5db4dbd8aaee218fe447fa4', 43000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO order_item (id, ORDER_ID, item_id, count, create_at) VALUES ('cb64377b68e7417b99ca512bba003492', 'd74e0c8f7db24f349196a385bebeaa9c', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2, ext_delivery_id, create_at) VALUES
('a50ab45c8f2a4fa7ba9393340d6c1f85', 'd74e0c8f7db24f349196a385bebeaa9c', 1, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', 'EXT002', dateadd('hour', -3, current_timestamp));
INSERT INTO delivery_item (id, delivery_id, item_id, count, create_at) VALUES ('1feb0f7f001e451bb0964854f311ef98', 'a50ab45c8f2a4fa7ba9393340d6c1f85', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
UPDATE order_item SET delivery_item_id = '1feb0f7f001e451bb0964854f311ef98' WHERE id = 'cb64377b68e7417b99ca512bba003492';

INSERT INTO order_master (order_email, id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2, create_at) VALUES
('open7894.v2@gmail.com', '88a88c1c67334924825db1fff2d56279','그랜드 핀턱 팬츠 (2color)', '3a18e633a5db4dbd8aaee218fe447fa4', 43000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO order_item (id, ORDER_ID, item_id, count, create_at) VALUES ('42aeeba0eebc43c384e4827e70631aee', '88a88c1c67334924825db1fff2d56279', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2, ext_delivery_id, create_at) VALUES
('349e3e49e4704b01b9a36777277c8352', '88a88c1c67334924825db1fff2d56279', 1, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', 'EXT003', dateadd('hour', -3, current_timestamp));
INSERT INTO delivery_item (id, delivery_id, item_id, count, create_at) VALUES ('086b8c8016644dc094a5e398f830b8b6', '349e3e49e4704b01b9a36777277c8352', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
UPDATE order_item SET delivery_item_id = '086b8c8016644dc094a5e398f830b8b6' WHERE id = '42aeeba0eebc43c384e4827e70631aee';

INSERT INTO order_master (order_email, id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2, create_at) VALUES
('open7894.v2@gmail.com', 'e938c8eef4254f4baed023d856aef508','그랜드 핀턱 팬츠 (2color)', '3a18e633a5db4dbd8aaee218fe447fa4', 43000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO order_item (id, ORDER_ID, item_id, count, create_at) VALUES ('8521ffb112334dc894c4e3893cd8001b', 'e938c8eef4254f4baed023d856aef508', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2, create_at) VALUES
('43b5c2eaef544a54b815a75ab1c4630d', 'e938c8eef4254f4baed023d856aef508', 1, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO delivery_item (id, delivery_id, item_id, count, create_at) VALUES ('b72add0ebdc94cbca92f3ef8cf3cb12a', '43b5c2eaef544a54b815a75ab1c4630d', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
UPDATE order_item SET delivery_item_id = 'b72add0ebdc94cbca92f3ef8cf3cb12a' WHERE id = '8521ffb112334dc894c4e3893cd8001b';

INSERT INTO order_master (order_email, id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2, create_at) VALUES
('open7894.v2@gmail.com', '2f5c5865742344fcb078ade1f3be38fe','그랜드 핀턱 팬츠 (2color)', '3a18e633a5db4dbd8aaee218fe447fa4', 43000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO order_item (id, ORDER_ID, item_id, count, create_at) VALUES ('065ea3f9443b422ca89857854182f7fc', '2f5c5865742344fcb078ade1f3be38fe', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2, create_at) VALUES
('18de65123bbf4b518b93a34a14ed0b30', '2f5c5865742344fcb078ade1f3be38fe', 1, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO delivery_item (id, delivery_id, item_id, count, create_at) VALUES ('2f2a0f5d72e546f9885c470ab302ef38', '18de65123bbf4b518b93a34a14ed0b30', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
UPDATE order_item SET delivery_item_id = '2f2a0f5d72e546f9885c470ab302ef38' WHERE id = '065ea3f9443b422ca89857854182f7fc';

INSERT INTO order_master (order_email, id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2, create_at) VALUES
('open7894.v2@gmail.com', '1d082677e18943f3a7804327f678008a','그랜드 핀턱 팬츠 (2color)', '3a18e633a5db4dbd8aaee218fe447fa4', 43000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO order_item (id, ORDER_ID, item_id, count, create_at) VALUES ('b633ea73772146609a910ceaf466edd4', '1d082677e18943f3a7804327f678008a', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2, create_at) VALUES
('17fb2f8199cf42d0b0b8db850876ea3a', '1d082677e18943f3a7804327f678008a', 1, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO delivery_item (id, delivery_id, item_id, count, create_at) VALUES ('db1daff8e7ac4c4792e1f63abb80e7c9', '17fb2f8199cf42d0b0b8db850876ea3a', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
UPDATE order_item SET delivery_item_id = 'db1daff8e7ac4c4792e1f63abb80e7c9' WHERE id = 'b633ea73772146609a910ceaf466edd4';

INSERT INTO order_master (order_email, id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2, create_at) VALUES
('open7894.v2@gmail.com', 'a0d44030f35348afbe5c8861b203535f','그랜드 핀턱 팬츠 (2color)', '3a18e633a5db4dbd8aaee218fe447fa4', 43000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO order_item (id, ORDER_ID, item_id, count, create_at) VALUES ('ee442e0ce2f14e06b38a8d2ed2082434', 'a0d44030f35348afbe5c8861b203535f', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2, create_at) VALUES
('91974bfe7fb943b58d06e0a9098f2c1d', 'a0d44030f35348afbe5c8861b203535f', 1, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO delivery_item (id, delivery_id, item_id, count, create_at) VALUES ('968a8cc4bcb649959c064f436efb8b84', '91974bfe7fb943b58d06e0a9098f2c1d', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
UPDATE order_item SET delivery_item_id = '968a8cc4bcb649959c064f436efb8b84' WHERE id = 'ee442e0ce2f14e06b38a8d2ed2082434';

INSERT INTO order_master (order_email, id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2, create_at) VALUES
('open7894.v2@gmail.com', 'd0c993bfd3764b56be18f523f655f970','그랜드 핀턱 팬츠 (2color)', '3a18e633a5db4dbd8aaee218fe447fa4', 43000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO order_item (id, ORDER_ID, item_id, count, create_at) VALUES ('1e5d75ca327e4ca8bd9e840c3b055e8f', 'd0c993bfd3764b56be18f523f655f970', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2, create_at) VALUES
('ddd58d9835fa4e648c04d56dc3b7d80a', 'd0c993bfd3764b56be18f523f655f970', 1, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO delivery_item (id, delivery_id, item_id, count, create_at) VALUES ('c8f753c3ea814fea9c61298bfecbb4b7', 'ddd58d9835fa4e648c04d56dc3b7d80a', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
UPDATE order_item SET delivery_item_id = 'c8f753c3ea814fea9c61298bfecbb4b7' WHERE id = '1e5d75ca327e4ca8bd9e840c3b055e8f';

INSERT INTO order_master (order_email, id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2, create_at) VALUES
('open7894.v2@gmail.com', '474d822e2e3c4abca2b5e3c63f5ce6ec','그랜드 핀턱 팬츠 (2color)', '3a18e633a5db4dbd8aaee218fe447fa4', 43000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO order_item (id, ORDER_ID, item_id, count, create_at) VALUES ('8b64229c99bf4165828e6fcb14f365e9', '474d822e2e3c4abca2b5e3c63f5ce6ec', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2, create_at) VALUES
('e30eb6a630224300bb42c0c71c7a55bf', '474d822e2e3c4abca2b5e3c63f5ce6ec', 1, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO delivery_item (id, delivery_id, item_id, count, create_at) VALUES ('e69b10826ce44be3bdcec5a0060bb83f', 'e30eb6a630224300bb42c0c71c7a55bf', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
UPDATE order_item SET delivery_item_id = 'e69b10826ce44be3bdcec5a0060bb83f' WHERE id = '8b64229c99bf4165828e6fcb14f365e9';

INSERT INTO order_master (order_email, id, abbr_items_name, member_id, total_amount, order_name, order_tel, order_addr1, order_addr2, create_at) VALUES
('open7894.v2@gmail.com', '49ba26196ba8435dbd1e9ade8fd5263a','그랜드 핀턱 팬츠 (2color)', '3a18e633a5db4dbd8aaee218fe447fa4', 43000, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO order_item (id, ORDER_ID, item_id, count, create_at) VALUES ('8f811e5ef62c4eacbe3166bc311a5a43', '49ba26196ba8435dbd1e9ade8fd5263a', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
INSERT INTO delivery (id, order_id, STATUS, RECEIVER_NAME, RECEIVER_TEL, RECEIVER_ADDR1, RECEIVER_ADDR2, create_at) VALUES
('fddd09b724444c749c3d6e33d923f2f3', '49ba26196ba8435dbd1e9ade8fd5263a', 1, '명진', '010-1234-5678','XX시 XX구 XX로', '1-1111', dateadd('hour', -3, current_timestamp));
INSERT INTO delivery_item (id, delivery_id, item_id, count, create_at) VALUES ('59628a352b8f46f9ad94c9ca3c9d11bf', 'fddd09b724444c749c3d6e33d923f2f3', 'c9402883dbe540e898a417e4884845bf', 1, dateadd('hour', -3, current_timestamp));
UPDATE order_item SET delivery_item_id = '59628a352b8f46f9ad94c9ca3c9d11bf' WHERE id = '8f811e5ef62c4eacbe3166bc311a5a43';

// 배송정보 수신 데이터
INSERT INTO received_delivery_status (id, ext_delivery_id, seq, status) values ('d22555848ba84718a1d48414a21d371e','EXT001', 1, 2);
INSERT INTO received_delivery_status (id, ext_delivery_id, seq, status) values ('4918a486e4044bb2acc0edeea7cfcbcf', 'EXT002', 1, 2);
INSERT INTO received_delivery_status (id, ext_delivery_id, seq, status) values ('51e6c2dd4cef49419d499e6f9d1bb4aa', 'EXT003', 1, 2);

INSERT INTO reserves_history (member_id, type, amount) values ('3a18e633a5db4dbd8aaee218fe447fa4', 'ADMIN', 300000);

INSERT INTO event (seq, name, type, discount_ratio, start_at, end_at, status) values (1, '아우터 20% 할인', 'P', '20', current_date, current_date, 1);
INSERT INTO event_item (id, event_seq, item_id) values ('02495bd30acb45c5b9b27cd1df5e5c7d', 1, '6bdbf6eea40b425caae4410895ca4809');