insert into creative_count (count)
values (0);
insert into creative_count (count)
values (0);

insert into creative (title, price, exposure_start_date, exposure_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터1', 1, '2021-06-01T17:00', '2021-06-11T17:00', 0, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into creative (title, price, exposure_start_date, exposure_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터2', 2, '2021-06-02T17:00', '2021-06-12T17:00', 0, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into creative_image (name, extension, size, creative_id)
values ('first.txt', 'txt', 10, 1);
insert into creative_image (name, extension, size, creative_id)
values ('second.txt', 'txt', 10, 2);