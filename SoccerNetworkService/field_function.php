<?php
	class FIELD_FUNCTION {
		private $db;
		// hàm khởi tạo
		function __construct() {
        	require_once 'db_connect.php';
        	$this->db = new DB_CONNECT();
    	}
    	// hàm hủy
    	function __destruct() {
		
    	}

		function get_all_fields() {
			mysql_query("set names 'utf8'");
   			$result = mysql_query("SELECT fd.field_id, fd.field_name, fd.open_time, fd.close_time, fd.district_id, fd.latitude, fd.longitude, fd.address, fd.phone_number, fd.created, ct.city_name, dt.district_name FROM fields fd join districts dt ON fd.district_id = dt.district_id join cities ct ON dt.city_id = ct.city_id");
			$no_of_rows = mysql_num_rows($result);
			if ($no_of_rows > 0) {
				while ($row = mysql_fetch_assoc($result)) {
    				$responce[] = $row;
				}
				echo json_encode($responce);
			}
			else {
				$responce["code"] = 0;
				$responce["message"] = "Không có sân bóng được tìm thấy";
				echo json_encode($responce);
			}
		}

	}
?>