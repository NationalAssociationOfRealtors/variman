<?php $tmpl_title = "Variman Web Server";
      $tmpl_location = "Beta NEWS";
      include("header.php");

      require("markdown.php");
      
      echo Markdown(file_get_contents("NEWS_beta"));
?>



<?php include("footer.php"); ?>
