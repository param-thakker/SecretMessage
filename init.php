<?php
$db_name="webappdb";
$mysql_user="root";
$mysql_pass="root123";
$server_name="127.0.0.1";

$con=mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);
if (!$con){
	//echo "Connection Error".mysqli_connect_error();
}
else{
	//echo "<h3>Connection Success </h3>";
}



?>