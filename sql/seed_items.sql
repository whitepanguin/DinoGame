-- 사용 database 명시

INSERT INTO
    items (name, type, price, effect_description, effect_value)
VALUES
    ('체력 회복 아이템', 'heal', 500, '체력 20% 회복', 20.00),
    ('공격력 증가 아이템', 'attack', 500, '공격력 20% 증가', 20.00),
    ('랜덤 아이템', 'random', 1000, '70% 확률로 체력 전부 회복, 30% 확률로 절반 피해', 0.70);
