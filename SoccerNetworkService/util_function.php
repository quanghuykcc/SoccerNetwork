<?php
	class UTIL_FUNCTION {
		// hàm khởi tạo
		function __construct() {

    	}
    	// hàm hủy
    	function __destruct() {
		
    	}

		function send_push_notification($gcm_reg_id, $message) {
        

	        $headers = array("Content-Type:" . "application/json", "Authorization:" . "key=" . "AIzaSyDhORgvy5f0yiWKMncjkHlaDX-jOz6A0Aw");
    		$data = array(
        		'data' => array("message" => $message),
        		'to' => $gcm_reg_id
    		);
 
    		$ch = curl_init();
 
		    curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers ); 
		    curl_setopt( $ch, CURLOPT_URL, "https://android.googleapis.com/gcm/send" );
		    curl_setopt( $ch, CURLOPT_SSL_VERIFYHOST, 0 );
		    curl_setopt( $ch, CURLOPT_SSL_VERIFYPEER, 0 );
		    curl_setopt( $ch, CURLOPT_POST, true );
		    curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
		    curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode($data));
		 
		    $response = curl_exec($ch);
		    curl_close($ch);
    	}

	}
?>