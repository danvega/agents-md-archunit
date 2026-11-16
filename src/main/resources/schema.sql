create table book (
    id     bigint auto_increment primary key,
    title  varchar(200)  not null,
    author varchar(200)  not null,
    price  decimal(10, 2) not null
);

create table book_order (
    id       bigint auto_increment primary key,
    book_id  bigint         not null references book (id),
    quantity int            not null,
    total    decimal(10, 2) not null
);
