drop table if exists USERS;
create table USERS(
    id varchar (10) primary key,
    name varchar(20) not null,
    password varchar(10) not null
);