CREATE TABLE VENUE (
id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
name varchar(255),
category varchar(255),
url varchar(255),
date timestamp,
excerpt varchar(255),
thumbnail varchar(255),
lat decimal(10,7),
lng decimal(10,7),
address varchar(255),
phone varchar(255),
twitter varchar(255),
stars_beer decimal(2,1),
stars_atmosphere decimal(2,1),
stars_amenities decimal(2,1),
stars_value decimal(2,1),
tags varchar(255)
)