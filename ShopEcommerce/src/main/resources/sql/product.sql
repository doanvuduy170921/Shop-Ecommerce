INSERT INTO attribute_group (name) VALUES 
('Thông số kỹ thuật'),
('Màn hình'),
('Pin & Sạc'),
('Âm thanh');

INSERT INTO attribute (group_id, name) VALUES 
(1, 'Bộ vi xử lý'),
(1, 'RAM'),
(1, 'Bộ nhớ trong'),
(1, 'Hệ điều hành'),
(2, 'Kích thước màn hình'),
(2, 'Công nghệ màn hình'),
(3, 'Dung lượng pin'),
(3, 'Công suất sạc'),
(4, 'Công nghệ âm thanh');

INSERT INTO categories (name) VALUES 
('Điện thoại'),
('Laptop'),
('Tai nghe'),
('Loa Bluetooth'),
('Phụ kiện công nghệ'),
('Màn hình máy tính'),
('Máy tính bảng'),
('Đồng hồ thông minh');

INSERT INTO products (name, description, price, thumbnail, category_id, created_at, updated_at) VALUES 
-- Điện thoại (category_id = 1)
('iPhone 15 Pro', 'Điện thoại Apple cao cấp với chip A17 Pro', 25000000, 'iphone15pro.jpg', 1, NOW(), NOW()),
('Samsung Galaxy S23 Ultra', 'Flagship của Samsung với camera 200MP', 28000000, 'galaxy_s23_ultra.jpg', 1, NOW(), NOW()),
('Xiaomi 13 Pro', 'Điện thoại Xiaomi với chip Snapdragon 8 Gen 2', 20000000, 'xiaomi_13_pro.jpg', 1, NOW(), NOW()),
('Google Pixel 7 Pro', 'Điện thoại Google với camera AI thông minh', 23000000, 'pixel_7_pro.jpg', 1, NOW(), NOW()),
('OnePlus 11', 'Hiệu năng mạnh mẽ với OxygenOS', 18000000, 'oneplus_11.jpg', 1, NOW(), NOW()),
('Oppo Find X5 Pro', 'Camera Hasselblad chất lượng cao', 21000000, 'oppo_find_x5_pro.jpg', 1, NOW(), NOW()),
('Vivo X90 Pro', 'Điện thoại flagship của Vivo', 19000000, 'vivo_x90_pro.jpg', 1, NOW(), NOW()),
('Realme GT Neo 5', 'Gaming phone với sạc 150W', 17000000, 'realme_gt_neo5.jpg', 1, NOW(), NOW()),
('Asus ROG Phone 6', 'Smartphone gaming chuyên nghiệp', 25000000, 'rog_phone_6.jpg', 1, NOW(), NOW()),
('Nothing Phone (2)', 'Thiết kế LED độc đáo', 15000000, 'nothing_phone_2.jpg', 1, NOW(), NOW()),

-- Laptop (category_id = 2)
('MacBook Pro M2', 'Laptop hiệu năng cao với chip Apple M2', 35000000, 'macbook_pro_m2.jpg', 2, NOW(), NOW()),
('Dell XPS 15', 'Laptop Windows mạnh mẽ với màn hình OLED', 32000000, 'dell_xps_15.jpg', 2, NOW(), NOW()),
('Asus ROG Zephyrus G14', 'Laptop gaming nhỏ gọn nhưng mạnh mẽ', 30000000, 'rog_zephyrus_g14.jpg', 2, NOW(), NOW()),
('HP Spectre x360', 'Laptop 2 trong 1 cao cấp từ HP', 27000000, 'hp_spectre_x360.jpg', 2, NOW(), NOW()),
('Lenovo ThinkPad X1 Carbon', 'Laptop doanh nhân bền bỉ và nhẹ', 31000000, 'thinkpad_x1_carbon.jpg', 2, NOW(), NOW()),
('Acer Predator Helios 300', 'Laptop gaming mạnh mẽ', 29000000, 'predator_helios_300.jpg', 2, NOW(), NOW()),
('MSI Stealth 15M', 'Laptop gaming siêu mỏng', 28000000, 'msi_stealth_15m.jpg', 2, NOW(), NOW()),
('LG Gram 17', 'Laptop siêu nhẹ với màn hình lớn', 27000000, 'lg_gram_17.jpg', 2, NOW(), NOW()),
('Razer Blade 16', 'Laptop gaming cao cấp từ Razer', 40000000, 'razer_blade_16.jpg', 2, NOW(), NOW()),
('Microsoft Surface Laptop 5', 'Laptop 2-trong-1 mạnh mẽ', 25000000, 'surface_laptop_5.jpg', 2, NOW(), NOW()),

-- Tai nghe (category_id = 3)
('AirPods Pro 2', 'Tai nghe không dây chống ồn của Apple', 6000000, 'airpods_pro_2.jpg', 3, NOW(), NOW()),
('Sony WH-1000XM5', 'Tai nghe chống ồn cao cấp từ Sony', 9000000, 'sony_wh_1000xm5.jpg', 3, NOW(), NOW()),
('Bose QuietComfort 45', 'Tai nghe chống ồn chủ động từ Bose', 8500000, 'bose_qc_45.jpg', 3, NOW(), NOW()),
('Sennheiser Momentum 4', 'Tai nghe cao cấp với âm thanh chất lượng', 9500000, 'sennheiser_momentum_4.jpg', 3, NOW(), NOW()),
('Beats Studio Buds', 'Tai nghe không dây từ Beats với âm bass mạnh', 5000000, 'beats_studio_buds.jpg', 3, NOW(), NOW()),
('JBL Tune 760NC', 'Tai nghe không dây chống ồn', 4000000, 'jbl_tune_760nc.jpg', 3, NOW(), NOW()),
('AKG N700NC', 'Tai nghe studio chuyên nghiệp', 7000000, 'akg_n700nc.jpg', 3, NOW(), NOW()),
('Shure AONIC 50', 'Tai nghe audiophile cao cấp', 12000000, 'shure_aonic_50.jpg', 3, NOW(), NOW()),
('Marshall Major IV', 'Tai nghe on-ear phong cách retro', 4500000, 'marshall_major_iv.jpg', 3, NOW(), NOW()),
('Soundcore Life Q35', 'Tai nghe giá rẻ chất lượng tốt', 3500000, 'soundcore_life_q35.jpg', 3, NOW(), NOW()),

