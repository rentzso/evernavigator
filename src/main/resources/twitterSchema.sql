create user twitter identified by '';

create database twitterapi;
grant all on twitterapi.* to twitter;

use twitterapi;

create table users(
  username VARCHAR(50) NOT NULL PRIMARY KEY,
  realname VARCHAR(50),
  apikey VARCHAR(16)
  );
  
create table tweets(
  id INT NOT NULL PRIMARY KEY,
  username VARCHAR(50),
  text VARCHAR(300),
  date DATE
);

create table followers (
  username VARCHAR(50), 
  follower VARCHAR(50)
);

alter table followers
ADD CONSTRAINT pk_pair 
PRIMARY KEY (username, follower);

alter table tweets
ADD INDEX username(username);

insert into users values ('rentzso', 'renzo frigato', 'belloeimpossibil');
insert into users values ('juliavc', 'julia viladomat', 'bellatroppobella');
insert into users (username, apikey) values ('sconosciuto', 'echiloconoscelui');
insert into users (username, apikey) values ('papadirenzo', 'eilpapadirenzono');
insert into tweets values ( 1, 'rentzso', 'julia I love you :)', sysdate());
insert into tweets values ( 2, 'juliavc', 'renzo I love you :)', sysdate());
insert into tweets values ( 3, 'sconosciuto', 'renzo, julia who are they?', sysdate());
insert into tweets values ( 4, 'papadirenzo', 'sono il papa di renzo', sysdate());
insert into followers values ('rentzso', 'juliavc');
insert into followers values ('rentzso', 'papadirenzo');
insert into followers values ('juliavc', 'rentzso');

