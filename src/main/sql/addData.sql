
INSERT INTO dbo.user (email, name, token, password, avatar) VALUES ('jose.tremocos@gmail.com', 'Jose', 'token123', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8','https://live.staticflickr.com/65535/52841364369_13521f6ef1_m.jpg');
commit;

insert into user_board(iduser, idboard) values (3,1);

insert into board (name, description) values ('Board1','this is board1.');
insert into board (name, description) values ('Board2','this is board2.');
commit;

insert into list (idboard, name) values (1, 'List2');

insert into card (idboard, idlist) values (1, 2);

insert into card (idboard, idlist, startdate, name, description, archived, idx) values (1, 1, '2023-10-20 17:15:00', 'Card3', 'this is a Card3.', false, 3);

insert into card(idboard, idlist, startdate, name, archived, idx)
values (1, null, '2023-05-18 16:40', 'CardDelete', false, 100);