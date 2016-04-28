<?php
require("class-Clockwork.php");
$apikey = "a24630654eb405dd89bac43de3dfc8cfac91327a";
$clockwork = new Clockwork($apikey);
$message = array('to' => '84962314802', 'message' => 'This is a test text message');
$done = $clockwork->send($message);
echo json_encode($done);
?>