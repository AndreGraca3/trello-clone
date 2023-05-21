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

--getBoards
SELECT b.idboard, b.name, b.description, COUNT(l.idlist) AS numLists
FROM dbo.board b
         INNER JOIN dbo.user_board u ON u.idboard = b.idboard
         LEFT OUTER JOIN dbo.list l ON l.idboard = b.idboard
WHERE u.iduser = 1 AND b.name LIKE '%Boa%'
GROUP BY b.idboard, b.name, b.description
HAVING COUNT(l.idlist) >= 0
ORDER BY b.idboard ASC
LIMIT 4 OFFSET 1;

--getBoardsCount
SELECT COUNT(*) AS count
FROM (
         SELECT DISTINCT b.idBoard
         FROM dbo.board b
                  INNER JOIN dbo.user_board ub ON ub.idBoard = b.idBoard
                  LEFT JOIN dbo.list l ON l.idBoard = b.idBoard
         WHERE ub.idUser = 1 AND b.name LIKE '%Bo%'
         GROUP BY b.idBoard
         HAVING COUNT(l.idList) >= 0
    ) AS subquery;

-- getBoard
SELECT b.name, b.description, l.idlist, l.name AS listName, c.idcard, c.name AS cardName, c.idx, c.archived
FROM dbo.board b
INNER JOIN dbo.list l on l.idboard = b.idboard
LEFT OUTER JOIN dbo.card c ON c.idlist = l.idlist
WHERE b.idboard = 30
ORDER BY l.idlist ASC, c.idcard ASC;

-- getArchivedCards
SELECT * FROM dbo.card where card.idList is null and card.idBoard = 2;