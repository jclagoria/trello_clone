-- Add index on the email column in users table
CREATE INDEX idx_users_email ON users(email);

commit ;