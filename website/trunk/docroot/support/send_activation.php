<?php $tmpl_title = "Variman Activation Form";
      $tmpl_location = "Variman Activation Form";
      include("../header.php");
?>

<?php
 $email = $_POST['email'];
 $host = $_POST['host'];
 $hash = md5($host.$email);
 $properties = "#This is your activation file.  Put it in \$VARIMAN_HOME/webapps/WEB-INF\n";
 $properties .= "variman.activation.email=$email\n";
 $properties .= "variman.activation.code=$hash" ;

$message = "Your variman activation file is attached as the file activation.properties.  Please put this file in \$VARIMAN_HOME/webapps/WEB-INF\n";



if(store_activation($host, $email)) {
	send_activation($email, "Your Variman Activation File", $message, $properties);
} else {
	echo "Could not create activation!";
	exit();
}

function store_activation($host, $email){
	$dbcnx = mysql_connect("localhost", "variman_act", "stuc9A5e");	
	if($dbcnx) {
		if(mysql_select_db("variman_activation", $dbcnx)) {		
			$scrubbed_host = mysql_real_escape_string($host);
			$scrubbed_email = mysql_real_escape_string($email);
			$query_sql = "select * from activation_information where host='$scrubbed_host' and email='$scrubbed_email'";
			$result = mysql_query($query_sql);
			if($result and mysql_fetch_array($result)) {				
				return true;
			} else {
				$insert_sql = "insert into activation_information(host, email) values('$scrubbed_host','$scrubbed_email')";
				return mysql_query($insert_sql);
			}
		} else {
			return false;
		}
	} else {
		return false;
	}
}

function send_activation($to,$subject,$body,$attachment){

 $semi_rand = md5( time() );
 $mime_boundary = "==Multipart_Boundary_x{$semi_rand}x";
 $headers = "From: Variman Activation <variman.activation@crt.realtors.org>\n";
 $headers .= "Reply-To: CRT Information <info@crt.realtors.org>\n";
 $headers .= "Content-Type: multipart/mixed; boundary=\"{$mime_boundary}\"";  // space *needs* to be there

$message = "This is a multi-part message in MIME format.\n\n".
  "--{$mime_boundary}\n".
  "Content-Type: text/plain; charset=ISO-8859-1;\n".
  "Content-Transfer-Encoding: 7bit\n\n".
  $body."\n\n".
  "--{$mime_boundary}\n".
  "Content-Type: text/plain; charset=ISO-8859-1; name=\"activation.properties\"\n".
  "Content-Transfer-Encoding: 7bit\n".
  "Content-Disposition: attachment; filename=\"activation.properties\"\n\n".
  $attachment."\n\n".
  "--{$mime_boundary}--";

 return mail($to,$subject,$message,$headers);
}

?>

<h3>Variman Activation</h3>
<p>The activation file has been sent to you via email at the address '<?php echo $email?>'.  This email will have the file "activation.properties" attached to it.  
Put it in your Variman install in the directory "$VARIMAN_HOME/webapp/WEB-INF".  </p>
