drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start 1 increment 1;
create table if not exists movie_entity (id int8 not null, description varchar(2000) not null, name varchar(255) not null, rate float8 not null, year timestamp not null, primary key (id));
