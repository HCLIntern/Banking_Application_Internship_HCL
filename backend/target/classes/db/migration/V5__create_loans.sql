CREATE TABLE IF NOT EXISTS loans (
  id SERIAL PRIMARY KEY,
  user_id INTEGER NOT NULL REFERENCES users(id),
  loan_amount NUMERIC(19, 2) NOT NULL,
  principal_amount NUMERIC(19, 2) NOT NULL,
  interest_rate NUMERIC(5, 2) NOT NULL,
  loan_term_months INTEGER NOT NULL,
  emi_amount NUMERIC(19, 2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  start_date DATE,
  end_date DATE,
  disbursed_date DATE
);

CREATE INDEX idx_loans_user_id ON loans(user_id);
CREATE INDEX idx_loans_status ON loans(status);
