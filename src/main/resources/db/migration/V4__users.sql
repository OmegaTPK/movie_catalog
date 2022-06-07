CREATE TABLE credentials_entity (
  id BIGINT NOT NULL,
   login VARCHAR(255),
   password VARCHAR(255),
   CONSTRAINT pk_credentials_entity PRIMARY KEY (id)
);

CREATE TABLE role_entity (
  id BIGINT NOT NULL,
   name VARCHAR(255) NOT NULL,
   CONSTRAINT pk_role_entity PRIMARY KEY (id)
);

CREATE TABLE usr (
  id BIGINT NOT NULL,
   name VARCHAR(255) NOT NULL,
   lastname VARCHAR(255) NOT NULL,
   description VARCHAR(255),
   active BOOLEAN NOT NULL,
   credentials_id BIGINT,
   CONSTRAINT pk_usr PRIMARY KEY (id)
);

CREATE TABLE usr_roles (
  role_id BIGINT NOT NULL,
   usr_id BIGINT NOT NULL,
   CONSTRAINT pk_usr_roles PRIMARY KEY (role_id, usr_id)
);

ALTER TABLE credentials_entity ADD CONSTRAINT uc_credentials_entity_login UNIQUE (login);

ALTER TABLE role_entity ADD CONSTRAINT uc_role_entity_name UNIQUE (name);

ALTER TABLE usr ADD CONSTRAINT FK_USR_ON_CREDENTIALS FOREIGN KEY (credentials_id) REFERENCES credentials_entity (id);

ALTER TABLE usr_roles ADD CONSTRAINT fk_usr_roles_on_role_entity FOREIGN KEY (role_id) REFERENCES role_entity (id);

ALTER TABLE usr_roles ADD CONSTRAINT fk_usr_roles_on_user_entity FOREIGN KEY (usr_id) REFERENCES usr (id);

INSERT INTO public.role_entity(
	id, name)
	VALUES (0,'DEFAULT'), (1,'MANAGER'), (2,'ADMIN');