<?php $tmpl_title = "Variman Activation Form";
      $tmpl_location = "Variman Activation Form";
      include("../header.php");
?>

<h3>Variman Activation</h3>
<p>Enter the hostname of the server that you are going to run variman on as well as your email address.  We will then email you the variman
	activation file you will need to put in $VARIMAN_HOME/WEB-INF.
<form action="send_activation.php" method="post">
	Hostname: <input type="textbox" name="host"/><br/>
	Email: <input type="textbox" name="email"/><br/>
	<input type="Submit" name="Create Activation" value="Create Activation"/>
</form>
