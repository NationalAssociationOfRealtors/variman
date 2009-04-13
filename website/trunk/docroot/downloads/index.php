<?php $tmpl_title = "Downloads";
      $tmpl_location = "Downloads";
      include("../header.php");

function dl_file($file)
{
    $url = $GLOBALS["ROOT"] . "/files/" . $file;
    echo "<a href=\"$url\">$file</a>";
}

function dl_filesize($file) 
{
    $url = "." . $GLOBALS["ROOT"] . "/files/" . $file;
    $url = "../files/" . $file;
    if (substr($url,0,4)=='http') 
    {
        $x = array_change_key_case(get_headers($url, 1),CASE_LOWER);
        $size = $x['content-length'];
    }
    else 
    { 
	clearstatcache();
        $size = filesize($url); 
    }

    $sizes = array("B","K","M","G");
    for ($i=0; $size > 1024 && isset($sizes[$i+1]); $i++) $size /= 1024;
    $result = round($size,0).$sizes[$i];
    echo "$result";
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
          <?php dl_file("variman-" . ver() . "-setup.exe")?>
        </td>
        <td>
          <?php dl_filesize("variman-" . ver() . "-setup.exe")?>
        </td>
        <td>
          Embedded Tomcat Windows installer.  Installs a Service to
          start and stop Variman.
        </td>
      </tr>
      <tr>
        <td>
          <?php dl_file("variman-" . ver() . ".tar.gz")?>
        </td>
        <td>
          <?php dl_filesize("variman-" . ver() . ".tar.gz")?>
        </td>
        <td>
            Embedded Tomcat archive for all other platforms.  Command
            line administration only.
        </td>
      </tr>
      <tr>
        <td class="dl-group" colspan="3">Web Application Binary Downloads</td>
      </tr>
      <tr>
        <td>
          <?php dl_file("variman-webapp-" . ver() . ".tar.gz")?>
        </td>
        <td>
          <?php dl_filesize("variman-webapp-" . ver() . ".tar.gz")?>
        </td>
        <td> Web application archive, tar+gzipped. </td>
      </tr>
      <tr>
        <td>
          <?php dl_file("variman-webapp-" . ver() . ".zip")?>
        </td>
        <td>
          <?php dl_filesize("variman-webapp-" . ver() . ".zip")?>
        </td>
        <td> Web application archive, zipped. </td>
      </tr>
      <tr>
        <td class="dl-group" colspan="3">Source Downloads</td>
      </tr>
      <tr>
        <td>
          <?php dl_file("variman-" . ver() . "-src.tar.gz")?>
        </td>
        <td>
          <?php dl_filesize("variman-" . ver() . "-src.tar.gz")?>
        </td>
        <td>Source archive, tar+gzipped</td>
      </tr>
      <tr>
        <td>
          <?php dl_file("variman-" . ver() . "-src.zip")?>
        </td>
        <td>
          <?php dl_filesize("variman-" . ver() . "-src.zip")?>
        </td>
        <td>Source archive, zipped</td>
      </tr>
      <tr>
        <td class="dl-group" colspan="3">Miscellaneous Downloads</td>
      </tr>
      <tr>
        <td>
	  <?php dl_file("sample-metadata.zip")?>
        </td>
        <td>
	  <?php dl_filesize("sample-metadata.zip")?>
        </td>
        <td>Sample metadata</td>
      </tr>
      <tr>
        <td>
	  <?php dl_file("sample-1.7.2-metadata.zip")?>
        </td>
        <td>
	  <?php dl_filesize("sample-1.7.2-metadata.zip")?>
        </td>
        <td>Sample RETS 1.7.2 metadata and sample database schema</td>
      </tr>
    </table>

    <?php if (beta_ver()) : ?>
    <p>
      The current beta version is <b><?php print_beta_ver()?></b>.
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
          <?php dl_file("variman-" . beta_ver() . "-setup.exe")?>
        </td>
        <td>
          <?php dl_filesize("variman-" . beta_ver() . "-setup.exe")?>
        </td>
        <td>
          Embedded Tomcat Windows installer.  Installs a Service to
          start and stop Variman.
        </td>
      </tr>
      <tr>
        <td>
          <?php dl_file("variman-" . beta_ver() . ".tar.gz")?>
        </td>
        <td>
          <?php dl_filesize("variman-" . beta_ver() . ".tar.gz")?>
        </td>
        <td>
            Embedded Tomcat archive for all other platforms.  Command
            line administration only.
        </td>
      </tr>
      <tr>
        <td class="dl-group" colspan="3">Web Application Binary Downloads</td>
      </tr>
      <tr>
        <td>
          <?php dl_file("variman-webapp-" . beta_ver() . ".tar.gz")?>
        </td>
        <td>
          <?php dl_filesize("variman-webapp-" . beta_ver() . ".tar.gz")?>
        </td>
        <td> Web application archive, tar+gzipped. </td>
      </tr>
      <tr>
        <td>
          <?php dl_file("variman-webapp-" . beta_ver() . ".zip")?>
        </td>
        <td>
          <?php dl_filesize("variman-webapp-" . beta_ver() . ".zip")?>
        </td>
        <td> Web application archive, zipped. </td>
      </tr>
      <tr>
        <td class="dl-group" colspan="3">Source Downloads</td>
      </tr>
      <tr>
        <td>
          <?php dl_file("variman-" . beta_ver() . "-src.tar.gz")?>
        </td>
        <td>
          <?php dl_filesize("variman-" . beta_ver() . "-src.tar.gz")?>
        </td>
        <td>Source archive, tar+gzipped</td>
      </tr>
      <tr>
        <td>
          <?php dl_file("variman-" . beta_ver() . "-src.zip")?>
        </td>
        <td>
	  <?php dl_filesize("variman-" . beta_Ver() . "-src.zip")?>
	</td>
        <td>Source archive, zipped</td>
      </tr>
    </table>
    <?php endif; ?>

    <p>
      All versions may be downloaded from <a href="<?php
      ROOT()?>/files/">this directory</a>.
    </p>

    <?php include("../footer.php"); ?>
