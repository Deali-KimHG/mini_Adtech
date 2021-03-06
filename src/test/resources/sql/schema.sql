create table if not exists creative (
    id bigint generated by default as identity,
    created_date timestamp,
    updated_date timestamp,
    advertise_end_date timestamp,
    advertise_start_date timestamp,
    price bigint,
    status integer,
    title varchar(255),
    creative_count_id bigint,
    primary key (id)
);

create table if not exists creative_count (
    id bigint generated by default as identity,
    count bigint,
    primary key (id)
);

create table if not exists creative_image (
    id binary(16) default random_uuid() not null,
    extension varchar(255),
    name varchar(255),
    size bigint,
    creative_id bigint,
    primary key (id)
);

alter table creative
    add constraint if not exists FKpdeaf7bgm11nhfxc2fr5xcb3b
    foreign key (creative_count_id)
    references creative_count;

alter table creative_image
    add constraint if not exists FKs5dq7803f72wm181b2aglps3b
    foreign key (creative_id)
    references creative;