#Spring 게시판 CRUD 프로젝트

## 환경

- windows11
- java 11
- spring framework
- h2 database
- git

## H2 테이블 생성

- bloguser 사용자로 접속
- use blog; 데이터베이스 선택

### USER
- id         (유저 id)
- userId     (아이디)
- password   (비밀번호)
- userName   (이름)
- email      (이메일)
- address    (주소)
- userRole   (관리자인지 사용자인지)
- createDate (생성날짜)


'''sql
drop table users if exists cascade;
create table users (
 id int not null auto_increment,
 userId varchar(100) not null unique,
 password varchar(100) not null,
 username varchar(30) not null,
 email varchar(100) not null,
 address varchar(100),
 userRole varchar(50),
 createDate timestamp,
 primary key (id)
);
'''

### BOARD
- id         (게시글 id)
- userId     (작성자)
- title      (제목)
- content    (내용)
- readCount  (조회수)
- createDate (생성날짜)


'''sql
drop table board if exists cascade;
create table board (
 id int not null auto_increment,
 userId int,
 title varchar(100) not null,
 content longtext,
 readCount int default 0,
 createDate timestamp
 foreign key(userId) references user(id) on update cascade,
 primary key (id)
);
'''

### REPLY
- id		 (댓글 id)
- userId	 (댓글 작성자 id)
- boardId	 (게시글 id)
- content	 (내용)
- createDate (생성날짜)

'''sql
drop table reply if exists cascade;
create table reply (
 id int auto_increment,
 userId int,
 boardId int,
 content varchar(300) not null,
 createDate timestamp,
 foreign key (userId) references users (id) on delete set null,
 foreign key (boardId) references board (id) on delete cascade,
 primary key (id)
);
'''
