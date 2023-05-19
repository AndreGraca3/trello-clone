select * from "user";
select * from board;
select * from user_board;
select * from list;
select * from card;

select b.name, b.description, l.idlist, l.name, c.idcard, c.name, c.idx, c.archived from board b
inner join list l on l.idboard = b.idboard
left outer join card c on c.idlist = l.idlist
where b.idboard = 1
order by l.idlist asc, c.idcard asc;

select * from card c
where idboard = 1 and c.archived and c.idlist is null;


select b.idboard, b.name, description, count(l.idlist) from board b
inner join user_board u on u.idboard = b.idboard
left outer join list l on b.idboard = l.idboard
where u.iduser = 1
group by b.idboard
order by b.idboard asc;


select u.iduser, u2.name, u2.email, u2.token, u2.avatar from user_board u
inner join "user" u2 on u2.iduser = u.iduser
where u.idboard = 1
LIMIT null OFFSET null;

insert into card(idboard, idlist, startdate, name, archived, idx)
values (1, null, '2023-05-18 16:40', 'CardDelete', false, 100);
