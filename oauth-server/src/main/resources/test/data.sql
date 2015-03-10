
INSERT INTO user ( id, name ) VALUES
  ( 1, 'ADMIN1' ),
  ( 2, 'ADMIN2' ),
  ( 3, 'ADMIN3' );

INSERT INTO client ( id, name, secret, url, callback, description, owner_id ) VALUES
  ( 1, 'APP1', 'SECRET1', 'http://example.com', 'http://example.com', '1111', 1 ),
  ( 2, 'APP2', 'SECRET2', 'http://example.com', 'http://example.com', '2222', 2 ),
  ( 3, 'APP3', '$2a$10$Sm07H4pIys2Ae45rdO5rJuiEe/swFKoJneZLg.bU9HjFsBBZktwua', 'http://example.com', 'http://example.com', '3333', 2 );

INSERT INTO access_token ( id, token, scope, client_id, user_id, date_created, date_updated ) VALUES
  ( 1, 'TOKEN1', '011', 1,  1, '2050-12-25', '2050-12-25' ),
  ( 2, 'TOKEN2', '00', 2,  2, '2050-12-25', '2050-12-25' ),
  ( 3, '$2a$10$dEFh.Tw.s05K66oj0gOopObZePAyYRGuTCkBLN5dvrdfS4x2RxtHO', '011', 2, 2, '2050-12-25', '2050-12-25' );
-- Mzo6OlRPS0VO

INSERT INTO auth_code ( id, code, scope, client_id, user_id, date_created, date_updated ) VALUES
  ( 1, 'CODE1', '011', 1,  1, '2050-12-25', '2050-12-25' ),
  ( 2, 'CODE2', '00', 2,  2, '2050-12-25', '2050-12-25' ),
  ( 3, '$2a$10$BCU4w83RytKXMUswUcflw.nhZdAAq.lr4Mf873a0C6zXX/IclxTIC', '011', 3, 3, '2050-12-25', '2050-12-25' );
-- Mzo6OkNPREU=

INSERT INTO permission ( id, name ) VALUES
  ( 1, 'ADMIN' ),
  ( 2, 'READ' ),
  ( 3, 'WRITE' );