<?php $tmpl_title = "Demo";
      $tmpl_location = "Demo";
      include("../header.php");
?>
    <h3>Demo</h3>
    <p>
      A demo of the server is available for anyone to try out.  To
      access it, use the following parameters:
    </p>

    <p>
      <table border="1" cellspacing="0" cellpadding="5">
        <tr>
          <td class="demo-param">URL:</td>
          <td class="demo-value">
            http://demo.crt.realtors.org:6103/rets/login
          </td>
        </tr>
        <tr>
          <td class="demo-param">Username:</td>
          <td class="demo-value">Joe</td>
        </tr>
        <tr>
          <td class="demo-param">Password:</td>
          <td class="demo-value">Schmoe</td>
        </tr>
      </table>
    </p>

    <p>
      Dummy metadata and data have been loaded into the server.  For a
      simple query, try: <code>(STATUS|=A)</code>.  If you do not have
      a RETS client, try out <a
      href="http://cart.sourceforge.net/">Cart</a>.
    </p>

    <?php include("../footer.php"); ?>
