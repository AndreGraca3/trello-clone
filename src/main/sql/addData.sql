
INSERT INTO dbo.user (email, name, token) VALUES ('jose.tremocos@gmail.com', 'Jose', 'token123');
commit;

insert into user_board(iduser, idboard) values (1,2);


insert into board (name, description) values ('Board2','this is board2.');
commit;

insert into list (idboard, name) values (1, 'List2');

insert into card (idboard, idlist) values (1, 2);

insert into card (idboard, idlist, enddate, name, description, archived, idx) values (4, 2, null, 'Card1', 'this is a Card1.', false, 0);