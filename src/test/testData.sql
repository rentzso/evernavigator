insert into users values ('rentzso', 'renzo frigato', 'e9189acf445018463257bdafdfc7e9ff', '-93,81,-102,-75,-110,-26,-66,69,85,-32,-109,-99');
insert into users values ('juliavc', 'julia viladomat', 'cf73f1c9d9ae8429c02d9c09b132bf8e', '43,101,-90,71,-54,10,-121,-128,-22,53,-74,-17');
insert into users (username, hashkey, salts) values ('sconosciuto', 'bc5ccaeab2c685e6ce7b4fa39ec097d8', '-18,-97,-31,-45,-64,90,109,47,-61,44,113,18');
insert into users (username, hashkey, salts) values ('papadirenzo', '7f0b80e87097719e580a66d479bbb8e4', '14,22,49,-76,-36,-14,-33,63,84,35,-107,-90');
insert into tweets values ( 1, 'rentzso', 'julia I love you :)', sysdate());
insert into tweets values ( 2, 'juliavc', 'renzo I love you :)', sysdate());
insert into tweets values ( 3, 'sconosciuto', 'renzo, julia who are they?', sysdate());
insert into tweets values ( 4, 'papadirenzo', 'I am renzo\'s father', sysdate());
--insert into followers values ('rentzso', 'juliavc');
--insert into followers values ('rentzso', 'papadirenzo');
--insert into followers values ('juliavc', 'rentzso');

-- belloeimpossibil, e9189acf445018463257bdafdfc7e9ff, -93,81,-102,-75,-110,-26,-66,69,85,-32,-109,-99
-- bellatroppobella, cf73f1c9d9ae8429c02d9c09b132bf8e, 43,101,-90,71,-54,10,-121,-128,-22,53,-74,-17
-- echiloconoscelui, bc5ccaeab2c685e6ce7b4fa39ec097d8, -18,-97,-31,-45,-64,90,109,47,-61,44,113,18
-- eilpapadirenzono, 7f0b80e87097719e580a66d479bbb8e4, 14,22,49,-76,-36,-14,-33,63,84,35,-107,-90