-- Loa Bluetooth (category_id = 4)
('JBL Charge 5', 'Loa Bluetooth chống nước với âm bass mạnh mẽ', 4000000, 'jbl_charge_5.jpg', 4, NOW(), NOW()),
('Sony SRS-XB43', 'Loa di động Sony Extra Bass', 5000000, 'sony_srs_xb43.jpg', 4, NOW(), NOW()),
('Bose SoundLink Revolve+', 'Loa 360 độ cao cấp từ Bose', 7000000, 'bose_soundlink_revolve_plus.jpg', 4, NOW(), NOW()),
('Ultimate Ears Boom 3', 'Loa chống nước nhỏ gọn', 4500000, 'ue_boom_3.jpg', 4, NOW(), NOW()),
('Marshall Emberton', 'Loa Bluetooth nhỏ gọn với thiết kế retro', 4800000, 'marshall_emberton.jpg', 4, NOW(), NOW()),
('Harman Kardon Onyx Studio 7', 'Loa thiết kế sang trọng', 6000000, 'harman_kardon_onyx_7.jpg', 4, NOW(), NOW()),
('Anker Soundcore Motion+', 'Loa di động giá tốt', 3500000, 'anker_motion_plus.jpg', 4, NOW(), NOW()),
('Tribit StormBox Pro', 'Loa di động chống nước', 3000000, 'tribit_stormbox_pro.jpg', 4, NOW(), NOW()),
('Dynaudio Music 1', 'Loa cao cấp cho audiophile', 12000000, 'dynaudio_music_1.jpg', 4, NOW(), NOW()),
('Bang & Olufsen Beosound A1', 'Loa di động chất âm premium', 13000000, 'b&o_beosound_a1.jpg', 4, NOW(), NOW());

-- Giả sử các `id` của attribute đã được thêm vào trước đó, chúng ta giả định như sau:
-- 1 = Bộ vi xử lý, 2 = RAM, 3 = Bộ nhớ trong, 4 = Hệ điều hành
-- 5 = Kích thước màn hình, 6 = Công nghệ màn hình, 7 = Dung lượng pin, 8 = Công suất sạc, 9 = Công nghệ âm thanh

INSERT INTO product_attribute (product_id, attribute_id, value) VALUES 
-- Điện thoại
(1, 1, 'Apple A17 Pro'), (1, 2, '8GB'), (1, 3, '256GB'), (1, 4, 'iOS 17'), (1, 5, '6.1 inch'), (1, 6, 'Super Retina XDR'),
(2, 1, 'Snapdragon 8 Gen 2'), (2, 2, '12GB'), (2, 3, '512GB'), (2, 4, 'Android 13'), (2, 5, '6.8 inch'), (2, 6, 'Dynamic AMOLED'),
(3, 1, 'Snapdragon 8 Gen 2'), (3, 2, '12GB'), (3, 3, '256GB'), (3, 4, 'Android 13'), (3, 5, '6.7 inch'), (3, 6, 'AMOLED'),
(4, 1, 'Tensor G2'), (4, 2, '8GB'), (4, 3, '128GB'), (4, 4, 'Android 13'), (4, 5, '6.7 inch'), (4, 6, 'P-OLED'),
(5, 1, 'Snapdragon 8 Gen 2'), (5, 2, '16GB'), (5, 3, '512GB'), (5, 4, 'Android 13'), (5, 5, '6.7 inch'), (5, 6, 'AMOLED'),

-- Laptop
(11, 1, 'Apple M2'), (11, 2, '16GB'), (11, 3, '1TB'), (11, 4, 'macOS Ventura'), (11, 5, '16 inch'), (11, 6, 'Liquid Retina XDR'),
(12, 1, 'Intel Core i9'), (12, 2, '32GB'), (12, 3, '1TB'), (12, 4, 'Windows 11'), (12, 5, '15 inch'), (12, 6, 'OLED'),
(13, 1, 'AMD Ryzen 9 7945HS'), (13, 2, '32GB'), (13, 3, '1TB'), (13, 4, 'Windows 11'), (13, 5, '14 inch'), (13, 6, 'IPS LCD'),
(14, 1, 'Intel Core i7'), (14, 2, '16GB'), (14, 3, '512GB'), (14, 4, 'Windows 11'), (14, 5, '13.5 inch'), (14, 6, 'OLED'),

-- Tai nghe
(21, 9, 'Active Noise Cancelling, Spatial Audio'), (22, 9, 'LDAC, Hi-Res Audio'), (23, 9, 'Active Noise Cancelling'), 
(24, 9, 'aptX Adaptive, Hi-Res Audio'), (25, 9, 'Pure Bass Sound'),

-- Loa Bluetooth
(31, 7, '7500mAh'), (31, 8, '30W'), (31, 9, 'JBL Signature Sound'),
(32, 7, '10000mAh'), (32, 8, '40W'), (32, 9, 'Extra Bass'),
(33, 7, '8500mAh'), (33, 8, '35W'), (33, 9, '360-degree Sound'),
(34, 7, '4000mAh'), (34, 8, '20W'), (34, 9, 'Booming Bass');