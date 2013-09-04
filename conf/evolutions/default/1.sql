# Create base schema

# --- !Ups

create table users (
    userid text not null primary key
);

create table thread (
    created    timestamptz not null,
    shortTitle text not null,
    random     int not null,
    title      text not null,
    userid     text references users(userid) not null,
    primary key (created, shortTitle, random)
);

create table post (
    thread_created timestamptz not null,
    thread_sTitle  text not null,
    thread_random  int not null,
    posted        timestamptz not null,
    body          text,
    userid        text references users(userid) not null,
    foreign key (thread_created, thread_sTitle, thread_random) references thread(created, shortTitle, random),
    unique (posted, body, userid)
);

# --- !Downs

drop table users;
drop table thread;
drop table post;
