insert into creative_count (count)
values (0);
insert into creative_count (count)
values (0);
insert into creative_count (count)
values (0);
insert into creative_count (count)
values (0);
insert into creative_count (count)
values (0);

insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터1', 1, '2021-07-07T17:00', '2021-07-17T17:00', 0, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터2', 2, '2021-07-02T17:00', '2021-07-12T17:00', 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터3', 3, '2021-06-01T17:00', '2021-06-11T17:00', 2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터4', 4, '2021-06-02T17:00', '2021-06-12T17:00', 3, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터5', 5, '2021-07-02T17:00', '2021-07-12T17:00', 1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into creative_image (name, extension, size, creative_id)
values ('테스트데이터01.txt', 'txt', 20, 1);
insert into creative_image (name, extension, size, creative_id)
values ('테스트데이터02.txt', 'txt', 20, 2);
insert into creative_image (name, extension, size, creative_id)
values ('테스트데이터03.txt', 'txt', 20, 3);
insert into creative_image (name, extension, size, creative_id)
values ('테스트데이터04.txt', 'txt', 20, 4);
insert into creative_image (name, extension, size, creative_id)
values ('테스트데이터05.txt', 'txt', 20, 5);