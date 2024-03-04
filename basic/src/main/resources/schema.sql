drop table if exists public.collection_message;
drop table if exists public.collection;
drop table if exists public.request;
drop table if exists public.message;
drop table if exists public.category;


create table if not exists public.users
(
    id bigint generated by default as identity not null primary key
    , name varchar(250) not null
    , email varchar(254) not null
    , constraint UQ_USER_EMAIL unique (email)
);

create table if not exists public.category
(
    id bigint generated by default as identity not null primary key
    , name varchar(50) not null
    , constraint UQ_CATEGORY_NAME unique (name)
);

create type if not exists message_status as enum('PENDING', 'PUBLISHED', 'CANCELED');
create table if not exists public.message
(
    id bigint generated by default as identity not null primary key
    , annotation varchar(2000)
    , category bigint references category(id) on delete restrict
    , description varchar(7000)
    , event_date /*eventDate*/ timestamp without time zone
    , lat float
    , lon float
    , paid bool
    , participant_limit /*participantLimit*/ int
    , request_moderation /*requestModeration*/ bool
    , title varchar(120)
    , created_on /*createdOn*/ timestamp without time zone default now()
    , published_on /*publishedOn*/ timestamp without time zone
    , initiator bigint references users(id) on delete cascade
    , state message_status not null default 'PENDING'
);

create type if not exists request_status as enum('PENDING', 'APPROVED', 'REJECTED', 'CANCELED', 'CONFIRMED');
create table if not exists public.request
(
    id bigint generated by default as identity not null primary key
    , message_id /*event*/ bigint not null references message(id) on delete cascade
    , user_id /*requester*/ bigint references users(id) on delete cascade
    , created_on /*created*/ timestamp without time zone default now()
    , status request_status not null default 'PENDING'
    , constraint UQ_REQUEST_USER unique(message_id, user_id)
);

create table if not exists public.collection
(
    id bigint generated by default as identity not null primary key
    , pinned bool not null
    , title varchar(50) not null
    , constraint UQ_COLLECTION_NAME unique(title)
);

create table if not exists public.collection_message
(
    id bigint generated by default as identity not null primary key
    , collection_id bigint not null references collection(id) on delete cascade
    , message_id bigint not null references message(id) on delete cascade
);
