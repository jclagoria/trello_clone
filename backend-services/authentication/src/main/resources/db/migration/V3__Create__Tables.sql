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
  id serial primary key,
  name text not null,
  position integer not null,
  board_id bigint references boards (id) on delete cascade
);

commit ;