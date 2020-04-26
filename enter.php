<?php
require "init.php";

$message=$_POST["OriginalMessage"];
$password=$_POST["Password"];
$encrypted=$_POST["Encrypted"];

$sql_query="insert into db_info values('$message','$password','$encrypted');";

if (mysqli_query($con,$sql_query)){
	//echo "<h3> Data insertion successful</h3>";
}
else{
	//echo "Data insertion error".mysqli_error($con);
}




?>