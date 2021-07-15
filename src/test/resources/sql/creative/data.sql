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
values ('테스트데이터1', 1, '2021-07-24T17:00', '2021-07-27T17:00', 0, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터2', 2, '2021-07-02T17:00', '2021-07-25T17:00', 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터3', 3, '2021-06-01T17:00', '2021-06-21T17:00', 2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터4', 4, '2021-07-02T17:00', '2021-07-22T17:00', 4, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터5', 5, '2021-07-02T17:00', '2021-07-14T17:00', 1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into creative_image (name, extension, size, creative_id)
values ('테스트데이터01.jpg', 'jpg', 20, 1);
insert into creative_image (name, extension, size, creative_id)
values ('테스트데이터02.jpg', 'jpg', 20, 2);
insert into creative_image (name, extension, size, creative_id)
values ('테스트데이터03.jpg', 'jpg', 20, 3);
insert into creative_image (name, extension, size, creative_id)
values ('테스트데이터04.jpg', 'jpg', 20, 4);
insert into creative_image (name, extension, size, creative_id)
values ('테스트데이터05.jpg', 'jpg', 20, 5);