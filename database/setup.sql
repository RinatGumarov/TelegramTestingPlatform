DROP DATABASE IF EXISTS testingplatformbot;
CREATE SCHEMA IF NOT EXISTS testingplatformbot DEFAULT CHARACTER SET utf8;
SET NAMES utf8;
use testingplatformbot;

drop TABLE  IF EXISTS tests;
create table IF NOT EXISTS tests
(
  token varchar(16) not null
    primary key,
  user_id int null,
  name mediumtext null
)
;

drop TABLE IF EXISTS questions;
create table questions
(
  id int not null,
  test_token varchar(16) not null,
  question mediumtext null,
  photo text null,
  primary key (id, test_token),
  constraint test_token
  foreign key (test_token) references testingplatformbot.tests (token)
    on update cascade on delete cascade
)
;

create index test_token_idx
  on questions (test_token)
;


DROP TABLE IF EXISTS answers;
create table IF NOT EXISTS answers
(
  id int not null,
  id_question int not null,
  test_token varchar(16) not null,
  answer mediumtext null,
  istrue tinyint null
)
;

