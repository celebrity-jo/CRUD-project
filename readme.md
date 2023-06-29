#Spring 게시판 CRUD 프로젝트

## 환경

- windows11
- java 11
- spring framework
- thymeleaf
- h2 database
- git

## H2 테이블 생성

- root 사용자로 접속

### USER
- id         (유저 id)
- userId     (아이디)
- password   (비밀번호)
- userName   (이름)
- email      (이메일)
- address    (주소)
- userRole   (관리자, 사용자 구분)
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

insert into users(userId, password, username, email, address, userRole, createDate) values ('hello','world', '스프링', 'hello@naver.com', '서울특별시', 'USER', NOW());
insert into users(userId, password, username, email, address, userRole, createDate) values ('test','test', '테스트', 'test@test.co.kr', '제주도', 'USER', NOW());
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

insert into board(userId, title, content, createDate) values (2, '제목', '내용', NOW());
insert into board(userId, title, content, createDate) values (1, '게시글', '입니다', NOW());
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

기타 설명
- 회원 가입 기능 구현
- Session을 이용해 로그인 기능 구현
- DataSource로 데이터베이스 커넥션 풀을 이용 (10개의 커넥션)
- 썸머노트를 이용해 게시글 작성 (https://summernote.org/)
- 댓글 기능 구현
- 게시판 페이징 구현 (Previous, 1~5, Next)
