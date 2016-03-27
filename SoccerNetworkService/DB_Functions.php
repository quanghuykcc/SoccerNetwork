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

    public function registerUser($username, $password, $email, $fullName) {
		mysql_query("set names 'utf8'");
		
        $result = mysql_query("INSERT INTO User(Username, Password, Email, FullName) VALUES('$username', '$password', '$email', '$fullName')");
        // kiểm tra nếu insert thành công
        if ($result) {
        	$last_id = mysql_insert_id();
        	$avatar = strval($last_id) . ".png";
        	mysql_query("UPDATE User SET Avatar = '$avatar' WHERE UserID = '$last_id'");
            return $last_id;
        } 
		else {
            return false;
        }
    }
	 
    public function isUserExisted($username) {
		mysql_query("set names 'utf8'");
        $result = mysql_query("SELECT Username FROM User WHERE Username = '$username'");
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
        $result = mysql_query("SELECT * FROM User WHERE Username = '$username' AND Password = '$password'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows == 1) {
            return mysql_fetch_assoc($result);
        } 
		else {
            // user không tìm thấy
            return false;
        }
    }
	
	public function changeUserInformation($username, $field, $value) {
		mysql_query("set names 'utf8'");
		$result = mysql_query("UPDATE User SET $field = '".$value."' WHERE Username = '".$username."'");
		if ($result) {
			return true;
		}
		else {
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
	
	public function getUserByID($userID) {
		mysql_query("set names 'utf8'");
		$result = mysql_query("SELECT UserID, FullName, Avatar FROM User WHERE UserID = '$userID'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows == 1) {
            return mysql_fetch_assoc($result);
        } 
		else {
            // user không tìm thấy
            return false;
        }
	}
	
	public function submitCommentMotel($comment, $userIDPosted, $motelRoomID) {
		mysql_query("set names 'utf8'");
		$date = date('Y-m-d H:i:s');
		$result = mysql_query("INSERT INTO MotelRoomComment(Comment, TimePosted, UserIDPosted, MotelRoomID) VALUES('$comment', '$date', '$userIDPosted', '$motelRoomID')");
        // kiểm tra nếu insert thành công
        if ($result) {
			// lấy id vừa insert vào cơ sở dữ liệu
			$last_id = mysql_insert_id();
			$comment = mysql_query("SELECT * FROM MotelRoomComment WHERE CommentID = '$last_id'");
			$no_of_rows = mysql_num_rows($comment);
			if ($no_of_rows == 1) {
				return mysql_fetch_assoc($comment);
			} 
			else {
				// không tìm thấy comment
				return false;
			}
        } 
		else {
			// không insert comment vào được
            return false;
        }
	}
	
	public function getCommentByMotelRoom($motelRoomID) {
		mysql_query("set names 'utf8'");
		$result = mysql_query("SELECT * FROM MotelRoomComment WHERE MotelRoomID = '$motelRoomID'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // có dữ liệu
            return $result;
        } else {
            // không có dữ liệu
            return false;
        }
	}
	
	public function submitMotelRoom($address, $area, $price, $phone, $latitude, $longitude, $description, $city, $userIDPosted) {
		mysql_query("set names 'utf8'");
		$date = date('Y-m-d H:i:s');
		$result = mysql_query("INSERT INTO MotelRoom(Address, Area, Price, Phone, Latitude, Longitude, Description, City, TimePosted, UserIDPosted) VALUES('$address', '$area', '$price', '$phone', '$latitude', '$longitude', '$description', '$city', '$date', '$userIDPosted')");
        // kiểm tra nếu insert thành công
        if ($result) {
			// lấy id vừa insert vào cơ sở dữ liệu
			$last_id = mysql_insert_id();
			$images = strval($last_id) . ".png";
        	mysql_query("UPDATE MotelRoom SET Images = '$images' WHERE MotelRoomID = '$last_id'");
			$room = mysql_query("SELECT * FROM MotelRoom WHERE MotelRoomID = '$last_id'");
			$no_of_rows = mysql_num_rows($room);
			if ($no_of_rows == 1) {
				return mysql_fetch_assoc($room);
			} 
			else {
				// không tìm thấy phòng
				return false;
			}
        } 
		else {
			// không insert phòng vào được
            return false;
        }
	}
	
	public function searchMotelRoom($city, $price, $area) {
		mysql_query("set names 'utf8'");
		if ($price == "" && $area == "") {
			$result = mysql_query("SELECT * FROM MotelRoom WHERE City = '$city' AND Approved = '1'");
		}
		else if ($price != "" && $area == "") {
			$result = mysql_query("SELECT * FROM MotelRoom WHERE City = '$city' AND Price >= '$price' AND Approved = '1'");
		}
		else if ($price == "" && $area != "") {
			$result = mysql_query("SELECT * FROM MotelRoom WHERE City = '$city' AND Area >= '$area' AND Approved = '1'");
		}
		else {
			$result = mysql_query("SELECT * FROM MotelRoom WHERE City = '$city' AND Area >= '$area' AND Price >= '$price' AND Approved = '1'");
		}
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // có dữ liệu
            return $result;
        } else {
            // không có dữ liệu
            return false;
        }
	}
	
	public function getRoomByUser($userIDPosted) {
		mysql_query("set names 'utf8'");
		$result = mysql_query("SELECT * FROM MotelRoom WHERE UserIDPosted = '$userIDPosted' AND Approved = '1'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // có dữ liệu
            return $result;
        } else {
            // không có dữ liệu
            return false;
        }
	}
	
	public function deleteRoom($motelRoomID) {
		mysql_query("set names 'utf8'");
		$result = mysql_query("DELETE FROM MotelRoom WHERE MotelRoomID = '$motelRoomID'");
		return $result;
	}

	public function submitFeedback($email, $content) {
		mysql_query("set names 'utf8'");
		$date = date('Y-m-d H:i:s');
		$result = mysql_query("INSERT INTO Feedback(Email, Content, TimePosted) VALUES('$email', '$content', '$date')");
        return $result;
	}

	public function submitRating($rating) {
		mysql_query("set names 'utf8'");
		$result = mysql_query("INSERT INTO Rating(Rating) VALUES('$rating')");
        return $result;
	}

	public function getCurrentRating() {
		mysql_query("set names 'utf8'");
		$result = mysql_query("SELECT AVG(Rating) FROM Rating");
        return $result;
	}
	
}

?>
