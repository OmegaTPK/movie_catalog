create table if not exists actor_entity (
  id INT8 NOT NULL,
   name VARCHAR(300) NOT NULL,
   surname VARCHAR(300) NOT NULL,
   middle_name VARCHAR(300),
   gender INTEGER NOT NULL,
   description VARCHAR(2000) not null,
   birth_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   active_start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   birth_place VARCHAR(255) NOT NULL,
   primary key (id)
);
