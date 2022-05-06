CREATE TABLE if not exists actor_movie_link (
  actor_id INT8 NOT NULL,
   movie_id INT8 NOT NULL,
   CONSTRAINT pk_actor_movie_link PRIMARY KEY (actor_id, movie_id)
);

ALTER TABLE actor_movie_link ADD CONSTRAINT fk_actmov_on_actor_entity FOREIGN KEY (actor_id) REFERENCES actor_entity (id);

ALTER TABLE actor_movie_link ADD CONSTRAINT fk_actmov_on_movie_entity FOREIGN KEY (movie_id) REFERENCES movie_entity (id);
