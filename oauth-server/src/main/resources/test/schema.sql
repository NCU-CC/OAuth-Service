CREATE TABLE IF NOT EXISTS access_token
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  version INT DEFAULT 0,
  date_created DATETIME,
  last_updated DATETIME,
  last_used DATETIME,
  date_expired DATETIME,
  encrypted_token VARCHAR(255),
  client_id INT NOT NULL,
  user_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS refresh_token
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  version INT DEFAULT 0,
  date_created DATETIME,
  last_updated DATETIME,
  last_used DATETIME,
  date_expired DATETIME,
  encrypted_token VARCHAR(255),
  client_id INT NOT NULL,
  user_id INT NOT NULL,
  access_token_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS authorization_code
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  version INT DEFAULT 0,
  date_created DATETIME,
  last_updated DATETIME,
  last_used DATETIME,
  date_expired DATETIME,
  encrypted_code VARCHAR(255) ,
  client_id INT NOT NULL,
  user_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS client
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  version INT DEFAULT 0,
  date_created DATETIME,
  last_updated DATETIME,
  callback VARCHAR(255),
  description VARCHAR(255),
  name VARCHAR(255),
  serial_id VARCHAR(255),
  encrypted_secret VARCHAR(255),
  url VARCHAR(255),
  deleted BOOLEAN DEFAULT FALSE ,
  owner_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS client_restricted
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  version INT DEFAULT 0,
  date_created DATETIME,
  last_updated DATETIME,
  client_id INT NOT NULL,
  reason VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS api_token
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  version INT DEFAULT 0,
  date_created DATETIME,
  last_updated DATETIME,
  last_used DATETIME,
  date_expired DATETIME,
  encrypted_token VARCHAR(255),
  client_id INT NOT NULL,
);

CREATE TABLE IF NOT EXISTS user
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  version INT DEFAULT 0,
  date_created DATETIME,
  last_updated DATETIME,
  name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_restricted
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  version INT DEFAULT 0,
  date_created DATETIME,
  last_updated DATETIME,
  user_id INT NOT NULL,
  reason VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS role
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  version INT DEFAULT 0,
  date_created DATETIME,
  last_updated DATETIME,
  name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_roles
(
  user_id INT NOT NULL,
  role_id INT NOT NULL,
);

CREATE TABLE IF NOT EXISTS permission
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  version INT DEFAULT 0,
  date_created DATETIME,
  last_updated DATETIME,
  name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS access_token_scope
(
  permission_id INT NOT NULL,
  access_token_id INT NOT NULL,
);

CREATE TABLE IF NOT EXISTS authorization_code_scope
(
  permission_id INT NOT NULL,
  authorization_code_id INT NOT NULL,
);

CREATE TABLE IF NOT EXISTS refresh_token_scope
(
  permission_id INT NOT NULL,
  refresh_token_id INT NOT NULL,
);

CREATE TABLE IF NOT EXISTS token_access_log
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  version INT DEFAULT 0,
  date_created DATETIME,
  last_updated DATETIME,
  token_type VARCHAR(50),
  token_id  INT,
  client_id INT,
  ip VARCHAR(50),
  referer VARCHAR(255),
  application VARCHAR(50)
);