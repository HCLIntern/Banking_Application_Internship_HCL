INSERT INTO accounts (account_number, user_id, balance, currency)
VALUES
('ACC1001', 1, 10000.00, 'INR'),
('ACC1002', 2, 5000.00, 'INR')
ON CONFLICT DO NOTHING;
