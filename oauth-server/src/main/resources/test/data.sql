INSERT INTO user ( id, name ) VALUES
  ( 1, 'ADMIN1' ),
  ( 2, 'ADMIN2' ),
  ( 3, 'ADMIN3' );

  INSERT INTO user_restricted ( id, user_id, reason ) VALUES
  ( 1, 2, 'reason1' );

INSERT INTO role ( id, name ) VALUES
  ( 1, 'admin' );

INSERT INTO user_roles ( user_id, role_id ) VALUES
  ( 1, 1 );

INSERT INTO client ( id, name, serial_id, encrypted_secret, url, callback, description, owner_id ) VALUES
  ( 1, 'APP1', 'S1', 'SECRET1', 'http://example.com', 'http://example.com', '1111', 1 ),
  ( 2, 'APP2', 'S2', 'SECRET2', 'http://example.com', 'http://example.com', '2222', 2 ),
  ( 3, 'APP3', 'S3', 'SECRET3', 'http://example.com', 'http://example.com', '3333', 2 );
-- 150f821363ad4e3a3031dd47565a7cba
-- bb4266c5c397d40e5b70dfe1e9dee626

INSERT INTO client_restricted ( id, client_id, reason ) VALUES
  ( 1, 2, 'reason1' );

INSERT INTO api_token ( id, encrypted_token, date_created, last_updated, date_expired,  client_id ) VALUES
  ( 1, 'TOKEN1', '2050-12-25', '2050-12-25', '2100-12-25', 1 ),
  ( 2, 'TOKEN2', '2050-12-25', '2050-12-25', '2000-12-25', 2 ),
  ( 3, 'TOKEN3', '2050-12-25', '2050-12-25', '2100-12-25', 3 );
-- 404928c32a31c3bb589c1a878b54c3fe

INSERT INTO refresh_token ( id, encrypted_token, access_token_id, client_id, user_id, date_created, last_updated, date_expired ) VALUES
  ( 1, 'TOKEN1', 1, 1, 1, '2050-12-25', '2050-12-25', '2100-12-25' ),
  ( 2, 'TOKEN2', 2, 2, 2, '2050-12-25', '2050-12-25', '2000-12-25' ),
  ( 3, 'TOKEN3', 3, 3, 3, '2050-12-25', '2050-12-25', '2100-12-25' );
-- 3008a3bf9b3cbc298303f731c350debe08fc04eb46cff3b48d00ff43574e2f50

INSERT INTO refresh_token_scope ( permission_id, refresh_token_id ) VALUES
  ( 1, 1 ),
  ( 2, 1 ),
  ( 1, 3 ),
  ( 2, 3 );

INSERT INTO access_token ( id, encrypted_token, client_id, user_id, date_created, last_updated, date_expired ) VALUES
  ( 1, 'TOKEN1', 1,  1, '2050-12-25', '2050-12-25', '2100-12-25' ),
  ( 2, 'TOKEN2', 2,  2, '2050-12-25', '2050-12-25', '2000-12-25' ),
  ( 3, 'TOKEN3', 3, 3, '2050-12-25', '2050-12-25', '2100-12-25' );
-- 3008a3bf9b3cbc298303f731c350debe08fc04eb46cff3b48d00ff43574e2f50

INSERT INTO access_token_scope ( permission_id, access_token_id ) VALUES
  ( 1, 1 ),
  ( 2, 1 ),
  ( 1, 3 ),
  ( 2, 3 );

INSERT INTO authorization_code ( id, encrypted_code, client_id, user_id, date_created, last_updated, date_expired ) VALUES
  ( 1, 'CODE1', 1,  1, '2050-12-25', '2050-12-25', '2100-12-25' ),
  ( 2, 'CODE2', 2,  2, '2050-12-25', '2050-12-25', '2000-12-25' ),
  ( 3, 'CODE3', 3, 3, '2050-12-25', '2050-12-25', '2100-12-25' );
-- 63a2bb18e5031622606ccf41fcee4f1245340e016184080c36824351d9773dfc

INSERT INTO authorization_code_scope ( permission_id, authorization_code_id ) VALUES
  ( 1, 1 ),
  ( 2, 1 ),
  ( 1, 3 ),
  ( 2, 3 );

INSERT INTO permission ( id, name ) VALUES
  ( 1, 'ADMIN' ),
  ( 2, 'READ' ),
  ( 3, 'WRITE' );

INSERT INTO token_access_log( id, token_type, token_id, client_id, ip, referer, application ) VALUES
  ( 1, 'AccessToken', 1, 1, '127.0.0.1', NULL, 'location' ),
  ( 2, 'AccessToken', 2, 2, '192.168.0.1', NULL, 'activity' ),
  ( 3, 'ApiToken', 3, 3, '192.168.0.2', NULL, 'course' );