<?php
	class MATCH_FUNCTION {
		private $db;
		// hàm khởi tạo
		function __construct() {
        	require_once 'db_connect.php';
        	$this->db = new DB_CONNECT();
    	}
    	// hàm hủy
    	function __destruct() {
		
    	}

		function get_all_matches() {
			mysql_query("set names 'utf8'");
			$result = mysql_query("SELECT mt.match_id, us.user_id, us.full_name, us.avatar_path, us.phone_number, mt.start_time, mt.end_time, mt.price, mt.maximum_players, mt.created, fd.field_name, fd.address, fd.latitude, fd.longitude, ct.city_name, dt.district_name, sum(sl.quantity) as attended FROM matches mt join fields fd ON fd.field_id = mt.field_id join districts dt ON fd.district_id = dt.district_id join cities ct ON ct.city_id = dt.district_id join user_profiles us ON us.user_id = mt.host_id LEFT JOIN slots sl ON sl.match_id = mt.match_id GROUP BY mt.match_id");
			$no_of_rows = mysql_num_rows($result);
			if ($no_of_rows > 0) {
				while ($row = mysql_fetch_assoc($result)) {
					$responce[] = $row;
				}
				echo json_encode($responce);
			}
			else {
				$responce["code"] = 0;
				$responce["message"] = "Không có trận đấu được tìm thấy";
				echo json_encode($responce);
			}
		}

		function get_matches_by_field($field_id) {
			mysql_query("set names 'utf8'");
			$result = mysql_query("SELECT mt.match_id, us.user_id, us.full_name, us.avatar_path, us.phone_number, mt.start_time, mt.end_time, mt.price, mt.maximum_players, mt.created, fd.field_name, fd.address, fd.latitude, fd.longitude, ct.city_name, dt.district_name, sum(sl.quantity) as attended FROM matches mt join fields fd ON fd.field_id = mt.field_id join districts dt ON fd.district_id = dt.district_id join cities ct ON ct.city_id = dt.district_id join user_profiles us ON us.user_id = mt.host_id LEFT JOIN slots sl ON sl.match_id = mt.match_id WHERE mt.field_id = '$field_id' GROUP BY mt.match_id");
			$no_of_rows = mysql_num_rows($result);
			if ($no_of_rows > 0) {
				while ($row = mysql_fetch_assoc($result)) {
					$responce[] = $row;
				}
				echo json_encode($responce);
			}
			else {
				$responce["code"] = 0;
				$responce["message"] = "Không có trận đấu được tìm thấy";
				echo json_encode($responce);
			}
		}

		function add_match($field_id, $host_id, $maximum_players, $price, $start_time, $end_time) {
			mysql_query("set names 'utf8'");
			$date = date('Y-m-d H:i:s');
			$insert_success = mysql_query("INSERT INTO matches(field_id, host_id, maximum_players, price, start_time, end_time, created) VALUES('$field_id', '$host_id', '$maximum_players', '$price', '$start_time', '$end_time', '$date')");
			if ($insert_success) {
				$last_id = mysql_insert_id();
				$result = mysql_query("SELECT mt.match_id, us.full_name, us.avatar_path, mt.start_time, mt.end_time, mt.price, mt.maximum_players, mt.created, fd.field_name, fd.address, fd.latitude, fd.longitude, ct.city_name, dt.district_name, sum(sl.quantity) as attended FROM matches mt join fields fd ON fd.field_id = mt.field_id join districts dt ON fd.district_id = dt.district_id join cities ct ON ct.city_id = dt.district_id join user_profiles us ON us.user_id = mt.host_id LEFT JOIN slots sl ON sl.match_id = mt.match_id WHERE mt.match_id = '$last_id' GROUP BY mt.match_id");
				$no_of_rows = mysql_num_rows($result);
        		if ($no_of_rows == 1) {	    
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
				$responce["message"] = "Thêm trận đấu thất bại";
				echo json_encode($responce);
			}
		}

		

		function delete_match($match_id,$user_id) {
			require_once 'util_function.php';
			$uf = new UTIL_FUNCTION();
			mysql_query("set names 'utf8'");
			$result = mysql_query("DELETE FROM matches WHERE match_id = '$match_id'");
			if ($result) {

				$user_joined = mysql_query("SELECT us.gcm_reg_id, us.full_name 
        					FROM slots sl join user_profiles us ON us.user_id = sl.user_id WHERE match_id = '$match_id'");
				while($row=mysql_fetch_array($user_joined))
        		{
				$gcm_reg_id = $row['gcm_reg_id'];
        		$full_name = $row['full_name'];
        		$message = "'$full_name' vừa hủy trận đấu của anh ấy.";
	        	$uf->send_push_notification($gcm_reg_id, $message);
                }
				$responce["code"] = 1;
				$responce["message"] = "Xóa match thành công";
				echo json_encode($responce);
			}
			else {
				$responce["code"] = 0;
				$responce["message"] = "Không có match được tìm thấy";
				echo json_encode($responce);
			}
		}
        function search_match($field_id,$price,$start_time)
		{
		  mysql_query("set names 'utf8'");
			$result = mysql_query("SELECT mt.match_id, us.user_id, us.full_name, us.phone_number, mt.start_time, mt.end_time, mt.price, mt.maximum_players, mt.created, fd.field_name, fd.address, fd.latitude, fd.longitude, ct.city_name, dt.district_name, sum(sl.quantity) as attended FROM matches mt join fields fd ON fd.field_id = mt.field_id join districts dt ON fd.district_id = dt.district_id join cities ct ON ct.city_id = dt.district_id join user_profiles us ON us.user_id = mt.host_id LEFT JOIN slots sl ON sl.match_id = mt.match_id WHERE (mt.field_id = '$field_id' and mt.price='$price') and start_time=convert(DateTime,'$start_time')  GROUP BY mt.match_id");
			$no_of_rows = mysql_num_rows($result);
			if ($no_of_rows > 0) {
				while ($row = mysql_fetch_assoc($result)) {
					$responce[] = $row;
				}
				echo json_encode($responce);
			}
			else {
				$responce["code"] = 0;
				$responce["message"] = "Không có trận đấu được tìm thấy";
				echo json_encode($responce);
			}
		}
	}
?>