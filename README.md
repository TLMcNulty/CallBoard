![Callboard+](https://github.com/pete800/CallBoard/blob/master/Pictures/logo.png)
=========

An automatic and autonomous 911 page display and notification. 

![GOGOGO](https://github.com/pete800/CallBoard/blob/master/Pictures/Current%20View.png)

CallBoard currently recieves text based digital pages from Monroe County 911 ECD. The pages are then dumped to a serial output 
where a Java App picks them up and formats them before inserting them into a MySQL database. 

The database is then read by PHP and inserted into a generated HTML doc through JavaScript. 

The page automatically refreshes when new information is detected. Upon this detection the call details are forwarded to an AWS SNS topic that sends text messages.

![An early version](https://github.com/pete800/CallBoard/blob/master/Pictures/Pri1%20Edited.PNG)
The earliest version of the display. 

![A later revision](https://github.com/pete800/CallBoard/blob/master/Pictures/pri_4_bottom.png)
A later revision

![The current skin](https://github.com/pete800/CallBoard/blob/master/Pictures/Current%20View.png)
The current layout. The colors are subdued for low light situations. The colored time is the time the call was recieved. 

![Pager output](https://github.com/pete800/CallBoard/blob/master/Pictures/Capture.PNG)

This is the output the pager gives us. 
