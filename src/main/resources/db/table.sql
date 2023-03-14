create table user_tb(
    id int auto_increment primary key,
    username varchar(100) not null unique,
    password varchar not null,
    email varchar(100) not null,
    provider varchar(100) default 'me'
);