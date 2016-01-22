CallBoard
=========

An automatic and autonomous 911 page display.

![GOGOGO](https://github.com/TLMcNulty/CallBoard/raw/master/Pictures/Done1.PNG)

CallBoard currently recieves text based digital pages from Monroe County 911 ECD. The pages are then dumped to a serial output 
where a Java App picks them up and formats them before inserting them into a MySQL database. 

The database is then read by PHP and inserted into a generated HTML doc through JavaScript. 

Version .1 uses an HTML Meta http refresh command to update the clock and database queries. Later versions will be static pages 
with pushed notifications. 

![Priority 2](https://github.com/TLMcNulty/CallBoard/raw/master/Pictures/Done%202.png)
