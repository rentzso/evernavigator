create user 'twitter'@'localhost' identified by '';

create database twitterapi;
grant all on twitterapi.* to twitter;

use twitterapi;

create table users(
  username VARCHAR(50) NOT NULL PRIMARY KEY,
  realname VARCHAR(50),
  hashkey VARCHAR(32),
  salts VARCHAR(60)
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
primary KEY (username, follower);

alter table tweets
ADD INDEX username(username);

alter table followers
add index follower(follower);

alter table tweets
add index username(username);
