<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- <?php

# 
$DOCUMENT_ROOT = ereg_replace(dirname($_SERVER["PHP_SELF"]) . "$", "",
                              dirname($_SERVER["SCRIPT_FILENAME"]));
$ROOT = str_replace($DOCUMENT_ROOT, "", dirname(__FILE__));
function ROOT()
{
    echo $GLOBALS["ROOT"];
}


$nav_urls = array("Home" => "/",
             "Documentation" => "/documentation/",
             "Downloads" => "/downloads/",
             "Support" => "/support/");

function nav($url, $name)
{
    echo "<a href=\"" . $GLOBALS["ROOT"] . $url . "\"\>" . $name . "</a>";
}

function enav($name)
{
    $url = $GLOBALS["ROOT"] . $GLOBALS["nav_urls"][$name];
    if ($GLOBALS["tmpl_location"] == $name) {
        echo $name ;
    }
    else {
        echo "<a href=\"" . $url . "\">" . $name . "</a>";
    }
}

?> -->
<html>
  <head>
    <title><?php echo "Rex: $tmpl_title"; ?></title>
    <link href="<?php ROOT() ?>/main.css" type="text/css"
          rel="STYLESHEET">
    </style>
  </head>

  <body bgcolor="#ffffff">
    <div class="header">
      <span class="hleft">Rex RETS Server</span>
      <span class="hidden">:</span>
      <span class="hright">
        <?php enav("Home") ?> |
        <?php enav("Documentation") ?> |
        <?php enav("Downloads") ?> |
        <?php enav("Support") ?>
      </spah>
    </div>

    <div class="content">
