drop table if exists user;
drop table if exists estate;

create table user
(
    user_id       int auto_increment,
    user_password varchar(15) null,
    user_email    varchar(30) null,
    private_key   varchar(1200) null,
    public_key    varchar(1200) null,

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

    constraint table_name_pk
        primary key (estate_id)
);

create table transaction
(
    trans_id     int auto_increment,
    trans_estate_id int null,
    trans_buyer_id  int null,
    trans_seller_id  int null,
    trans_time    timestamp null,

        constraint table_name_pk
        primary key (trans_id)
);


insert into user (user_password, user_email) values
                                                            ('P@ssword1', 'zgy.peter@gmail.com'),
                                                            ('123*5678Aa', '1762513582@qq.com'),
                                                            ('zsBdg*e32regh', '912@comp3334.com');


insert into estate (estate_name, estate_description, estate_owner_id, estate_price) values
                                                            ('Dragon Legend', 'Very expensive', 1, 10),
                                                            ('estate1', 'i dont know', 2, 30);




