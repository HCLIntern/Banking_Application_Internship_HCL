INSERT INTO users (name, email, password, role)
VALUES
('Admin User', 'admin@test.com', 'admin123', 'admin'),
('Test User', 'user@test.com', 'user123', 'customer')
ON CONFLICT DO NOTHING;
