<?php $tmpl_title = "Digest Calculator";
      $tmpl_location = "Digest Calculator";
      include("../header.php");
?>

<?php
   $realm = "";
   $username = "";
   $password = "";
   $nonce = "";
   $uri = "";
   $method = "";
   $qop = "";
   $cnonce = "";
   $nc = "";
   
   if (!$_GET['clear'])
   {
   $realm  = stripslashes($_GET['realm']);
   $username = stripslashes($_GET['username']);
   $password = stripslashes($_GET['password']);
   $nonce =  stripslashes($_GET['nonce']);
   $uri = stripslashes($_GET['uri']);
   $method = stripslashes($_GET['method']);
   $qop = stripslashes($_GET['qop']);
   $cnonce = stripslashes($_GET['cnonce']);
   $nc = stripslashes($_GET['nc']);
   }

   function text($name, $value)
   {
   echo "<input type=\"text\" name=\"$name\" value='$value' size=\"50\" />";
   }
   ?>


<h3>Digest Calculator</h3>
<p>
  This calculates HTTP digest authentication as per <a
  href="http://www.ietf.org/rfc/rfc2617.txt">RFC 2617</a>.

  <form method="get" focus="username">
    <table border="0" cellspacing="0" cellpadding="5">
      <tr>
        <td>Username:</>
        <td><?php text("username", $username) ?></td>
      </tr>
      <tr>
        <td>Password:</td>
        <td><?php text("password", $password) ?></td>
      </tr>
      <tr>
        <td>Realm:</td>
        <td><?php text("realm", $realm) ?></td>
      </tr>
      <tr>
        <td>qop:</td>
        <td><?php text("qop", $qop) ?></td>
      </tr>
      <tr>
        <td>nonce:</td>
        <td><?php text("nonce", $nonce) ?></td>
      </tr>
      <tr>
        <td>Method:</td>
        <td><?php text("method", $method) ?></td>
      </tr>
      <tr>
        <td>URI:</td>
        <td><?php text("uri", $uri) ?></td>
      </tr>
      <tr>
        <td>cnonce:</td>
        <td><?php text("cnonce", $cnonce) ?></td>
      </tr>
      <tr>
        <td>nc:</td>
        <td><?php text("nc", $nc) ?></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>
          <input type="submit" name="submit" value="Calculate Digest" />
          <input type="submit" name="clear" value="Clear" />
        </td>
      </tr>
    </table>
  </form>
</p>

<?php
   import_request_variables('g', 'r_');
   if ($r_submit)
   {
   $a1 = "$username:$realm:$password";
   $a1_hashed = md5($a1);
   $a2 = "$method:$uri";
   $a2_hashed = md5($a2);
   if ($qop == "auth")
     $response_unhashed =  "$a1_hashed:$nonce:$nc:$cnonce:$qop:$a2_hashed";
   else
     $response_unhashed = "$a1_hashed:$nonce:$a2_hashed";
   $response = md5($response_unhashed);
   echo("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"5\">\n");
   echo("<tr><td>A1:</td> <td> $a1 </td></tr>");
   echo("<tr><td>A1, hashed:</td> <td> $a1_hashed </td></tr>");
   echo("<tr><td>A2:</td> <td> $a2 </td></tr>");
   echo("<tr><td>A2, hashed:</td> <td> $a2_hashed </td></tr>");
   echo("<tr><td>Response, unhashed:</td> <td> $response_unhashed </td></tr>");
   echo("<tr><td>Response:</td> <td> $response </td></tr>");
   echo("</table>\n");
   }
   ?>

<p>


    <?php include("../footer.php"); ?>
