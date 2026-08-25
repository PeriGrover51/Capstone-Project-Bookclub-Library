drop database if exists bookclub;
create database bookclub;
use bookclub;

create table user (
                      user_id int primary key auto_increment,
                      username text,
                      password text
);