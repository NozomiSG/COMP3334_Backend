drop table if exists user;
drop table if exists product;

create table user
(
    user_id       int auto_increment,
    user_name     varchar(10) null,
    user_password varchar(15) null,
    user_email    varchar(20) null,

    #to be completed: encryption variables

    constraint table_name_pk
        primary key (user_id)
);

insert into user (user_name, user_password, user_email) values
                ('user1', 'password1', 'zgy.peter@gmail.com'),
                ('user2', '123456', '1762513582@qq.com'),
                ('JohnXina', 'zsh', '912@comp3334.com'),
                ('CarrieLam', '4r6', '777@comp3334.com'),
                ('JohnLee', '54h', '896@comp3334.com'),
                ('TungCH', 'f45', '845@comp3334.com'),
                ('LeungCY', 'dc3', '344@comp3334.com');

create table product
(
    product_id  int auto_increment,
    product_name    varchar(10) null,
    product_description varchar(300) null,

    #to be completed: encryption variables

    constraint table_name_pk
        primary key (product_id)
)

