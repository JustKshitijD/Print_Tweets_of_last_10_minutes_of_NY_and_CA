<?php 

//$res=shell_exec("/var/www/html/show_fp_output_2.sh 2>$");

//readfile("/home/ubuntu/fp_output_2")

header('Content-Type: text/plain'); 
ini_set('auto_detect_line_endings',true);
echo file_get_contents("/home/ubuntu/fp_output_2");


//echo "$res"
 ?>


