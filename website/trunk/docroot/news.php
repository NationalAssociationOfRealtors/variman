<?php $tmpl_title = "Variman Web Server";
      $tmpl_location = "NEWS";
      include("header.php");

      require("markdown.php");
      
      echo Markdown(file_get_contents("NEWS"));
?>



<?php include("footer.php"); ?>
