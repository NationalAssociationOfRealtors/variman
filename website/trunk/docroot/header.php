<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- <?php

# 
function setup_globals()
{
  $self = $_SERVER["PHP_SELF"];
  $script = realpath($_SERVER["SCRIPT_FILENAME"]);
  $file = realpath(__FILE__);

#  echo "self: $self\n";
#  echo "script: $script\n";
#  echo "file: $file\n";

  $doc_root =  ereg_replace(dirname($self) . "$", "", dirname($script));
  $GLOBALS["DOCUMENT_ROOT"] = $doc_root;
  $GLOBALS["ROOT"] = str_replace($doc_root, "", dirname($file));
}

setup_globals();
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
    <table width="100%"class="header">
        <tr>
          <td class="hleft">Rex RETS Server</td>
          <td class="hright">
        <?php enav("Home") ?> |
        <?php enav("Documentation") ?> |
        <?php enav("Downloads") ?> |
        <?php enav("Support") ?>
          </td>
        </tr>
    </table>

    <div class="content">
