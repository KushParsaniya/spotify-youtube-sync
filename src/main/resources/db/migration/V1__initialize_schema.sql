create table users
(
    user_id         serial primary key,
    username        varchar unique,
    password        varchar,
    role            varchar,
    spotify_user_id varchar,
    youtube_user_id varchar,
    created_at      timestamp
);

create table oauth2_apps
(
    app_id            serial primary key,
    app_name          varchar,
    auth_token_url    varchar,
    access_token_url  varchar,
    refresh_token_url varchar
);


create table user_tokens
(
    user_token_id serial primary key,
    access_token  varchar,
    refresh_token varchar,
    token_type    varchar,
    created_at    timestamp,
    expiry_at     timestamp,
    user_id       int references users (user_id),
    app_id        int references oauth2_apps (app_id)
);


create table clients
(
    id            serial primary key,
    client_id     varchar unique,
    client_secret varchar,
    app_id        int references oauth2_apps (app_id)
);


create table grant_types
(
    grant_type_id   serial primary key,
    grant_type_name varchar
);


create table clients_grant_types
(
    client_id     int references clients (id),
    grant_type_id int references grant_types (grant_type_id)
);

create table scopes
(
    scope_id   serial primary key,
    scope_name varchar,
    client_id  int references clients (id)
);


create table redirect_uris
(
    redirect_uri_id serial primary key,
    uri             varchar
);


create table clients_redirect_uris
(
    client_id       int references clients (id),
    redirect_uri_id int references redirect_uris (redirect_uri_id)
);