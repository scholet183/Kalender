#!/bin/bash
service mysql start

mysql -e "CREATE DATABASE IF NOT EXISTS kalender_db;"
mysql -e "CREATE USER IF NOT EXISTS 'kalender_user'@'localhost' IDENTIFIED BY 'password';"
mysql -e "GRANT ALL PRIVILEGES ON kalender_db.* TO 'kalender_user'@'localhost';"
mysql -e "FLUSH PRIVILEGES;"