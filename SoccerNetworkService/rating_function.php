<?php
	class RATING_FUNCTION {
		private $db;
		// hàm khởi tạo
		function __construct() {
        	require_once 'db_connect.php';
        	$this->db = new DB_CONNECT();
    	}
    	// hàm hủy
    	function __destruct() {
		
    	}

		function set_rating($user_rating_id, $user_rated_id, $value, $rating_type) {
			require_once 'util_function.php';
			$uf = new UTIL_FUNCTION();
			mysql_query("set names 'utf8'");
			$date = date('Y-m-d H:i:s');
			$rating = mysql_query("SELECT * FROM ratings rt WHERE user_rating_id = '$user_rating_id' AND user_rated_id = '$user_rated_id' AND rating_type = '$rating_type'");
			$no_of_rows = mysql_num_rows($rating);
			if ($no_of_rows <= 0) {
				$insert_success = mysql_query("INSERT INTO ratings (user_rating_id, user_rated_id, value, created, rating_type) VALUES ('$user_rating_id', '$user_rated_id', '$value', '$date', '$rating_type')");
				if ($insert_success) {
	        		$gcm_reg_id_tb = mysql_query("SELECT us.gcm_reg_id 
	        				FROM user_profiles us WHERE us.user_id = '$user_rated_id'");
	        		$gcm_reg_id = mysql_result($gcm_reg_id_tb, 0);
		        	$message = "Có người vừa rating cho bạn";
		        	$uf->send_push_notification($gcm_reg_id, $message);	   
			        $responce["code"] = 1;
			        $responce["message"] = "Rating thành công";
			        echo json_encode($responce);	        	
				}
				else {
					$responce["code"] = 0;
					$responce["message"] = "Rating thất bại";
					echo json_encode($responce);
				}
			}
			else {
				$update_success = mysql_query("UPDATE ratings SET value = '$value' WHERE user_rating_id = '$user_rating_id' AND user_rated_id = '$user_rated_id' AND rating_type = '$rating_type'");
				if ($update_success) {
	        		$gcm_reg_id_tb = mysql_query("SELECT us.gcm_reg_id 
	        				FROM user_profiles us WHERE us.user_id = '$user_rated_id'");
	        		$gcm_reg_id = mysql_result($gcm_reg_id_tb, 0);
		        	$message = "Có người vừa rating cho bạn";
		        	$uf->send_push_notification($gcm_reg_id, $message);	   
			        $responce["code"] = 1;
			        $responce["message"] = "Rating thành công";
			        echo json_encode($responce);	        	
				}
				else {
					$responce["code"] = 0;
					$responce["message"] = "Rating thất bại";
					echo json_encode($responce);
				}
			}
			
		}

		function get_user_rating($user_rating_id, $user_rated_id) {
			mysql_query("set names 'utf8'");
			$result = mysql_query("SELECT rt.value, rt.rating_type FROM ratings rt WHERE rt.user_rating_id = '$user_rating_id' AND rt.user_rated_id = '$user_rated_id'");
			$no_of_rows = mysql_num_rows($result);
			if ($no_of_rows > 0) {
				while ($row = mysql_fetch_assoc($result)) {
    				$responce[] = $row;
				}
				echo json_encode($responce);
			}
			else {
				$responce["code"] = 0;
				$responce["message"] = "Không có rating được tìm thấy";
				echo json_encode($responce);
			}
		}

		function get_total_rating($user_rated_id) {
			mysql_query("set names 'utf8'");
			$result = mysql_query("SELECT (SUM(rt.value) / COUNT(rt.user_rating_id)) AS value, rt.rating_type FROM ratings rt WHERE user_rated_id = '$user_rated_id' GROUP BY rt.rating_type");
			$no_of_rows = mysql_num_rows($result);
			if ($no_of_rows > 0) {
				while ($row = mysql_fetch_assoc($result)) {
    				$responce[] = $row;
				}
				echo json_encode($responce);
			}
			else {
				$responce["code"] = 0;
				$responce["message"] = "Không có rating được tìm thấy";
				echo json_encode($responce);
			}
		}



	}
?>