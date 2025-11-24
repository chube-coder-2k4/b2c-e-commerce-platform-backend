-- V1__initial_schema.sql

-- Create users table
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(255),
                       phone VARCHAR(20),
                       username VARCHAR(150) UNIQUE,
                       address VARCHAR(255),
                       provider VARCHAR(50),
                       is_active BOOLEAN DEFAULT true,
                       is_verify BOOLEAN DEFAULT false,
                       is_locked BOOLEAN DEFAULT false,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL,
                       created_by UUID,
                       updated_by UUID
);


-- Create role table
CREATE TABLE role (
                      id UUID PRIMARY KEY,
                      name VARCHAR(100) NOT NULL UNIQUE,
                      description TEXT,
                      created_at TIMESTAMP NOT NULL,
                      updated_at TIMESTAMP NOT NULL,
                      created_by UUID,
                      updated_by UUID
);

-- Create user_roles junction table
CREATE TABLE user_roles (
                            user_id UUID NOT NULL,
                            role_id UUID NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);

-- Create refresh_token table
CREATE TABLE refresh_token (
                               id UUID PRIMARY KEY,
                               user_id UUID NOT NULL,
                               token VARCHAR(500) NOT NULL UNIQUE,
                               exp_date TIMESTAMP NOT NULL,
                               created_at TIMESTAMP NOT NULL,
                               updated_at TIMESTAMP NOT NULL,
                               created_by UUID,
                               updated_by UUID,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


-- Create category table
CREATE TABLE category (
                          id UUID PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          slug VARCHAR(255) NOT NULL UNIQUE,
                          is_active BOOLEAN DEFAULT true,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,
                          created_by UUID,
                          updated_by UUID
);

-- Create product table
CREATE TABLE product (
                         id UUID PRIMARY KEY,
                         category_id UUID,
                         name VARCHAR(255) NOT NULL,
                         slug VARCHAR(255) NOT NULL UNIQUE,
                         description TEXT,
                         price DECIMAL(15, 2) NOT NULL,
                         stock_quantity INTEGER NOT NULL DEFAULT 0,
                         is_active BOOLEAN DEFAULT true,
                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP NOT NULL,
                         created_by UUID,
                         updated_by UUID,
                         FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL
);

-- Create product_image table
CREATE TABLE product_image (
                               id UUID PRIMARY KEY,
                               product_id UUID NOT NULL,
                               image_url VARCHAR(500) NOT NULL,
                               public_id VARCHAR(255),
                               is_primary BOOLEAN DEFAULT false,
                               created_at TIMESTAMP NOT NULL,
                               updated_at TIMESTAMP NOT NULL,
                               created_by UUID,
                               updated_by UUID,
                               FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

-- Create cart table
CREATE TABLE cart (
                      id UUID PRIMARY KEY,
                      user_id UUID NOT NULL,
                      total_price DOUBLE PRECISION NOT NULL DEFAULT 0,
                      created_at TIMESTAMP NOT NULL,
                      updated_at TIMESTAMP NOT NULL,
                      created_by UUID,
                      updated_by UUID,
                      FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create cart_item table
CREATE TABLE cart_item (
                           id UUID PRIMARY KEY,
                           cart_id UUID NOT NULL,
                           product_id UUID NOT NULL,
                           quantity INTEGER NOT NULL DEFAULT 1,
                           price DOUBLE PRECISION NOT NULL,
                           created_at TIMESTAMP NOT NULL,
                           updated_at TIMESTAMP NOT NULL,
                           created_by UUID,
                           updated_by UUID,
                           FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE,
                           FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

-- Create order table
CREATE TABLE "order" (
                         id UUID PRIMARY KEY,
                         user_id UUID NOT NULL,
                         order_code VARCHAR(100) NOT NULL UNIQUE,
                         total_amount DOUBLE PRECISION NOT NULL,
                         status VARCHAR(50) NOT NULL,
                         payment_method VARCHAR(50) NOT NULL,
                         shipping_address TEXT NOT NULL,
                         paid_at TIMESTAMP,
                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP NOT NULL,
                         created_by UUID,
                         updated_by UUID,
                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create order_item table
CREATE TABLE order_item (
                            id UUID PRIMARY KEY,
                            order_id UUID NOT NULL,
                            product_id UUID NOT NULL,
                            quantity INTEGER NOT NULL,
                            unit_price DOUBLE PRECISION NOT NULL,
                            created_at TIMESTAMP NOT NULL,
                            updated_at TIMESTAMP NOT NULL,
                            created_by UUID,
                            updated_by UUID,
                            FOREIGN KEY (order_id) REFERENCES "order"(id) ON DELETE CASCADE,
                            FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

-- Create payment table
CREATE TABLE payment (
                         id UUID PRIMARY KEY,
                         order_id UUID NOT NULL,
                         provider VARCHAR(50) NOT NULL,
                         transaction_id VARCHAR(255) UNIQUE,
                         amount DOUBLE PRECISION NOT NULL,
                         status VARCHAR(50) NOT NULL,
                         paid_at TIMESTAMP,
                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP NOT NULL,
                         created_by UUID,
                         updated_by UUID,
                         FOREIGN KEY (order_id) REFERENCES "order"(id) ON DELETE CASCADE
);

-- Create audit_log table
CREATE TABLE audit_log (
                           id UUID PRIMARY KEY,
                           user_id UUID,
                           action VARCHAR(255) NOT NULL,
                           details TEXT,
                           created_at TIMESTAMP NOT NULL,
                           updated_at TIMESTAMP NOT NULL,
                           created_by UUID,
                           updated_by UUID,
                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Create indexes for performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_product_slug ON product(slug);
CREATE INDEX idx_product_category ON product(category_id);
CREATE INDEX idx_cart_user ON cart(user_id);
CREATE INDEX idx_order_user ON "order"(user_id);
CREATE INDEX idx_order_code ON "order"(order_code);
CREATE INDEX idx_order_status ON "order"(status);
CREATE INDEX idx_payment_order ON payment(order_id);
CREATE INDEX idx_payment_transaction ON payment(transaction_id);
CREATE INDEX idx_audit_log_user ON audit_log(user_id);
CREATE INDEX idx_refresh_token_user ON refresh_token(user_id);
