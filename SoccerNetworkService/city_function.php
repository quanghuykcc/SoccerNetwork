<?php
	class CITY_FUNCTION {
		private $db;
		// hàm khởi tạo
		function __construct() {
        	require_once 'db_connect.php';
        	$this->db = new DB_CONNECT();
    	}
    	// hàm hủy
    	function __destruct() {
		
    	}

		function get_all_cities() {
			mysql_query("set names 'utf8'");
			$result = mysql_query("SELECT * FROM cities");
			$no_of_rows = mysql_num_rows($result);
			if ($no_of_rows > 0) {
				while ($row = mysql_fetch_assoc($result)) {
					$responce[] = $row;
				}
				echo json_encode($responce);
			}
			else {
				$responce["code"] = 0;
				$responce["message"] = "Không có thành phố được tìm thấy";
				echo json_encode($responce);
			}
		}

		

	}
?>