
<?php 

//$res=shell_exec("/var/www/html/show_fp_output.sh 2>$");

//readfile("/home/ubuntu/fp_output")
header('Content-Type: text/plain'); 
ini_set('auto_detect_line_endings',true);
echo file_get_contents("/home/ubuntu/fp_output");

//echo "$res"
 ?>

