-- ==============================
-- Insert mock data for Users
-- ==============================
INSERT INTO users(id, email, password, full_name, phone, username, address, provider, is_active, is_verify, is_locked, created_at, updated_at)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'user1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User One', '0901111111', 'user1', 'Address 1', 'LOCAL', true, true, false, NOW(), NOW()),
    ('22222222-2222-2222-2222-222222222222', 'user2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User Two', '0902222222', 'user2', 'Address 2', 'LOCAL', true, false, false, NOW(), NOW()),
    ('33333333-3333-3333-3333-333333333333', 'user3@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User Three', '0903333333', 'user3', 'Address 3', 'GOOGLE', true, true, false, NOW(), NOW()),
    ('44444444-4444-4444-4444-444444444444', 'user4@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User Four', '0904444444', 'user4', 'Address 4', 'FACEBOOK', true, false, false, NOW(), NOW()),
    ('55555555-5555-5555-5555-555555555555', 'user5@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'User Five', '0905555555', 'user5', 'Address 5', 'LOCAL', true, true, false, NOW(), NOW());

-- ==============================
-- Insert mock data for Roles
-- ==============================
INSERT INTO role(id, name, created_at, updated_at)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'USER', NOW(), NOW()),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'ADMIN', NOW(), NOW()),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'MANAGER', NOW(), NOW()),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'SUPPORT', NOW(), NOW()),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'GUEST', NOW(), NOW());

