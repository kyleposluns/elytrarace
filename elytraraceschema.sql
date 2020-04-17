DROP DATABASE IF EXISTS hyperion_elytra_race;

CREATE DATABASE hyperion_elytra_race;

USE hyperion_elytra_race;
 

DROP TABLE IF EXISTS vectors;
CREATE TABLE vectors (
	vector_id INT NOT NULL AUTO_INCREMENT, 
    x DOUBLE NOT NULL, 
    y DOUBLE NOT NULL, 
    z DOUBLE NOT NULL, 
    CONSTRAINT pk_vectors PRIMARY KEY (vector_id)
);

DROP TABLE IF EXISTS spawns;
CREATE TABLE spawns (
    spawn_id INT NOT NULL AUTO_INCREMENT,
    world_name VARCHAR(255) NOT NULL,
    vector_id INT NOT NULL, 
    yaw DOUBLE NOT NULL,
    pitch DOUBLE NOT NULL,
    CONSTRAINT pk_spawns_id PRIMARY KEY (spawn_id),
	CONSTRAINT fk_spawns_vector_id FOREIGN KEY (vector_id) REFERENCES vectors (vector_id)
);

DROP TABLE IF EXISTS arenas;
CREATE TABLE arenas (
	arena_id CHAR(36) NOT NULL,
    arena_name VARCHAR(255) NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    spawn_id INT NOT NULL,
    CONSTRAINT pk_arenas PRIMARY KEY (arena_id),
    CONSTRAINT fk_arenas_spawn_id FOREIGN KEY (spawn_id) REFERENCES spawns (spawn_id)
);


DROP TABLE IF EXISTS records;
CREATE TABLE records (
	record_id INT NOT NULL AUTO_INCREMENT,
	player_id CHAR(36) NOT NULL, 
    arena_id CHAR(36) NOT NULL, 
    date_millis BIGINT NOT NULL, 
    seconds INT NOT NULL, 
    CONSTRAINT pk_records PRIMARY KEY (record_id), 
    CONSTRAINT fk_records_arena_id FOREIGN KEY (arena_id) REFERENCES arenas (arena_id), 
    CONSTRAINT uk_records UNIQUE (date_millis)
);

DROP TABLE IF EXISTS arena_paths;
CREATE TABLE arena_paths (
	arena_id CHAR(36) NOT NULL,
	vector_id INT NOT NULL, 
    CONSTRAINT fk_arena_paths_arena_id FOREIGN KEY (arena_id) REFERENCES arenas (arena_id),
    CONSTRAINT fk_arena_paths_vector_id FOREIGN KEY (vector_id) REFERENCES vectors (vector_id), 
    CONSTRAINT pk_fk_arena_paths PRIMARY KEY (arena_id, vector_id)
);

DROP TABLE IF EXISTS areas;
CREATE TABLE areas (
	area_id INT NOT NULL AUTO_INCREMENT,
    area_type VARCHAR(10) NOT NULL,
	arena_id CHAR(36) NOT NULL, 
    center_id INT DEFAULT NULL, 
    radius DOUBLE DEFAULT NULL,
    rot_x DOUBLE DEFAULT NULL, 
    rot_y DOUBLE DEFAULT NULL,
	rot_z DOUBLE DEFAULT NULL, 
    pos1_id INT DEFAULT NULL, 
    pos2_id INT DEFAULT NULL,
	checkpoint_ordinal INT NOT NULL,
    CONSTRAINT fk_arena_id FOREIGN KEY (arena_id) REFERENCES arenas (arena_id),
    CONSTRAINT fk_center_id FOREIGN KEY (center_id) REFERENCES vectors (vector_id), 
    CONSTRAINT fk_pos1_id FOREIGN KEY (pos1_id) REFERENCES vectors (vector_id), 
    CONSTRAINT fk_pos2_id FOREIGN KEY (pos2_id) REFERENCES vectors (vector_id), 
    CONSTRAINT pk_area PRIMARY KEY (area_id)
);




DROP PROCEDURE IF EXISTS get_player_record_book;
DELIMITER //
CREATE PROCEDURE get_player_record_book(IN player_id CHAR(36))
BEGIN
	SELECT * FROM records WHERE records.player_id = player_id;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS get_arena_record_book;
DELIMITER //
CREATE PROCEDURE get_arena_record_book(IN arena_id CHAR(36))
BEGIN
	SELECT * FROM records WHERE records.arena_id = arena_id;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS get_arena_path;
DELIMITER //
CREATE PROCEDURE get_arena_path(IN arena_id CHAR(36))
BEGIN
	SELECT p.arena_id, v.x, v.y, v.z FROM arena_paths AS p JOIN vectors AS v ON p.vector_id = v.vector_id WHERE p.arena_id = arena_id;
END //
DELIMITER ; 

DROP PROCEDURE IF EXISTS get_arena_areas; 
DELIMITER // 
CREATE PROCEDURE get_arena_areas(IN arena_id CHAR(36))
BEGIN 
	SELECT * FROM areas WHERE areas.arena_id = arena_id;
