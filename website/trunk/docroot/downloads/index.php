<?php $tmpl_title = "Downloads";
      $tmpl_location = "Downloads";
      include("../header.php");

function ver()
{
    return "1.0.0-beta1";
}

function print_ver()
{
    echo ver();
}

function dl_file($file)
{
    $url = $GLOBALS["ROOT"] . "/files/" . $file;
    echo "<a href=\"$url\">$file</a>";
}
?>
    <h3>Downloads</h3>

    <p>
      The current version is <b><?php print_ver()?></b>.  The embedded
      Tomcat binary distributions include an embedded version of the
      <a href="http://jakarta.apache.org/tomcat/">Jakarta Tomcat
      4.1</a> servlet container.  The web application distributions
      are meant to be deployed in a separate servlet container.
    </p>

    <table width="100%" border="1" cellspacing="0" cellpadding="5">
      <tr>
        <td class="dl-header">File&nbsp;Name</td>
        <td class="dl-header">File&nbsp;Size</td>
        <td class="dl-header">Description</td>
      </tr>
      <tr>
        <td class="dl-group" colspan="3">Embedded Tomact Binary Downloads</td>
      </tr>
      <tr>
        <td>
          <?php dl_file("rex-" . ver() . "-setup.exe")?>
        </td>
        <td>14M</td>
        <td>
          Embedded Tomcat Windows installer.  Installs a Service to
          start and stop Rex.
        </td>
      </tr>
      <tr>
        <td>
          <?php dl_file("rex-" . ver() . ".tar.gz")?>
        </td>
        <td>14M</td>
        <td>Embedded Tomcat archive for non-Windows platforms.</td>
      </tr>
      <tr>
        <td class="dl-group" colspan="3">Web Application Binary Downloads</td>
      </tr>
      <tr>
        <td>
          <?php dl_file("rex-webapp-" . ver() . ".tar.gz")?>
        </td>
        <td>11M</td>
        <td> Web application archive, tar+gzipped. </td>
      </tr>
      <tr>
        <td>
          <?php dl_file("rex-webapp-" . ver() . ".zip")?>
        </td>
        <td>11M</td>
        <td> Web application archive, zipped. </td>
      </tr>
      <tr>
        <td class="dl-group" colspan="3">Source Downloads</td>
      </tr>
      <tr>
        <td>
          <?php dl_file("rex-" . ver() . "-src.tar.gz")?>
        </td>
        <td>255k</td>
        <td>Source archive, tar+gzipped</td>
      </tr>
      <tr>
        <td>
          <?php dl_file("rex-" . ver() . "-src.zip")?>
        </td>
        <td>485k</td>
        <td>Source archive, zipped</td>
      </tr>
    </table>

    <p>
      All versions may be downloaded from <a href="<?php
      ROOT()?>/files/">this directory</a>.
    </p>

    <?php include("../footer.php"); ?>
