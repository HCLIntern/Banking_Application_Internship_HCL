CREATE TABLE IF NOT EXISTS assurance_policies (
  id SERIAL PRIMARY KEY,
  user_id INTEGER NOT NULL REFERENCES users(id),
  policy_number VARCHAR(50) NOT NULL UNIQUE,
  product_name VARCHAR(200),
  premium_amount NUMERIC(19, 2) NOT NULL,
  coverage_amount NUMERIC(19, 2) NOT NULL,
  start_date DATE NOT NULL,
  expiry_date DATE NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

CREATE INDEX idx_assurance_user_id ON assurance_policies(user_id);
CREATE INDEX idx_assurance_policy_number ON assurance_policies(policy_number);
