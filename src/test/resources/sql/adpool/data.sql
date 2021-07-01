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

-- CreativeStatus.ADVERTISING, deleteAdPool()로 제거될 예정
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터01', 1, '2021-06-25T12:00', '2021-07-01T12:00', 1, 1, '2021-06-25T12:00', '2021-06-27T12:00');
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터02', 2, '2021-06-26T12:00', '2021-07-02T12:00', 1, 2, '2021-06-26T12:00', '2021-06-28T12:00');
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터03', 3, '2021-06-27T12:00', '2021-07-03T12:00', 1, 3, '2021-06-27T12:00', '2021-06-29T12:00');
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터04', 4, '2021-06-28T12:00', '2021-07-04T12:00', 1, 4, '2021-06-28T12:00', '2021-06-30T12:00');
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터05', 5, '2021-06-29T12:00', '2021-07-05T12:00', 1, 5, '2021-06-29T12:00', '2021-07-01T12:00');
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터06', 6, '2021-06-30T12:00', '2021-07-06T12:00', 1, 6, '2021-06-30T12:00', '2021-07-02T12:00');
-- CreativeStatus.WAITING, insertAdPool()로 들어갈 예정
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터07', 7, '2021-07-01T12:00', '2021-07-07T12:00', 0, 7, '2021-07-01T12:00', '2021-07-03T12:00');
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터08', 8, '2021-07-02T12:00', '2021-07-08T12:00', 0, 8, '2021-07-02T12:00', '2021-07-04T12:00');
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터09', 9, '2021-07-03T12:00', '2021-07-09T12:00', 0, 9, '2021-07-03T12:00', '2021-07-05T12:00');
insert into creative (title, price, advertise_start_date, advertise_end_date, status, creative_count_id, created_date, updated_date)
values ('테스트데이터10', 10, '2021-07-04T12:00', '2021-07-10T12:00', 0, 10, '2021-07-04T12:00', '2021-07-06T12:00');


insert into creative_image (name, extension, size, creative_id)
values ('image01.txt', 'txt', 10, 1);
insert into creative_image (name, extension, size, creative_id)
values ('image02.txt', 'txt', 10, 2);
insert into creative_image (name, extension, size, creative_id)
values ('image03.txt', 'txt', 10, 3);
insert into creative_image (name, extension, size, creative_id)
values ('image04.txt', 'txt', 10, 4);
insert into creative_image (name, extension, size, creative_id)
values ('image05.txt', 'txt', 10, 5);
insert into creative_image (name, extension, size, creative_id)
values ('image06.txt', 'txt', 10, 6);
insert into creative_image (name, extension, size, creative_id)
values ('image07.txt', 'txt', 10, 7);
insert into creative_image (name, extension, size, creative_id)
values ('image08.txt', 'txt', 10, 8);
insert into creative_image (name, extension, size, creative_id)
values ('image09.txt', 'txt', 10, 9);
insert into creative_image (name, extension, size, creative_id)
values ('image10.txt', 'txt', 10, 10);

