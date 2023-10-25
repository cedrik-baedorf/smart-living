DROP USER IF EXISTS 'DEMO_USER'@'localhost';
CREATE USER IF NOT EXISTS 'DEMO_USER'@'localhost' IDENTIFIED BY 'PASSWORD';
REVOKE ALL PRIVILEGES ON *.* FROM 'DEMO_USER'@'localhost';
GRANT SELECT ON `smart-living`.* TO 'DEMO_USER'@'localhost';
GRANT INSERT ON `smart-living`.* TO 'DEMO_USER'@'localhost';
GRANT DELETE ON `smart-living`.* TO 'DEMO_USER'@'localhost';
FLUSH PRIVILEGES;