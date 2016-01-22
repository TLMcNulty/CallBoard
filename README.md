CallBoard
=========

An automatic and autonomous 911 page display and notification. 

![GOGOGO](https://github.com/pete800/CallBoard/blob/master/Pictures/Current%20View.png)

CallBoard currently recieves text based digital pages from Monroe County 911 ECD. The pages are then dumped to a serial output 
where a Java App picks them up and formats them before inserting them into a MySQL database. 

The database is then read by PHP and inserted into a generated HTML doc through JavaScript. 

The page automatically refreshes when new information is detected. Upon this detection the call details are forwarded to an AWS SNS topic that sends text messages.

![Priority 2](https://github.com/TLMcNulty/CallBoard/raw/master/Pictures/Done%202.png)
