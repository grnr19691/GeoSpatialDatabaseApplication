delete from hydrant;
delete from firebuilding;
delete from building;
delete from mdsys.user_sdo_geom_metadata where table_name like 'BUILDING';
delete from mdsys.user_sdo_geom_metadata where table_name like 'HYDRANT';
drop INDEX building_index;
drop INDEX hydrant_index;


drop table hydrant;
drop table firebuilding;
drop table building;
