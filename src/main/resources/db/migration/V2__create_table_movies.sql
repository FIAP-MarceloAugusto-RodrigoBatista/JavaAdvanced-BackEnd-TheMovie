CREATE TABLE T_TMOVIE_MOVIE (
    id bigint auto_increment,
    photo varchar(255),
    name VARCHAR2(255),
    description VARCHAR2(1000),
    actors VARCHAR2(500),
    primary key (id),
    user_id bigint,
    FOREIGN KEY (user_id) REFERENCES T_TMOVIE_USER(id)
);