<?php
require "init.php";

$encrypted=$_POST["Encrypted"];
$password=$_POST["Key"];


$sql_query= "select Message from db_info where Secret like '%$encrypted%' and Password like '$password';";

$result=mysqli_query($con,$sql_query);

if (mysqli_num_rows($result)>0){
	$row=mysqli_fetch_assoc($result);
	$mes=$row["Message"];
	echo "" . $mes . "";;
	
}




?>