create table attribute_group
(
    id   int auto_increment
        primary key,
    name varchar(255) null,
    constraint name
        unique (name)
);

create table attribute
(
    id       int auto_increment
        primary key,
    group_id int          null,
    name     varchar(255) null,
    constraint attribute_ibfk_1
        foreign key (group_id) references attribute_group (id)
            on delete cascade
);

create index group_id
    on attribute (group_id);

create table categories
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null
);

create table products
(
    id          bigint auto_increment
        primary key,
    created_at  datetime(6)  null,
    updated_at  datetime(6)  null,
    description varchar(255) null,
    name        varchar(255) null,
    price       int          not null,
    thumbnail   varchar(255) null,
    category_id bigint       null,
    constraint FKog2rp4qthbtt2lfyhfo32lsw9
        foreign key (category_id) references categories (id)
);

create table product_attribute
(
    id           int auto_increment
        primary key,
    product_id   bigint       null,
    attribute_id int          null,
    value        varchar(255) null,
    constraint product_attribute_ibfk_1
        foreign key (product_id) references products (id)
            on delete cascade,
    constraint product_attribute_ibfk_2
        foreign key (attribute_id) references attribute (id)
            on delete cascade
);

create index attribute_id
    on product_attribute (attribute_id);

create index product_id
    on product_attribute (product_id);

create table product_images
(
    id         bigint auto_increment
        primary key,
    image_url  varchar(300) null,
    product_id bigint       null,
    constraint FKqnq71xsohugpqwf3c9gxmsuy
        foreign key (product_id) references products (id)
);

create table roles
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null
);

create table users
(
    id            bigint auto_increment
        primary key,
    created_at    datetime(6)  null,
    updated_at    datetime(6)  null,
    address       varchar(255) null,
    date_of_birth date         null,
    is_active     bit          null,
    password      varchar(255) null,
    phone_number  varchar(255) null,
    role_id       bigint       null,
    email         varchar(255) null,
    name          varchar(255) null,
    is_verified   bit          not null,
    constraint FKp56c1712k691lhsyewcssf40f
        foreign key (role_id) references roles (id)
);


create table cart
(
    id         bigint auto_increment
        primary key,
    quantity   int    not null,
    product_id bigint null,
    user_id    bigint null,
    constraint FKg5uhi8vpsuy0lgloxk2h4w5o6
        foreign key (user_id) references users (id),
    constraint FKpu4bcbluhsxagirmbdn7dilm5
        foreign key (product_id) references products (id)
);

create table orders
(
    id               bigint auto_increment
        primary key,
    active           bit          null,
    order_date       datetime(6)  null,
    payment_method   varchar(255) null,
    shipping_address varchar(255) null,
    shipping_date    date         null,
    shipping_method  varchar(255) null,
    status           varchar(255) null,
    total_money      float        null,
    tracking_number  varchar(255) null,
    user_id          bigint       null,
    order_code       varchar(255) null,
    constraint FK32ql8ubntj5uh44ph9659tiih
        foreign key (user_id) references users (id)
);

create table order_details
(
    id          bigint auto_increment
        primary key,
    price       float  null,
    quantity    int    null,
    total_money float  null,
    order_id    bigint null,
    product_id  bigint null,
    constraint FK4q98utpd73imf4yhttm3w0eax
        foreign key (product_id) references products (id),
    constraint FKjyu2qbqt8gnvno9oe9j2s2ldk
        foreign key (order_id) references orders (id)
);


