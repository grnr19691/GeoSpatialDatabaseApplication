create table building(
id varchar(20) primary key,
name varchar(30) ,
no_points number(3),
shape SDO_GEOMETRY);

create table firebuilding(
name varchar(30) primary key
);

create table hydrant(
id varchar(20),
location SDO_GEOMETRY);

INSERT INTO user_sdo_geom_metadata(TABLE_NAME,COLUMN_NAME,DIMINFO,SRID) VALUES ('building','shape',SDO_DIM_ARRAY(SDO_DIM_ELEMENT('X', 0, 820, 0.005),SDO_DIM_ELEMENT('Y', 0, 580, 0.005)),NULL);

INSERT INTO user_sdo_geom_metadata(TABLE_NAME,COLUMN_NAME,DIMINFO,SRID) VALUES ('hydrant','location',SDO_DIM_ARRAY(SDO_DIM_ELEMENT('X', 0, 820, 0.005),SDO_DIM_ELEMENT('Y', 0, 580, 0.005)),NULL);

CREATE INDEX building_index
   ON building(shape)
   INDEXTYPE IS MDSYS.SPATIAL_INDEX;
   
   
CREATE INDEX hydrant_index
   ON hydrant(location)
   INDEXTYPE IS MDSYS.SPATIAL_INDEX;
