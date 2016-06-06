COPY AND PASTE ALL FILES IN HOME FOLDER(or CURRENT DIRECTORY).IF NOT YOU WILL HAVE TO GIVE PATH INSIDE CODE AND FOR EXECUTING

After clicking submit button you cannot draw any more lines for query 2.
After clicking submit button you cannot select anymore buildings in query 4.

You have to select another radiobutton and select again to refresh the query.

to compile you have to:

1.setup jdk

Note: Since our dc computers already have ojdbc6 in its libraries we can compile directly like javac hw3.java

2.javac -cp .:ojdbc6.jar hw3.java
3.java -cp .:ojdbc6.jar hw3

To populate
1.setup jdk
2.javac -cp .:ojdbc6.jar populate.java
3.java -cp .:ojdbc6.jar populate building.xy hydrant.xy firebuilding.txt

create db consists of following tables

building
firebuilding
hydrant

Values for user_sdo_geom_metadata is inserted for both buildibg,sgape and hydrant.shape

create db consists of following Indexes

building_index
hydrant_index
   
dropdb.sql removes all values from tables and indexes

COPY AND PASTE ALL FILES IN HOME FOLDER(or CURRENT DIRECTORY).IF NOT YOU WILL HAVE TO GIVE PATH INSIDE CODE AND FOR EXECUTING
