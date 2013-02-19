create user 'evernote'@'localhost' identified by 'evernote';

create database evernavigator;
grant all on evernavigator.* to evernote;

use evernavigator;

create table users(
  username VARCHAR(50) NOT NULL PRIMARY KEY,
  userid INT NOT NULL,
  hashkey VARCHAR(32) NOT NULL,
  salts VARCHAR(60) NOT NULL,
  accessToken VARCHAR(100) NOT NULL,
  expirationDate DATE NOT NULL,
  userAuthority VARCHAR(100) NOT NULL,
  email VARCHAR(30)
  );
