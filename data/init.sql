CREATE TABLE player (
  id int4 not null primary key,
  name varchar(100) not null
);

CREATE TABLE team_score (
  player_1 int4 not null,
  player_2 int4 not null,
  player_3 int4 not null,
  player_4 int4 not null,
  player_5 int4 not null,
  score decimal(8,3) not null,
  CONSTRAINT team_score_pk PRIMARY KEY (player_1, player_2, player_3, player_4, player_5),
  CONSTRAINT team_score_player_1_fk FOREIGN KEY (player_1) REFERENCES player(id),
  CONSTRAINT team_score_player_2_fk FOREIGN KEY (player_2) REFERENCES player(id),
  CONSTRAINT team_score_player_3_fk FOREIGN KEY (player_3) REFERENCES player(id),
  CONSTRAINT team_score_player_4_fk FOREIGN KEY (player_4) REFERENCES player(id),
  CONSTRAINT team_score_player_5_fk FOREIGN KEY (player_5) REFERENCES player(id)
);

CREATE VIEW team_ranking AS
SELECT score,
       (SELECT name FROM player WHERE id = player_1),
       (SELECT name FROM player WHERE id = player_2),
       (SELECT name FROM player WHERE id = player_3),
       (SELECT name FROM player WHERE id = player_4),
       (SELECT name FROM player WHERE id = player_5)
FROM team_score
ORDER BY score DESC;
