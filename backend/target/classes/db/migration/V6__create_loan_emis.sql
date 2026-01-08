CREATE TABLE IF NOT EXISTS loan_emis (
  id SERIAL PRIMARY KEY,
  loan_id INTEGER NOT NULL REFERENCES loans(id),
  emi_number INTEGER NOT NULL,
  due_date DATE NOT NULL,
  amount NUMERIC(19, 2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  paid_date DATE
);

CREATE INDEX idx_loan_emis_loan_id ON loan_emis(loan_id);
CREATE INDEX idx_loan_emis_status ON loan_emis(status);
