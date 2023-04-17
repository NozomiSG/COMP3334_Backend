drop table if exists user;
drop table if exists estate;
drop table if exists transaction;

create table user
(
    user_id       int auto_increment,
    user_password varchar(15) null,
    user_email    varchar(30) null,
    private_key   varchar(2000) null,
    public_key    varchar(1000) null,

    constraint table_name_pk
        primary key (user_id)
);

create table estate
(
    estate_id           int auto_increment,
    estate_name         varchar(30) null,
    estate_description  varchar(200) null,
    estate_owner_id     int null,
    estate_price        int null,
    estate_status       boolean null default 0,

    constraint table_name_pk
        primary key (estate_id)
);

create table transaction
(
    trans_id            int auto_increment,
    estate_id           int null,
    buyer_id            int null,
    seller_id           int null,
    trans_time          timestamp null,
    signature           varchar(1200) null,
    trans_hash          varchar(100) null,
    trans_status        boolean null default 0,

        constraint table_name_pk
        primary key (trans_id)
);


insert into user (user_password, user_email) values
                                                            ('P@ssword1', 'zgy.peter@gmail.com'),
                                                            ('123*5678Aa', '1762513582@qq.com'),
                                                            ('zsBdg*e32regh', '912@comp3334.com'),
                                                            ('Aa!12345', 'megan.wu@connect.polyu.hk');


insert into estate (estate_name, estate_description, estate_owner_id, estate_price) values
                                                            ('Dragon Legend', 'Very expensive', 1, 10),
                                                            ('estate1', 'i dont know', 2, 30);

insert into estate (estate_name, estate_description, estate_owner_id, estate_price) value ('estate2', 'i dont know', 4, 30);
insert into estate (estate_name, estate_description, estate_owner_id, estate_price) value ('estate3', 'i dont know i dont know i dont know i dont know i dont know', 4, 20);
insert into estate (estate_name, estate_description, estate_owner_id, estate_price) value ('estate4', 'i dont know', 4, 40);
insert into estate (estate_name, estate_description, estate_owner_id, estate_price) value ('estate5646345', 'i dont know', 4, 50);

