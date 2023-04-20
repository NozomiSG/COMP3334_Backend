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
    trans_price         int null,
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
                                                        ('zsBdg*e32regh', '912@comp3334.com');

insert into user (user_password, user_email, private_key, public_key) value ('Aa!12345' ,'wumeijia2019@hotmail.com', 'MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDCzoOoHK+cAnBx4jC4RDA4mEumRTAOXu6Li2KyhmDVoh+XmjsIB7RalFXWnZbD2qnEitHBw1VrSgN7Sv4HyK2Aj8HPXnHH3xlfdTOzSN9di3jKu6cHrURhZdUtLcNjaXzdgKr/KXVY7UIBMwTeZa12mzjBpa1XT/or2Lq4veX9FKcxT8CAOnUL4dlUWcMiVR2Hj78DJIwrarIeCj1KpMcgqq7eoQgqNj0dZt7/+38CF0mNGizE6lPX4wSw3P/TDBTsmxDLbDt663jTBBYgYLx7+//wTLqXAduXRiDHLe14DNLum+EAyqv1OwLv6CAlDr2MTCx0tRsSoeb6B/vxjDJlAgMBAAECggEAInOf3I4QL4zN4pIgUCI9DfE/Jb1kMZJv6Q1iS9TQkM+Oe0L7MCEesAs+QAmYNXR5JClxzaz0MRiCk54wXWOL7ane2oPnJvQpelSRq2IWoKZPGzmDIWwu5PO7tvT6Ml1gSBMeiqhEGNFC5E/ny0Ybnpd2eubISGH8Sh9W7F+nKjHajpIgnqRoEaF3lE3tyabLmRfb3HPQkRuBEEvtio9TFaVWP7TTzoTnA3uYMvYC12OrYzIn6lz8EiNUN4VZcAOIr7OAZWjBANiFbT92hGqlZQlfO0crthoXrAtrWjPKTb1JwZX8k2EZ84mt7ogcecR8TbAAe5rVCgedWp+7MBEJ0QKBgQD7tl22X6SoMjMdnm3Rs67E5i59KD6PPSva3GcDA0QCr/61xZGMDJiwAXY/YQmmJNijZWenUn+3Rk0jSrPMG614N5lIufac0Bs8X0lc+F/hsI8n2hCA2FySkxbbim3zqXZmTGwnsjNgk4VjP00VX+l+feVOM1R8N/F901YrHJULFQKBgQDGIABjAGXEJaKf6FBto8Hmga9tD/zGwdZAqwMKmmhx8aBDcY1wTWAGki14fG8PZ/REyQBqf+5CoCSzntYJsTgFUzGWe9GSdjB4lFVAuJzArA4Tv1sqN3gBWEUVjDITB8DtJxIYazqlgyWobo60vemlpiXAGRgG1C5RWLiRUXIeEQKBgDiCW42K8VNVWcZnf+4ZMb5AkuVEOK89o2/SFUpG3GjiDAPQ4/kWBt1UprBZ36muEfi01k1pJwUFQmIO7kh/WjVn/gCmxyezTLoEBltZ42Sul6Txvsy+RiId5X7KGB8gx1T2CT3sMr6Wv9ZlKrAGxUvBhc5Flc0BXjTKUkkBdLB1AoGAZuvm54ckVpHqekFXzqz8JH+IHxLoEGRPpcZwzQhLa63GwGJzxDwdvywevPF/TDSHe1gjhC+zGR9SEZFYxS4v2OxP3UmzI++L3IfKBdYhcCGxKmcEt9ia2a/+K2Rp+uLTQR9D0jWaALMxU98i+ReTEb9O6wzeI2YELbP2JV929dECgYBueaJMCrtW1whIzQDUrbWDdBYGhSkm55tn2PCKg1pb50WcXWll9pXc0Sw75FTIDqpF1qb8FzlzGVtLJquAU7u1Mf+Vf790SwgKXhfvIgzEzI6Juci/q8L2IamgkkNUB1BFoS8wcvLNw7B9UFlCOA76FxAj+77fx0JA1wNcnqsyKQ==', 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAws6DqByvnAJwceIwuEQwOJhLpkUwDl7ui4tisoZg1aIfl5o7CAe0WpRV1p2Ww9qpxIrRwcNVa0oDe0r+B8itgI/Bz15xx98ZX3Uzs0jfXYt4yrunB61EYWXVLS3DY2l83YCq/yl1WO1CATME3mWtdps4waWtV0/6K9i6uL3l/RSnMU/AgDp1C+HZVFnDIlUdh4+/AySMK2qyHgo9SqTHIKqu3qEIKjY9HWbe//t/AhdJjRosxOpT1+MEsNz/0wwU7JsQy2w7eut40wQWIGC8e/v/8Ey6lwHbl0Ygxy3teAzS7pvhAMqr9TsC7+ggJQ69jEwsdLUbEqHm+gf78YwyZQIDAQAB');

insert into estate (estate_name, estate_description, estate_owner_id, estate_price) values
                                                            ('One Shenzhen Bay', 'Very expensive!', 4, 100),
                                                            ('Discovery Bay', 'The chillest place in Hong Kong', 4, 80);

insert into estate (estate_name, estate_description, estate_owner_id, estate_price) values
                                                                                        ('estate1', 'Cheap', 3, 20),
                                                                                        ('estate2', 'Expensive', 3, 60),
                                                                                        ('estate3', 'i dont know', 1, 40),
                                                                                        ('estate4', 'i dont know', 2, 50),
                                                                                        ('estate5', 'hahahahaha', 3, 50);

