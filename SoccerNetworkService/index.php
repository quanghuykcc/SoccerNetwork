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
            $response["msg"] = "Tên đăng nhập hoặc mật khẩu không đúng";
            echo json_encode($response);
			
        }
    } 
	
	// lấy dữ liệu tất cả thành phố
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
            $response["msg"] = "Không có dữ liệu tìm thấy";
            echo json_encode($response);
        }
	}
	
	else {
        echo "Yêu cầu không hợp lệ";
    }
	
}
else {
    echo "Từ chối truy cập";
}
?>