-- ==============================
-- Insert mock data for User_Roles
-- ==============================
INSERT INTO user_roles(user_id, role_id)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    ('22222222-2222-2222-2222-222222222222', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    ('22222222-2222-2222-2222-222222222222', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
    ('33333333-3333-3333-3333-333333333333', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    ('44444444-4444-4444-4444-444444444444', 'cccccccc-cccc-cccc-cccc-cccccccccccc');

-- ==============================
-- Insert mock data for Category
-- ==============================
INSERT INTO category(id, name, slug, created_at, updated_at)
VALUES
    ('10101010-1010-1010-1010-101010101010', 'Electronics', 'electronics', NOW(), NOW()),
    ('20202020-2020-2020-2020-202020202020', 'Clothing', 'clothing', NOW(), NOW()),
    ('30303030-3030-3030-3030-303030303030', 'Books', 'books', NOW(), NOW()),
    ('40404040-4040-4040-4040-404040404040', 'Home', 'home', NOW(), NOW()),
    ('50505050-5050-5050-5050-505050505050', 'Sports', 'sports', NOW(), NOW());

-- ==============================
-- Insert mock data for Product
-- ==============================
INSERT INTO product(id, category_id, name, slug, description, price, stock_quantity, is_active, created_at, updated_at)
VALUES
    ('11111111-aaaa-1111-aaaa-111111111111', '10101010-1010-1010-1010-101010101010', 'iPhone 15', 'iphone-15', 'Latest iPhone model', 999.99, 50, true, NOW(), NOW()),
    ('22222222-bbbb-2222-bbbb-222222222222', '10101010-1010-1010-1010-101010101010', 'Samsung Galaxy S23', 'samsung-galaxy-s23', 'Flagship Samsung phone', 899.99, 40, true, NOW(), NOW()),
    ('33333333-cccc-3333-cccc-333333333333', '20202020-2020-2020-2020-202020202020', 'T-shirt', 'tshirt', 'Comfortable cotton t-shirt', 19.99, 200, true, NOW(), NOW()),
    ('44444444-dddd-4444-dddd-444444444444', '30303030-3030-3030-3030-303030303030', 'Java Book', 'java-book', 'Learn Java programming', 49.99, 100, true, NOW(), NOW()),
    ('55555555-eeee-5555-eeee-555555555555', '50505050-5050-5050-5050-505050505050', 'Football', 'football', 'Official size football', 29.99, 150, true, NOW(), NOW());

-- ==============================
-- Insert mock data for Product_Image
-- ==============================
INSERT INTO product_image(id, product_id, image_url, public_id, is_primary, created_at, updated_at)
VALUES
    ('11111111-aaaa-1111-aaaa-111111111100', '11111111-aaaa-1111-aaaa-111111111111', 'https://example.com/images/iphone15.jpg', 'iphone15-main', true, NOW(), NOW()),
    ('22222222-bbbb-2222-bbbb-222222222200', '22222222-bbbb-2222-bbbb-222222222222', 'https://example.com/images/galaxy23.jpg', 'galaxy23-main', true, NOW(), NOW()),
    ('33333333-cccc-3333-cccc-333333333300', '33333333-cccc-3333-cccc-333333333333', 'https://example.com/images/tshirt.jpg', 'tshirt-main', true, NOW(), NOW()),
    ('44444444-dddd-4444-dddd-444444444400', '44444444-dddd-4444-dddd-444444444444', 'https://example.com/images/java-book.jpg', 'java-book-main', true, NOW(), NOW()),
    ('55555555-eeee-5555-eeee-555555555500', '55555555-eeee-5555-eeee-555555555555', 'https://example.com/images/football.jpg', 'football-main', true, NOW(), NOW());

-- ==============================
-- Insert mock data for Cart
-- ==============================
INSERT INTO cart(id, user_id, total_price, created_at, updated_at)
VALUES
    ('11111111-aaaa-1111-aaaa-111111111111', '11111111-1111-1111-1111-111111111111', 0, NOW(), NOW()),
    ('22222222-bbbb-2222-bbbb-222222222222', '22222222-2222-2222-2222-222222222222', 0, NOW(), NOW()),
    ('33333333-cccc-3333-cccc-333333333333', '33333333-3333-3333-3333-333333333333', 0, NOW(), NOW()),
    ('44444444-dddd-4444-dddd-444444444444', '44444444-4444-4444-4444-444444444444', 0, NOW(), NOW()),
    ('55555555-eeee-5555-eeee-555555555555', '55555555-5555-5555-5555-555555555555', 0, NOW(), NOW());

-- ==============================
-- Insert mock data for Cart_Item
-- ==============================
INSERT INTO cart_item(id, cart_id, product_id, quantity, price, created_at, updated_at)
VALUES
    ('11111111-aaaa-aaaa-aaaa-111111111111', '11111111-aaaa-1111-aaaa-111111111111', '11111111-aaaa-1111-aaaa-111111111111', 1, 999.99, NOW(), NOW()),
    ('22222222-bbbb-bbbb-bbbb-222222222222', '22222222-bbbb-2222-bbbb-222222222222', '22222222-bbbb-2222-bbbb-222222222222', 2, 899.99, NOW(), NOW()),
    ('33333333-cccc-cccc-cccc-333333333333', '33333333-cccc-3333-cccc-333333333333', '33333333-cccc-3333-cccc-333333333333', 3, 19.99, NOW(), NOW()),
    ('44444444-dddd-dddd-dddd-444444444444', '44444444-dddd-4444-dddd-444444444444', '44444444-dddd-4444-dddd-444444444444', 1, 49.99, NOW(), NOW()),
    ('55555555-eeee-eeee-eeee-555555555555', '55555555-eeee-5555-eeee-555555555555', '55555555-eeee-5555-eeee-555555555555', 4, 29.99, NOW(), NOW());
-- ==============================
-- Insert mock data for Orders
-- ==============================
INSERT INTO "orders"(id, user_id, order_code, total_amount, status, payment_method, shipping_address, paid_at, created_at, updated_at)
VALUES
    ('aaaaaaaa-aaaa-1111-aaaa-111111111111', '11111111-1111-1111-1111-111111111111', 'ORD-0001', 999.99, 'PAID', 'CREDIT_CARD', 'Address 1', NOW(), NOW(), NOW()),
    ('bbbbbbbb-bbbb-2222-bbbb-222222222222', '22222222-2222-2222-2222-222222222222', 'ORD-0002', 1799.98, 'PENDING', 'PAYPAL', 'Address 2', NULL, NOW(), NOW()),
    ('cccccccc-cccc-3333-cccc-333333333333', '33333333-3333-3333-3333-333333333333', 'ORD-0003', 19.99, 'PAID', 'CREDIT_CARD', 'Address 3', NOW(), NOW(), NOW()),
    ('dddddddd-dddd-4444-dddd-444444444444', '44444444-4444-4444-4444-444444444444', 'ORD-0004', 49.99, 'CANCELLED', 'CREDIT_CARD', 'Address 4', NULL, NOW(), NOW()),
    ('eeeeeeee-eeee-5555-eeee-555555555555', '55555555-5555-5555-5555-555555555555', 'ORD-0005', 29.99, 'PAID', 'PAYPAL', 'Address 5', NOW(), NOW(), NOW());

-- ==============================
-- Insert mock data for Order_Item
-- ==============================
INSERT INTO order_item(id, order_id, product_id, quantity, unit_price, created_at, updated_at)
VALUES
    ('aaaaaaaa-1111-aaaa-1111-aaaaaaaa1111', 'aaaaaaaa-aaaa-1111-aaaa-111111111111', '11111111-aaaa-1111-aaaa-111111111111', 1, 999.99, NOW(), NOW()),
    ('bbbbbbbb-2222-bbbb-2222-bbbbbbbb2222', 'bbbbbbbb-bbbb-2222-bbbb-222222222222', '22222222-bbbb-2222-bbbb-222222222222', 2, 899.99, NOW(), NOW()),
    ('cccccccc-3333-cccc-3333-cccccccc3333', 'cccccccc-cccc-3333-cccc-333333333333', '33333333-cccc-3333-cccc-333333333333', 3, 19.99, NOW(), NOW()),
    ('dddddddd-4444-dddd-4444-dddddddd4444', 'dddddddd-dddd-4444-dddd-444444444444', '44444444-dddd-4444-dddd-444444444444', 1, 49.99, NOW(), NOW()),
    ('eeeeeeee-5555-eeee-5555-eeeeeeee5555', 'eeeeeeee-eeee-5555-eeee-555555555555', '55555555-eeee-5555-eeee-555555555555', 4, 29.99, NOW(), NOW());

-- ==============================
-- Insert mock data for Payment
-- ==============================
INSERT INTO payment(id, order_id, provider, transaction_id, amount, status, paid_at, created_at, updated_at)
VALUES
    ('aaaaaaaa-aaaa-1111-aaaa-aaaaaaaa1111', 'aaaaaaaa-aaaa-1111-aaaa-111111111111', 'Stripe', 'txn-1111', 999.99, 'SUCCESS', NOW(), NOW(), NOW()),
    ('bbbbbbbb-bbbb-2222-bbbb-bbbbbbbb2222', 'bbbbbbbb-bbbb-2222-bbbb-222222222222', 'PayPal', 'txn-2222', 1799.98, 'PENDING', NULL, NOW(), NOW()),
    ('cccccccc-cccc-3333-cccc-cccccccc3333', 'cccccccc-cccc-3333-cccc-333333333333', 'Stripe', 'txn-3333', 19.99, 'SUCCESS', NOW(), NOW(), NOW()),
    ('dddddddd-dddd-4444-dddd-dddddddd4444', 'dddddddd-dddd-4444-dddd-444444444444', 'Stripe', 'txn-4444', 49.99, 'FAILED', NULL, NOW(), NOW()),
    ('eeeeeeee-eeee-5555-eeee-eeeeeeee5555', 'eeeeeeee-eeee-5555-eeee-555555555555', 'PayPal', 'txn-5555', 29.99, 'SUCCESS', NOW(), NOW(), NOW());

-- ==============================
-- Insert mock data for Refresh Token
-- ==============================
INSERT INTO refresh_token(id, user_id, token, exp_date, created_at, updated_at)
VALUES
    ('aaaaaaaa-1111-aaaa-1111-aaaaaaaa1111', '11111111-1111-1111-1111-111111111111', 'token1111', NOW() + INTERVAL '7 days', NOW(), NOW()),
    ('bbbbbbbb-2222-bbbb-2222-bbbbbbbb2222', '22222222-2222-2222-2222-222222222222', 'token2222', NOW() + INTERVAL '7 days', NOW(), NOW()),
    ('cccccccc-3333-cccc-3333-cccccccc3333', '33333333-3333-3333-3333-333333333333', 'token3333', NOW() + INTERVAL '7 days', NOW(), NOW()),
    ('dddddddd-4444-dddd-4444-dddddddd4444', '44444444-4444-4444-4444-444444444444', 'token4444', NOW() + INTERVAL '7 days', NOW(), NOW()),
    ('eeeeeeee-5555-eeee-5555-eeeeeeee5555', '55555555-5555-5555-5555-555555555555', 'token5555', NOW() + INTERVAL '7 days', NOW(), NOW());

-- ==============================
-- Insert mock data for Audit Log
-- ==============================
INSERT INTO audit_log(id, user_id, action, details, created_at, updated_at)
VALUES
    ('aaaaaaaa-1111-aaaa-1111-aaaaaaaa1111', '11111111-1111-1111-1111-111111111111', 'LOGIN', 'User logged in', NOW(), NOW()),
    ('bbbbbbbb-2222-bbbb-2222-bbbbbbbb2222', '22222222-2222-2222-2222-222222222222', 'LOGOUT', 'User logged out', NOW(), NOW()),
    ('cccccccc-3333-cccc-3333-cccccccc3333', '33333333-3333-3333-3333-333333333333', 'UPDATE_PROFILE', 'User updated profile', NOW(), NOW()),
    ('dddddddd-4444-dddd-4444-dddddddd4444', '44444444-4444-4444-4444-444444444444', 'CREATE_ORDER', 'User created orders', NOW(), NOW()),
    ('eeeeeeee-5555-eeee-5555-eeeeeeee5555', '55555555-5555-5555-5555-555555555555', 'RESET_PASSWORD', 'User reset password', NOW(), NOW());
