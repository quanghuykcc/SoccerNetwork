<?php
	class SLOT_FUNCTION {
		private $db;
		// hàm khởi tạo
		function __construct() {
        	require_once 'db_connect.php';
        	$this->db = new DB_CONNECT();
    	}
    	// hàm hủy
    	function __destruct() {
		
    	}

		function get_slots_by_match_id($match_id) {
			mysql_query("set names 'utf8'");
			$result = mysql_query("SELECT us.user_id, us.full_name, us.avatar_path, us.phone_number, sl.quantity, sl.created FROM slots sl join user_profiles us ON sl.user_id = us.user_id WHERE sl.match_id = '$match_id'");
			$no_of_rows = mysql_num_rows($result);
			if ($no_of_rows > 0) {
				while ($row = mysql_fetch_assoc($result)) {
    				$responce[] = $row;
				}
				echo json_encode($responce);
			}
			else {
				$responce["code"] = 0;
				$responce["message"] = "Không có slot được tìm thấy";
				echo json_encode($responce);
			}
		}

		function add_slot($match_id, $user_id, $quantity) {
			require_once 'util_function.php';
			$uf = new UTIL_FUNCTION();
			mysql_query("set names 'utf8'");
			$date = date('Y-m-d H:i:s');
			$insert_success = mysql_query("INSERT INTO slots(match_id, user_id, quantity, created) VALUES('$match_id', '$user_id', '$quantity', '$date')");
			if ($insert_success) {
				$last_id = mysql_insert_id();
				$result = mysql_query("SELECT us.full_name, us.avatar_path, us.phone_number, sl.quantity, sl.created 
							FROM slots sl join user_profiles us ON sl.user_id = us.user_id WHERE sl.slot_id = '$last_id'");
				
				$no_of_rows = mysql_num_rows($result);
        		if ($no_of_rows == 1) {
	        		
        			$gcm_reg_id_tb = mysql_query("SELECT us.gcm_reg_id 
        					FROM matches mt join user_profiles us ON us.user_id = mt.host_id WHERE match_id = '$match_id'");
        			$gcm_reg_id = mysql_result($gcm_reg_id_tb, 0);
	        		$message = "Có người vừa tham gia vào trận đấu của bạn";
	        		$uf->send_push_notification($gcm_reg_id, $message);
	        		$responce = mysql_fetch_assoc($result);
	        		echo json_encode($responce);
        		} 
				else {
		            $responce["code"] = 0;
		            $responce["message"] = "Lỗi định mệnh";
		            echo json_encode($responce);

	        	}
			}
			else {
				$responce["code"] = 0;
				$responce["message"] = "Thêm slot thất bại";
				echo json_encode($responce);
			}
		}

		function delete_slot($match_id, $user_id) {
			mysql_query("set names 'utf8'");
			$result = mysql_query("DELETE FROM slots WHERE match_id = '$match_id' AND user_id = '$user_id'");
			if ($result) {

				$user_posted = mysql_query("SELECT us.gcm_reg_id, us.full_name 
        					FROM matches mt join user_profiles us ON us.user_id = mt.host_id WHERE match_id = '$match_id'");
        		$gcm_reg_id = mysql_result($user_posted, 0);
        		$full_name = mysql_result($user_posted, 1);
        		$message = "'$full_name' vừa rời khỏi trận đấu của bạn";
	        	$uf->send_push_notification($gcm_reg_id, $message);

				$responce["code"] = 1;
				$responce["message"] = "Xóa slot thành công";
				echo json_encode($responce);
			}
			else {
				$responce["code"] = 0;
				$responce["message"] = "Không có slot được tìm thấy";
				echo json_encode($responce);
			}
		}

	}
?>