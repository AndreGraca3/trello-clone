insert into dbo.courses(name) values ('LEIC');
insert into dbo.students(course, number, name) values (1, 12345, 'Alice');
insert into dbo.students(course, number, name) select cid as course, 12346 as number, 'Bob' as name from dbo.courses where name = 'LEIC';
commit;