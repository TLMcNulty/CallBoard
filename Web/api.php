<?php
// Create connection
$host     = "localhost";
$username = "";
$password = "";
$dbname   = "";

$con= mysqli_connect($host,$username,$password,$dbname);
// Check connection
#if (mysqli_connect_errno())
#{
#	exit("<div style=\"color:#FF0000;font-size:4em;font-family:Arial;\">FAILURE:<br /> \"" . mysqli_connect_error() . "\" </div>");
#	echo "<div style=\"color:#FF0000;font-size=\"6em;\"\">FAILURE: \"" . mysqli_connect_error() . "\" </div>";
#}

//Query Database for calls with the max callnum (Newest)
$result = mysqli_query($con,"SELECT * FROM calls ORDER BY callnum DESC LIMIT 1");

while($row = mysqli_fetch_array($result))
{
	$callnum	= $row['callnum'];
	$priority 	= $row['priority'];	
	$emd 		= $row['emd'];
	$loc 		= $row['loc'];
	$desc 		= $row['calldesc'];
	$rtime 		= $row['calltime'];
	echo "{\"callnum\":\"$callnum\", \"priority\":\"$priority\", \"emd\":\"$emd\", \"loc\":\"$loc\", \"desc\":\"$desc\", \"rtime\":\"$rtime\"}";
}

mysqli_close($con);
?>
