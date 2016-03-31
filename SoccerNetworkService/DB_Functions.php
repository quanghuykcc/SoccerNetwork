<?php

class DB_Functions {
	
    private $db;
    // hàm khởi tạo
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // hàm hủy
    function __destruct() {
		
    }
	 
    public function isUserExisted($username) {
		mysql_query("set names 'utf8'");
        $result = mysql_query("SELECT username FROM user_frofiles WHERE username = '$username'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows == 1) {
            // tài khoản đã tồn tại
            return true;
        } else {
            // tài khoản không tồn tại
            return false;
        }
    }
	
	// lấy user thông qua username và password
	public function getUserByUsernameAndPassword($username, $password) {
		mysql_query("set names 'utf8'");
        $result = mysql_query("SELECT * FROM user_frofiles WHERE username = '$username' AND password = '$password'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows == 1) {
            return mysql_fetch_assoc($result);
        } 
		else {
            // user không tìm thấy
            return false;
        }
    }
	
	public function getAllCities() {
		mysql_query("set names 'utf8'");
		$result = mysql_query("SELECT * FROM cities");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // có dữ liệu
            return $result;
        } else {
            // không có dữ liệu
            return false;
        }
	}
	
}

?>
