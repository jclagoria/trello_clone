== Method

This section outlines the technical design and architecture for the Trello clone.

=== Tech Stack
* **Front-end**: React with TypeScript and TailwindCSS for the user interface.
* **Back-end**: Java 21 with Spring Boot WebFlux for reactive programming.
* **Database**: PostgreSQL for data persistence.
* **Real-time collaboration**: WebSockets with Spring WebFlux to enable real-time updates across users.
* **Authentication**: JWT-based authentication using both email/password and OAuth (Google or others).

=== Architecture Overview

The application will follow a microservices architecture with the following key components:

1. **Front-End (React, TypeScript, TailwindCSS)**
   - A single-page application (SPA) built with React and TypeScript.
   - TailwindCSS for styling and maintaining a customizable, responsive UI.
   - WebSockets for real-time updates between users.
   - JWT will be used to handle user sessions and ensure secure communication between the client and server.
   - Authentication via email/password and OAuth using external providers like Google.

2. **Back-End (Spring Boot WebFlux, Java 21)**
   - A reactive, non-blocking API using Spring Boot WebFlux.
   - WebSocket endpoints for real-time collaboration.
   - JWT authentication integrated with email/password login and OAuth.
   - Task dependency logic implemented in the back-end to block/unblock tasks based on the status of other tasks.

3. **Database (PostgreSQL)**
   - PostgreSQL will store users, boards, lists, cards, task dependencies, comments, and memberships.
   - The schema separates user profile data from authentication information for flexibility and scalability.

=== Authentication

The authentication system provides two options:

1. **Email and Password Authentication**
   - Users can sign up and log in using an email address and password.
   - Passwords are securely hashed using industry-standard techniques (e.g., bcrypt).
   - Upon successful login, a JWT is issued, which the client uses for secure authorization in subsequent requests.

2. **OAuth Authentication (Google, etc.)**
   - Users can log in or sign up via external providers (e.g., Google).
   - OAuth token validation is performed, and if successful, a JWT is issued for session management.

=== Database Schema

The following schema includes the basic structure for users, authentication, boards, lists, cards, comments, and memberships.

```sql

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

-- Create the boards table to store board information
create table boards (
  id serial primary key,
  name text not null,
  description text,
  created_at timestamp with time zone default now(),
  owner_id bigint references users (id) on delete cascade
);

-- Create the lists table to store lists within boards
create table lists (
  id bigint primary key generated always as identity,
  name text not null,
  position integer not null,
  board_id bigint references boards (id) on delete cascade
);

-- Create the cards table to store cards within lists
create table cards (
  id bigint primary key generated always as identity,
  title text not null,
  description text,
  position integer not null,
  list_id bigint references lists (id) on delete cascade
);

-- Create the comments table to store comments on cards
create table comments (
  id bigint primary key generated always as identity,
  content text not null,
  created_at timestamp with time zone default now(),
  card_id bigint references cards (id) on delete cascade,
  user_id bigint references users (id) on delete cascade
);

-- Create the memberships table to manage board memberships
create table memberships (
  id bigint primary key generated always as identity,
  user_id bigint references users (id) on delete cascade,
  board_id bigint references boards (id) on delete cascade,
  role text not null
);

-- Create a table to store refresh tokens
CREATE TABLE refresh_tokens (
  id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  token TEXT NOT NULL,
  user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
  issued_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  expires_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create a table to store blacklisted access tokens
CREATE TABLE blacklisted_tokens (
  id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  token TEXT NOT NULL,
  blacklisted_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
