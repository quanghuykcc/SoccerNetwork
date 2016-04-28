<?php
	class USER_FUNCTION {
		private $db;
		// hàm khởi tạo
		function __construct() {
        	require_once 'db_connect.php';
        	$this->db = new DB_CONNECT();
    	}
    	// hàm hủy
    	function __destruct() {
		
    	}

		function login($username, $password, $gcm_reg_id) {
			require_once 'util_function.php';
			$uf = new UTIL_FUNCTION();
			mysql_query("set names 'utf8'");
        	$result = mysql_query("SELECT * FROM user_profiles WHERE username = '$username' AND password = '$password'");
        	$no_of_rows = mysql_num_rows($result);
        	if ($no_of_rows == 1) {
        		mysql_query("UPDATE user_profiles SET gcm_reg_id = '$gcm_reg_id' WHERE username = '$username'");
        		$responce = mysql_fetch_assoc($result);
        		echo json_encode($responce);
        	} 
			else {
	            $responce["code"] = 0;
	            $responce["message"] = "Tài khoản hoặc mật khẩu không chính xác";
	            echo json_encode($responce);

	        }
		}

		function logout($username) {
			mysql_query("set names 'utf8'");
			$result = mysql_query("UPDATE user_profiles SET gcm_reg_id = '' WHERE username = '$username'");
			if ($result != false) {
				$responce["code"] = 1;	
		        $responce["message"] = "Đăng xuất thành công";
		        echo json_encode($responce);
			}
			else {
				$responce["code"] = 0;	
	            $responce["message"] = "Đăng xuất thất bại";
	            echo json_encode($responce);	
			}

		}

		function check_user_exist($username) {
			mysql_query("set names 'utf8'");
			$result = mysql_query("SELECT username FROM user_profiles WHERE username = '$username'");
        	$no_of_rows = mysql_num_rows($result);
        	if ($no_of_rows == 1) {
            	return true;
        	} 
        	else {
        		return false;
        	}
		}

		function register($username, $password, $full_name, $email, $phone_number) {

			// kiểm tra user đã tồn tại chưa?
			mysql_query("set names 'utf8'");
			$user = mysql_query("SELECT username FROM user_profiles WHERE username = '$username'");
        	$no_user = mysql_num_rows($user);
        	if ($no_user == 1) {
            	$exist = true;
        	} 
        	else {
        		$exist = false;
        	}

			if ($exist) {
				$responce["code"] = 0;
	            $responce["message"] = "Tài khoản đã tồn tại";
	            echo json_encode($responce);
			}
			else {
				$date = date('Y-m-d H:i:s');
				$insert_success = mysql_query("INSERT INTO user_profiles(username, password, email, full_name, phone_number, created) VALUES('$username', '$password', '$email', '$full_name', '$phone_number', '$date')");
	        	// kiểm tra nếu insert thành công
	        	if ($insert_success) {
	        		$last_id = mysql_insert_id();
	        		$avatar_path = "user" . $last_id . ".png"	;
	        		mysql_query("UPDATE user_profiles SET avatar_path = '$avatar_path' WHERE username = '$username'");			
	        		$responce["code"] = 1;
	            	$responce["message"] = "user" . $last_id;
	            	echo json_encode($responce);
	        	} 
				else {
	            	$responce["code"] = 0;	
	            	$responce["message"] = "Lỗi đăng ký tài khoản";
	            	echo json_encode($responce);
	        	}
			}
			
		}

		function update_user($user_id, $full_name, $email, $district_id) {
			mysql_query("set names 'utf8'");
			$result = mysql_query("UPDATE user_profiles SET full_name = '$full_name', email = '$email', district_id = '$district_id' WHERE user_id = '$user_id'");
			if ($result) {
				$responce["code"] = 1;
	            $responce["message"] = "Cập nhật thông tin thành công";
	            echo json_encode($responce);
			}
			else {
				$responce["code"] = 0;	
	            $responce["message"] = "Cập nhật thông tin thất bại";
	            echo json_encode($responce);
			}
		}

		function change_password($user_id, $password) {
			mysql_query("set names 'utf8'");
			$result = mysql_query("UPDATE user_profiles SET password = '$password' WHERE user_id = '$user_id'");
			if ($result) {
				$responce["code"] = 1;
	            $responce["message"] = "Cập nhật mật khẩu thành công";
	            echo json_encode($responce);
			}
			else {
				$responce["code"] = 0;	
	            $responce["message"] = "Cập nhật mật khẩu thất bại";
	            echo json_encode($responce);
			}
		}

		function change_phone_number($user_id, $phone_number) {
			mysql_query("set names 'utf8'");
			$result = mysql_query("UPDATE user_profiles SET phone_number = '$phone_number' WHERE user_id = '$user_id'");
			if ($result) {
				$responce["code"] = 1;
	            $responce["message"] = "Cập nhật số điện thoại thành công";
	            echo json_encode($responce);
			}
			else {
				$responce["code"] = 0;	
	            $responce["message"] = "Cập nhật số điện thoại thất bại";
	            echo json_encode($responce);
			}
		}
		
		function get_user_by_id($user_id) {
			mysql_query("set names 'utf8'");
			$result = mysql_query("SELECT us.user_id, us.email, us.phone_number, us.district_id, dt.district_name, ct.city_id, ct.city_name, us.avatar_path, us.full_name FROM user_profiles us left join districts dt on us.district_id = dt.district_id left join cities ct on dt.city_id = ct.city_id WHERE us.user_id = '$user_id'");
			$no_of_rows = mysql_num_rows($result);
        	if ($no_of_rows == 1) {
        		$responce = mysql_fetch_assoc($result);
        		echo json_encode($responce);
        	} 
			else {
	            $responce["code"] = 0;
	            $responce["message"] = "Không tồn tại user này";
	            echo json_encode($responce);

	        }
		}

	}
?>