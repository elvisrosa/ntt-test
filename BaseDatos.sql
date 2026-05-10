CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================
-- 1. CLIENT TABLE (Customer Service - Microservice 1)
-- ============================================================
CREATE TABLE IF NOT EXISTS client (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(20) NOT NULL CHECK (gender IN ('M', 'F', 'OTHER')),
    identification VARCHAR(30) NOT NULL UNIQUE,
    address VARCHAR(200) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    status BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Indexes for Client
CREATE INDEX IF NOT EXISTS idx_client_identification ON client(identification);
CREATE INDEX IF NOT EXISTS idx_client_status ON client(status);
CREATE INDEX IF NOT EXISTS idx_client_created_at ON client(created_at DESC);

COMMENT ON TABLE client IS 'Client entity that extends Person with banking credentials';
COMMENT ON COLUMN client.id IS 'UUID primary key';
COMMENT ON COLUMN client.status IS 'TRUE = Active, FALSE = Inactive';

-- ============================================================
-- 2. ACCOUNT TABLE (Account Service - Microservice 2)
-- ============================================================
CREATE TABLE IF NOT EXISTS account (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    account_number VARCHAR(30) NOT NULL UNIQUE,
    client_id UUID NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    initial_balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    current_balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    status BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT chk_initial_balance CHECK (initial_balance >= 0),
    CONSTRAINT chk_current_balance CHECK (current_balance >= 0)
);

-- Indexes for Account
CREATE INDEX IF NOT EXISTS idx_account_number ON account(account_number);
CREATE INDEX IF NOT EXISTS idx_account_client_id ON account(client_id);
CREATE INDEX IF NOT EXISTS idx_account_type ON account(account_type);
CREATE INDEX IF NOT EXISTS idx_account_status ON account(status);

COMMENT ON TABLE account IS 'Bank account associated with a client';
COMMENT ON COLUMN account.client_id IS 'References client.id (no FK for microservice independence)';
COMMENT ON COLUMN account.account_type IS 'AHORRO (Ahorros) or CORRIENTE (Corriente)';
COMMENT ON COLUMN account.status IS 'TRUE = Active, FALSE = Inactive';

-- ============================================================
-- 3. MOVEMENT TABLE (Transaction History)
-- ============================================================
CREATE TABLE IF NOT EXISTS movement (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    account_id UUID NOT NULL REFERENCES account(id) ON DELETE CASCADE,
    movement_type VARCHAR(20) NOT NULL,
    transaction_date TIMESTAMPTZ NOT NULL DEFAULT now(),
    amount DECIMAL(19, 2) NOT NULL,
    balance_after DECIMAL(19, 2) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT chk_amount_positive CHECK (amount > 0),
    CONSTRAINT chk_balance_after CHECK (balance_after >= 0)
);

-- Indexes for Movement
CREATE INDEX IF NOT EXISTS idx_movement_account_id ON movement(account_id);
CREATE INDEX IF NOT EXISTS idx_movement_type ON movement(movement_type);
CREATE INDEX IF NOT EXISTS idx_movement_transaction_date ON movement(transaction_date DESC);
CREATE INDEX IF NOT EXISTS idx_movement_account_date ON movement(account_id, transaction_date DESC);

COMMENT ON TABLE movement IS 'Transaction history for accounts';
COMMENT ON COLUMN movement.movement_type IS 'DEBIT (withdrawal/retiro) or CREDIT (deposit/depósito)';
COMMENT ON COLUMN movement.amount IS 'Always positive. Type determines operation';
COMMENT ON COLUMN movement.balance_after IS 'Account balance after this movement';

-- ============================================================
-- TRIGGERS: Auto-update updated_at
-- ============================================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = now();
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_client_updated_at
    BEFORE UPDATE ON client
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_account_updated_at
    BEFORE UPDATE ON account
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- ============================================================
-- FUNCTION: Calculate new balance
-- ============================================================
CREATE OR REPLACE FUNCTION calculate_new_balance(
    p_current_balance DECIMAL,
    p_movement_type VARCHAR,
    p_amount DECIMAL
)
RETURNS DECIMAL AS $$
BEGIN
    IF p_movement_type = 'CREDIT' THEN
        RETURN p_current_balance + p_amount;
    ELSIF p_movement_type = 'DEBIT' THEN
        IF p_current_balance < p_amount THEN
            RAISE EXCEPTION 'Saldo no disponible';
        END IF;
        RETURN p_current_balance - p_amount;
    ELSE
        RAISE EXCEPTION 'Invalid movement type: %', p_movement_type;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- ============================================================
-- SAMPLE DATA (Based on test cases from document)
-- ============================================================

-- Test Case 1: Creación de Usuarios/Clientes
INSERT INTO client (name, gender, identification, address, phone, password, status) VALUES
('Jose Lema', 'M', '1234567890', 'Otavalo sn y principal', '098254785', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true),
('Marianela Montalvo', 'F', '0987654321', 'Amazonas y NNUU', '097548965', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true),
('Juan Osorio', 'M', '1122334455', '13 junio y Equinoccial', '098874587', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true)
ON CONFLICT (identification) DO NOTHING;

-- Test Case 2: Creación de Cuentas
INSERT INTO account (account_number, client_id, account_type, initial_balance, current_balance, status)
SELECT
    '478758',
    c.id,
    'Ahorros',
    2000.00,
    2000.00,
    true
FROM client c WHERE c.identification = '1234567890'
ON CONFLICT (account_number) DO NOTHING;

INSERT INTO account (account_number, client_id, account_type, initial_balance, current_balance, status)
SELECT
    '225487',
    c.id,
    'Corriente',
    100.00,
    100.00,
    true
FROM client c WHERE c.identification = '0987654321'
ON CONFLICT (account_number) DO NOTHING;

INSERT INTO account (account_number, client_id, account_type, initial_balance, current_balance, status)
SELECT
    '495878',
    c.id,
    'Ahorros',
    0.00,
    0.00,
    true
FROM client c WHERE c.identification = '1122334455'
ON CONFLICT (account_number) DO NOTHING;

INSERT INTO account (account_number, client_id, account_type, initial_balance, current_balance, status)
SELECT
    '496825',
    c.id,
    'Ahorros',
    540.00,
    540.00,
    true
FROM client c WHERE c.identification = '0987654321'
ON CONFLICT (account_number) DO NOTHING;

-- Test Case 3: Nueva cuenta corriente para Jose Lema
INSERT INTO account (account_number, client_id, account_type, initial_balance, current_balance, status)
SELECT
    '585545',
    c.id,
    'Corriente',
    1000.00,
    1000.00,
    true
FROM client c WHERE c.identification = '1234567890'
ON CONFLICT (account_number) DO NOTHING;

-- ============================================================
-- USEFUL QUERIES FOR DEVELOPMENT
-- ============================================================

-- Query: Get client accounts with current balance
CREATE OR REPLACE VIEW v_client_accounts AS
SELECT
    c.id as client_id,
    c.name as client_name,
    c.identification,
    a.id as account_id,
    a.account_number,
    a.account_type,
    a.initial_balance,
    a.current_balance,
    a.status as account_status,
    (a.current_balance - a.initial_balance) as balance_difference
FROM client c
INNER JOIN account a ON c.id = a.client_id
WHERE c.status = true;

-- Query: Get account movements with running balance
CREATE OR REPLACE VIEW v_account_movements AS
SELECT
    m.id as movement_id,
    m.account_id,
    a.account_number,
    c.name as client_name,
    m.movement_type,
    m.amount,
    m.balance_after,
    m.transaction_date,
    m.description,
    a.account_type,
    a.current_balance
FROM movement m
INNER JOIN account a ON m.account_id = a.id
INNER JOIN client c ON a.client_id = c.id
ORDER BY m.transaction_date DESC;

-- Query: Account statement (for F4 report)
CREATE OR REPLACE FUNCTION get_account_statement(
    p_client_id UUID,
    p_start_date TIMESTAMPTZ,
    p_end_date TIMESTAMPTZ
)
RETURNS TABLE (
    account_number VARCHAR,
    account_type VARCHAR,
    current_balance DECIMAL,
    movement_date TIMESTAMPTZ,
    movement_type VARCHAR,
    amount DECIMAL,
    balance_after DECIMAL
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        a.account_number,
        a.account_type,
        a.current_balance,
        m.transaction_date,
        m.movement_type,
        m.amount,
        m.balance_after
    FROM account a
    LEFT JOIN movement m ON a.id = m.account_id
        AND m.transaction_date BETWEEN p_start_date AND p_end_date
    WHERE a.client_id = p_client_id
    ORDER BY a.account_number, m.transaction_date DESC;
END;
$$ LANGUAGE plpgsql;

-- ============================================================
-- PERFORMANCE MONITORING
-- ============================================================

-- Query to check index usage
CREATE OR REPLACE VIEW v_index_usage AS
SELECT
    schemaname,
    relname as tablename,
    indexrelname as indexname,
    idx_scan as index_scans,
    idx_tup_read as tuples_read,
    idx_tup_fetch as tuples_fetched
FROM pg_stat_user_indexes
ORDER BY idx_scan DESC;

-- ============================================================
-- GRANTS (Application User)
-- ============================================================
-- Uncomment and configure for production

-- CREATE USER banking_app WITH PASSWORD 'ChangeMe!2024';
-- GRANT CONNECT ON DATABASE account_db TO banking_app;
-- GRANT USAGE ON SCHEMA public TO banking_app;
-- GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO banking_app;
-- GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO banking_app;
-- GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO banking_app;

-- ============================================================
-- END OF SCHEMA
-- ============================================================

COMMENT ON DATABASE current_database() IS 'Banking Microservice - Account Service Database';
