
INSERT INTO dbo.user (email, name, token) VALUES ('jose.tremocos@gmail.com', 'Jose', 'token123');
commit;


insert into board (name, description) values ('Board1','this');
commit;

insert into list (idboard, name) values (1, 'List2');

insert into card (idboard, idlist) values (1, 2);