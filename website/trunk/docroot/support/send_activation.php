<?php $tmpl_title = "Variman Activation Form";
      $tmpl_location = "Variman Activation Form";
      include("../header.php");
?>

<?php
 $email = $_POST['email'];
 $hash = md5($_POST['host'].$email);
 $properties = "#This is your activation file.  Put it in VARIMAN_HOME/webapps/WEB-INF\n";
 $properties = $properties."variman.activation.email=$email\n";
 $properties = $properties."variman.activation.code=$hash" ;

?>

<h3>Variman Activation</h3>
<p>The activation file has been sent to you via email at the address '<?php echo $email?>'.  This email will have the file "activation.properties" attached to it.  
Put it in your Variman install in the directory "$VARIMAN_HOME/webapp/WEB-INF".  </p>

Properties:<br/>
<pre>
<?php echo $properties?>
</pre>
