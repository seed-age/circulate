CREATE DATABASE mybox; 
CREATE DATABASE mybox_model; 
GRANT ALL PRIVILEGES ON mybox.* TO mybox@'localhost' IDENTIFIED BY 'mybox' WITH GRANT OPTION; 
GRANT ALL PRIVILEGES ON mybox.* TO mybox@'%' IDENTIFIED BY 'mybox' WITH GRANT OPTION; 
flush privileges;