END // 
DELIMITER ;

DROP PROCEDURE IF EXISTS get_arena_info;
DELIMITER // 
CREATE PROCEDURE get_arena_info()
BEGIN
	SELECT arena_id, arena_name, display_name, world_name, x, y, z, yaw, pitch 
		FROM (SELECT a.arena_id, a.arena_name, a.display_name, s.world_name, s.vector_id, s.yaw, s.pitch FROM arenas AS a JOIN spawns AS s ON a.spawn_id = s.spawn_id) AS t 
        JOIN vectors AS v ON t.vector_id = v.vector_id;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS get_vector;
DELIMITER // 
CREATE PROCEDURE get_vector(IN vector_id INT)
BEGIN
	SELECT x, y, z FROM vectors WHERE vectors.vector_id = vector_id;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS add_position_to_record;
DELIMITER // 
CREATE PROCEDURE add_position_to_path(IN arena_id CHAR(36), IN x DOUBLE, IN y DOUBLE, IN z DOUBLE)
BEGIN
	DECLARE vector_id INT;

	INSERT INTO vectors VALUES (NULL, x, y, z);
	SELECT LAST_INSERT_ID() INTO vector_id;
	INSERT IGNORE INTO arena_paths VALUES (arena_id, vector_id);
    
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS add_record;
DELIMITER // 
CREATE PROCEDURE add_record(IN player_id CHAR(36), IN arena_id CHAR(36), IN date_millis BIGINT, IN seconds INT)
BEGIN
	INSERT IGNORE INTO records VALUES (NULL, player_id, arena_id, date_millis, seconds);
END //
DELIMITER ;



DROP PROCEDURE IF EXISTS add_arena;
DELIMITER // 
CREATE PROCEDURE add_arena(IN arena_id CHAR(36), IN arena_name VARCHAR(255), IN display_name VARCHAR(255), IN world_name VARCHAR(255), IN x1 DOUBLE, IN y1 DOUBLE, IN z1 DOUBLE, IN yaw DOUBLE, IN pitch DOUBLE)
BEGIN 
	DECLARE p1_id INT;
    DECLARE spawn_id INT;
	INSERT INTO vectors VALUES (NULL, x1, y1, z1); 
    SET p1_id = LAST_INSERT_ID();
    INSERT INTO spawns VALUES (NULL, world_name, p1_id, yaw, pitch);
	SET spawn_id = LAST_INSERT_ID();
    INSERT INTO arenas VALUES (arena_id, arena_name, display_name, spawn_id);
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS add_start;
DELIMITER // 
CREATE PROCEDURE add_start(IN arena_id CHAR(36), IN x1 DOUBLE, IN y1 DOUBLE, IN z1 DOUBLE, IN posx2 DOUBLE, IN posy2 DOUBLE, IN posz2 DOUBLE)
BEGIN 
	DECLARE p1_id INT;
    DECLARE p2_id INT;

	INSERT INTO vectors VALUES (NULL, x1, y1, z1); 
    SET p1_id = LAST_INSERT_ID();
    INSERT INTO vectors VALUES (NULL, posx2, posy2, posz2);
    SET p2_id = LAST_INSERT_ID();
    INSERT INTO areas VALUES (NULL, "START", arena_id, NULL, NULL, NULL, NULL, NULL, p1_id, p2_id, 1);
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS add_end;
DELIMITER // 
CREATE PROCEDURE add_end(IN arena_id CHAR(36), IN x1 DOUBLE, IN y1 DOUBLE, IN z1 DOUBLE, IN posx2 DOUBLE, IN posy2 DOUBLE, IN posz2 DOUBLE, IN checkpoints INT)
BEGIN 
	DECLARE p1_id INT;
    DECLARE p2_id INT;

	INSERT INTO vectors VALUES (NULL, x1, y1, z1); 
    SET p1_id = LAST_INSERT_ID();
    INSERT INTO vectors VALUES (NULL, posx2, posy2, posz2);
    SET p2_id = LAST_INSERT_ID();
    INSERT INTO areas VALUES (NULL, "END", arena_id, NULL, NULL, NULL, NULL, NULL, p1_id, p2_id, checkpoints);
END //
DELIMITER ;


DROP PROCEDURE IF EXISTS add_checkpoint;
DELIMITER // 
CREATE PROCEDURE add_checkpoint(IN arena_id CHAR(36), IN x1 DOUBLE, IN y1 DOUBLE, IN z1 DOUBLE, IN rotX DOUBLE, IN rotY DOUBLE, IN rotZ DOUBLE, IN radius DOUBLE, IN checkpoint_order INT)
BEGIN 
	DECLARE p1_id INT;

	INSERT INTO vectors VALUES (NULL, x1, y1, z1); 
    SET p1_id = LAST_INSERT_ID();
    
    INSERT INTO areas VALUES (NULL, "CHECKPOINT", arena_id, p1_id, radius, rotX, rotY, rotZ, NULL, NULL, checkpoint_order); 
END //
DELIMITER ;


