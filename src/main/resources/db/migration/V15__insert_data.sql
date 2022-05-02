INSERT INTO
    users (id, username, password)
VALUES
    (
        1,
        'customer',
        '$2a$10$PDNnciwgm3rk2rELAsAH.u3g.oZUXG5afVJwb82VjwuLEJ9AZN9Cu'
    ),
    (
        2,
        'employee',
        '$2a$10$eIsOD9leuONH78iPgHUIKOHVLrGIh.XEewRDuiL5k/oOhBLdoJYju'
    ),
    (
        3,
        'owner',
        '$2a$10$KcpG3a8qCtv2sXxXRDKgzevP2DWbvylf9AMOP88v2p5p2SlbnAwnq'
    );

INSERT INTO
    roles (id, name)
VALUES
    (1, 'ROLE_CUSTOMER'),
    (2, 'ROLE_EMPLOYEE'),
    (3, 'ROLE_STORE_OWNER');

INSERT INTO
    user_roles (id, user_id, role_id)
VALUES
    (1, 1, 1),
    (2, 2, 2),
    (3, 3, 3);

INSERT INTO
    categories (id, name)
VALUES
    (1, 'base'),
    (2, 'stuffing'),
    (3, 'topping'),
    (4, 'fruit');

INSERT INTO
    ingredients (
        id,
        name,
        price,
        category_id,
        is_healthy
    )
VALUES
    (1, 'american_pancake', 15.55, 1, 0),
    (2, 'scotch_pancake', 22.70, 1, 0),
    (3, 'french_crepes', 12.30, 1, 1),
    (4, 'nutella', 9.85, 2, 0),
    (5, 'cheese', 13.80, 2, 1),
    (6, 'jam', 10.40, 2, 0),
    (7, 'healthy_jam', 12.50, 2, 1),
    (8, 'maple_syrup', 7.40, 3, 0),
    (9, 'chocolate', 8.70, 3, 0),
    (10, 'strawberry_syrup', 9.60, 3, 1),
    (11, 'blueberry_syrup', 11.30, 3, 1),
    (12, 'strawberry', 16.50, 4, 1),
    (13, 'blueberry', 18.20, 4, 1),
    (14, 'banana', 13.40, 4, 1),
    (15, 'berries', 16.40, 4, 1);
