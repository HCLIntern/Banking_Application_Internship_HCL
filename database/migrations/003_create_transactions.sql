CREATE TABLE IF NOT EXISTS transactions (
  id SERIAL PRIMARY KEY,
  from_account INTEGER REFERENCES accounts(id),
  to_account INTEGER REFERENCES accounts(id),
  amount NUMERIC(18,2),
  type VARCHAR(30),
  status VARCHAR(30),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
