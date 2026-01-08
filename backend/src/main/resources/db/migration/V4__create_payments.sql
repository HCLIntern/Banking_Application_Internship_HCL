CREATE TABLE IF NOT EXISTS payments (
  id SERIAL PRIMARY KEY,
  account_id INTEGER NOT NULL REFERENCES accounts(id),
  amount NUMERIC(19, 2) NOT NULL,
  description VARCHAR(500),
  payment_date TIMESTAMP NOT NULL,
  status VARCHAR(20) NOT NULL,
  reference_id VARCHAR(100),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_payments_account_id ON payments(account_id);
CREATE INDEX idx_payments_status ON payments(status);
