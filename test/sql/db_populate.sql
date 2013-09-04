insert into users (userid) values ('blast_hardcheese');
insert into users (userid) values ('thick_mcrunfast');
insert into users (userid) values ('big_mclargehuge');

insert into thread (created, shortTitle, random, title, userid) values ('2013-09-04 16:25:02+09', 'First_thread!_Hopefully_this_will_actually_work_c', 1234, 'First thread! Hopefully this will actually work correctly.', 'blast_hardcheese');
insert into post (thread_created, thread_sTitle, thread_random, posted, body, userid) values (
    '2013-09-04 16:25:02+09', 'First_thread!_Hopefully_this_will_actually_work_c', 1234,
    '2013-09-04 16:27:34+09', 'It looks like it worked reasonably well, fortunately.', 'thick_mcrunfast'
);
insert into post (thread_created, thread_sTitle, thread_random, posted, body, userid) values (
    '2013-09-04 16:25:02+09', 'First_thread!_Hopefully_this_will_actually_work_c', 1234,
    '2013-09-04 16:28:12+09', 'Yeah! Pretty neat!', 'big_mclargehuge'
);
