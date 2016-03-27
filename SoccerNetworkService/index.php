<?php
header("Content-Type: text/html;charset=UTF-8");

if (isset($_POST['tag']) && $_POST['tag'] != '') {
	// thiết lập timezone +7
	date_default_timezone_set("Asia/Bangkok");
	
    // nhận tag được gửi đến
    $tag = $_POST['tag'];

    // thêm đối tượng xử lý cơ sở dữ liệu
    require_once 'DB_Functions.php';
    $db = new DB_Functions();

    // kiểm tra loại tag 
    if ($tag == 'login') {
        // đăng nhập
        $username = $_POST['username'];
        $password = $_POST['password'];
		$result = $db->getUserByUsernameAndPassword($username, $password);
        if ($result != false) {
			echo json_encode($result);
        } 
		else {
            $response["code"] = 0;
            $response["error_msg"] = "Tên đăng nhập hoặc mật khẩu không đúng";
            echo json_encode($response);
			
        }
    } 
	else if ($tag == 'register') {
        // đăng ký tài khoản
        $username = $_POST['username'];
		$password = $_POST['password'];
        $email = $_POST['email'];
		$fullName = $_POST['fullName'];
		
        // kiểm tra nếu user đã tồn tại hay chưa
        if ($db->isUserExisted($username)) {
            // tồn tại - thông báo lỗi
            $response["code"] = 0;
            $response["error_msg"] = "Tài khoản đã tồn tại";
            echo json_encode($response);
        } 
		else {
            // lưu trữ tài khoản đăng ký
            $result = $db->registerUser($username, $password, $email, $fullName);
            if ($result != false) {
				$response["code"] = 1;
				$response["UserID"] = $result;
				echo json_encode($response);
			} 
			else {
                // lưu trữ thất bại
                $response["code"] = 0;
                $response["error_msg"] = "Lỗi đăng ký tài khoản";
                echo json_encode($response);
            }
        }
    } 
	
	else if ($tag == 'submit_field') {
		$username = $_POST['username'];
		$field = $_POST['field'];
		$value = $_POST['value'];
		$result = $db->changeUserInformation($username, $field, $value);
		if ($result != false) {
			$response["code"] = 1;
			echo json_encode($response);
		} 
		else {
            // lưu trữ thất bại
            $response["code"] = 0;
            $response["error_msg"] = "Lỗi thay đổi thông tin tài khoản";
            echo json_encode($response);
        }
	}
	
	// lấy dữ liệu tất cả phòng trọ
	else if ($tag == 'get_all_cities') {
		$result = $db->getAllCities();
		if ($result != false) {
			while ($row = mysql_fetch_assoc($result)) {
				$motelRooms[] = $row;
			}
			echo json_encode($motelRooms);
		} 
		else {
            // lấy dữ liệu thất bại
            $response["code"] = 0;
            $response["error_msg"] = "Không có dữ liệu tìm thấy";
            echo json_encode($response);
        }
	}

	else if ($tag == 'get_user_by_id') {
		$userID = $_POST['userID'];
		$result = $db->getUserByID($userID);
		if ($result != false) {
			echo json_encode($result);
        } 
		else {
            $response["code"] = 0;
            $response["error_msg"] = "Không có tài khoản tương ứng với ID trên";
            echo json_encode($response);
			
        }
	}
	
	else if ($tag == 'submit_comment_motel') {
		$comment = $_POST['comment'];
		$userIDPosted = $_POST['userIDPosted'];
		$motelRoomID = $_POST['motelRoomID'];
		$result = $db->submitCommentMotel($comment, $userIDPosted, $motelRoomID);
        if ($result != false) {
			echo json_encode($result);
		} 
		else {
            // lưu trữ thất bại
            $response["code"] = 0;
            $response["error_msg"] = "Lỗi đăng bình luận";
            echo json_encode($response);
        }
	}
	
	else if ($tag == 'get_comment_by_motelroom') {
		$motelRoomID = $_POST['motelRoomID'];
		$result = $db->getCommentByMotelRoom($motelRoomID);
		if ($result != false) {
			while ($row = mysql_fetch_assoc($result)) {
				$comments[] = $row;
			}
			echo json_encode($comments);
		} 
		else {
            // tìm kiếm không có bình luận
            $response["code"] = 0;
            $response["error_msg"] = "Không có dữ liệu tìm thấy";
            echo json_encode($response);
        }
	}
	
	else if ($tag == 'submit_motelroom') {
		$address = $_POST['address'];
		$area = $_POST['area'];
		$price = $_POST['price'];
		$phone = $_POST['phone'];
		$latitude = $_POST['latitude'];
		$longitude = $_POST['longitude'];
		$description = $_POST['description'];
		$city = $_POST['city'];
		$userIDPosted = $_POST['userIDPosted'];
		$result = $db->submitMotelRoom($address, $area, $price, $phone, $latitude, $longitude, $description, $city, $userIDPosted);
        if ($result != false) {
			echo json_encode($result);
		} 
		else {
            // lưu trữ thất bại
            $response["code"] = 0;
            $response["error_msg"] = "Lỗi đăng phòng";
            echo json_encode($response);
        }
	}
	
	else if ($tag == 'search_motel_room') {
		$city = $_POST['city'];
		$price = $_POST['price'];
		$area = $_POST['area'];
		$result = $db->searchMotelRoom($city, $price, $area);
		if ($result != false) {
			while ($row = mysql_fetch_assoc($result)) {
				$motelRooms[] = $row;
			}
			echo json_encode($motelRooms);
		} 
		else {
            // lấy dữ liệu thất bại
            $response["code"] = 0;
            $response["error_msg"] = "Không có dữ liệu tìm thấy";
            echo json_encode($response);
        }
	}
	
	else if ($tag == 'get_room_by_user') {
		$userIDPosted = $_POST['userIDPosted'];
		$result = $db->getRoomByUser($userIDPosted);
		if ($result != false) {
			while ($row = mysql_fetch_assoc($result)) {
				$motelRooms[] = $row;
			}
			echo json_encode($motelRooms);
		} 
		else {
            // lấy dữ liệu thất bại
            $response["code"] = 0;
            $response["error_msg"] = "Không có dữ liệu tìm thấy";
            echo json_encode($response);
        }
	}
	
	else if ($tag == 'delete_room') {
		$motelRoomID = $_POST['motelRoomID'];
		$result = $db->deleteRoom($motelRoomID);
		if ($result != false) {
			$response["code"] = 1;
			echo json_encode($response);
		} 
		else {
            // lấy dữ liệu thất bại
            $response["code"] = 0;
            $response["error_msg"] = "Xóa phòng trọ thất bại";
            echo json_encode($response);
        }
	}

	else if ($tag == 'submit_feedback') {
		$email = $_POST['email'];
		$content = $_POST['content'];
		$result = $db->submitFeedback($email, $content);
        if ($result != false) {
			$response["code"] = 1;
            echo json_encode($response);
		} 
		else {
            // lưu trữ thất bại
            $response["code"] = 0;
            $response["error_msg"] = "Lỗi đăng phản hồi";
            echo json_encode($response);
        }
	}

	else if ($tag == 'submit_rating') {
		$rating = $_POST['rating'];
		$result = $db->submitRating($rating);
        if ($result != false) {
			$response["code"] = 1;
            echo json_encode($response);
		} 
		else {
            // lưu trữ thất bại
            $response["code"] = 0;
            $response["error_msg"] = "Lỗi đăng rating";
            echo json_encode($response);
        }
	}

	else if ($tag == 'get_current_rating') {
		$result = $db->getCurrentRating();
		while ($row = mysql_fetch_assoc($result)) {
			$avg[] = $row;
		}
		echo json_encode($avg); 
	}
	
	


	
	else {
        echo "Yêu cầu không hợp lệ";
    }
	
}
else {
    echo "Từ chối truy cập";
}
?>
