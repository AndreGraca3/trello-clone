drop table if exists card;
drop table if exists list;
drop table if exists user_board;
drop table if exists board;
drop table if exists "user";


select * from "user";
select * from board;
insert into board (name, description)
values ('Board1','this');
select * from user_board;

create table "user" (
    idUser serial primary key,
    email varchar(320) unique,
    name varchar(20),
    token varchar(36) unique
);

create table user_board (
    idUser Int,
    idBoard Int,
    primary key(idUser,idBoard)
);

create table board (
    idBoard serial primary key,
    name varchar(20) unique,
    description varchar(400)
);

create table list (
    idBoard Int,
    idList serial,
    name varchar(20),
    primary key (idBoard,idList),
    foreign key (idBoard) references board(idBoard)
);

create table card (
    idBoard Int,
    idList Int,
    idCard serial,
    startDate timestamp,
    endDate timestamp check ( endDate > card.startDate ),
    name varchar(20),
    description varchar(400),
    archived boolean,
    primary key (idBoard,idList,idCard),
    foreign key (idBoard,idList) references list(idBoard, idList)
);
