-- 영양제 카테고리
INSERT INTO category (category_name) VALUES ('피부'), ('면역'), ('소화'), ('관절');

-- 영양제 데이터
INSERT INTO product (category_id, p_name, p_price, p_info) VALUES
(1, '영양제1', 10000, '영양제1 설명'),
(1, '영양제2', 15000, '영양제2 설명'),
(2, '영양제3', 20000, '영양제3 설명'),
(2, '영양제4', 25000, '영양제4 설명'),
(3, '영양제5', 30000, '영양제5 설명'),
(3, '영양제6', 35000, '영양제6 설명'),
(4, '영양제7', 40000, '영양제7 설명'),
(4, '영양제8', 45000, '영양제8 설명');