-- Create a new users table for basic user information
create table users (
                       id serial primary key,
                       username text not null unique,
                       email text not null unique,
                       created_at timestamp with time zone default now(),
                       updated_at timestamp
);

-- Create a user_logins table for login-related information
create table user_login (
                        id serial primary key,
                        user_id bigint references users (id) on delete cascade,
                        password_hash text,
                        auth_provider text not null, -- e.g., 'email', 'google', 'github'
                        auth_provider_id text, -- ID from the external provider, if applicable
                        created_at timestamp with time zone default now(),
                        updated_at timestamp
);

commit ;