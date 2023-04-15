drop table if exists user;
drop table if exists estate;

create table user
(
    user_id       int auto_increment,
    user_name     varchar(10) null,
    user_password varchar(15) null,
    user_email    varchar(20) null,
    private_key   varchar(300) null,
    public_key    varchar(300) null,

    #to be completed: encryption variables

    constraint table_name_pk
        primary key (user_id)
);

create table estate
(
    estate_id     int auto_increment,
    estate_name   varchar(30) null,
    estate_description  varchar(200) null,
    estate_owner_id  int null,
    estate_price int null,
    estate_image varchar(40) null,

    #to be completed: encryption variables

    constraint table_name_pk
        primary key (estate_id)
);


insert into user (user_name, user_password, user_email) values
                                                            ('user1', 'password1', 'zgy.peter@gmail.com'),
                                                            ('user2', '123456', '1762513582@qq.com'),
                                                            ('JohnXina', 'zsh', '912@comp3334.com'),
                                                            ('CarrieLam', '4r6', '777@comp3334.com'),
                                                            ('JohnLee', '54h', '896@comp3334.com'),
                                                            ('TungCH', 'f45', '845@comp3334.com'),
                                                            ('LeungCY', 'dc3', '344@comp3334.com');


insert into estate (estate_name, estate_description, estate_owner_id, estate_price) values
                                                            ('Dragon Legend', 'Very expensive', 1, 10),
                                                            ('estate1', 'i dont know', 2, 30);




