<?php
header("Content-Type: text/html;charset=UTF-8");

if (isset($_POST['tag']) && $_POST['tag'] != '') {
	// thiết lập timezone +7
	date_default_timezone_set("Asia/Bangkok");
	
    // nhận tag được gửi đến
    $tag = $_POST['tag'];

    // kiểm tra loại tag 
    if ($tag == 'login') {
        require_once 'user_function.php';
        $db = new USER_FUNCTION();
        $username = $_POST['username'];
        $password = $_POST['password'];
        $gcm_reg_id = $_POST['gcm_reg_id'];
        $db->login($username, $password, $gcm_reg_id);
    } 

    else if ($tag == 'get_all_fields') {
    	require_once 'field_function.php';
    	$db = new FIELD_FUNCTION();
    	$db->get_all_fields();
    }

    else if ($tag == 'register') {
    	require_once 'user_function.php';
        $db = new USER_FUNCTION();
        $username = $_POST['username'];
        $password = $_POST['password'];
        $full_name = $_POST['full_name'];
        $email = $_POST['email'];
        $phone_number = $_POST['phone_number'];
        $db->register($username, $password, $full_name, $email, $phone_number);
    }

    else if ($tag == 'get_all_cities') {
        require_once 'city_function.php';
        $db = new CITY_FUNCTION();
        $db->get_all_cities();
    }

    else if ($tag == 'get_all_districts') {
        require_once 'district_function.php';
        $db = new DISTRICT_FUNCTION();
        $db->get_all_districts();
    
    }

    else if ($tag == 'get_all_matches') {
        require_once 'match_function.php';
        $db = new MATCH_FUNCTION();
        $db->get_all_matches();
    }

    else if ($tag == 'get_slots_by_match_id') {
        require_once 'slot_function.php';
        $db = new SLOT_FUNCTION();
        $match_id = $_POST['match_id'];
        $db->get_slots_by_match_id($match_id);
    }

    else if ($tag == 'logout') {
        require_once 'user_function.php';
        $db = new USER_FUNCTION();
        $username = $_POST['username'];
        $db->logout($username);   
    }

    else if ($tag == 'add_slot') {
        require_once 'slot_function.php';
        $db = new SLOT_FUNCTION();
        $match_id = $_POST['match_id'];
        $user_id = $_POST['user_id'];
        $quantity = $_POST['quantity'];
        $db->add_slot($match_id, $user_id, $quantity); 
    }

    else if ($tag == 'add_match') {
        require_once 'match_function.php';
        $db = new MATCH_FUNCTION();
        $field_id = $_POST['field_id'];
        $host_id = $_POST['host_id'];
        $maximum_players = $_POST['maximum_players'];
        $price = $_POST['price'];
        $start_time = $_POST['start_time'];
        $end_time = $_POST['end_time'];
        $db->add_match($field_id, $host_id, $maximum_players, $price, $start_time, $end_time); 
    }
    else if ($tag == 'delete_slot') {
        require_once 'slot_function.php';
        $db = new SLOT_FUNCTION();
        $match_id = $_POST['match_id'];
        $user_id = $_POST['user_id'];
        $db->delete_slot($match_id, $user_id);

    }

    else if ($tag == 'update_user') {
        require_once 'user_function.php';
        $db = new USER_FUNCTION();
        $user_id = $_POST['user_id'];
        $full_name = $_POST['full_name'];
        $email = $_POST['email'];
        $district_id = $_POST['district_id'];
        $db->update_user($user_id, $full_name, $email, $district_id);
    }

    else if ($tag == 'change_password') {
        require_once 'user_function.php';
        $db = new USER_FUNCTION();
        $user_id = $_POST['user_id'];
        $password = $_POST['password'];        
        $db->change_password($user_id, $password);
    }

    else if ($tag == 'change_phone_number') {
        require_once 'user_function.php';
        $db = new USER_FUNCTION();
        $user_id = $_POST['user_id'];
        $phone_number = $_POST['phone_number'];        
        $db->change_phone_number($user_id, $phone_number);
    }

    else if ($tag == 'get_user_by_id') {
        require_once 'user_function.php';
        $db = new USER_FUNCTION();
        $user_id = $_POST['user_id'];      
        $db->get_user_by_id($user_id);
    }

    else if ($tag == 'set_rating') {
        require_once 'rating_function.php';
        $db = new RATING_FUNCTION();
        $user_rating_id = $_POST['user_rating_id']; 
        $user_rated_id = $_POST['user_rated_id']; 
        $value = $_POST['value'];      
        $rating_type = $_POST['rating_type']; 
        $db->set_rating($user_rating_id, $user_rated_id, $value, $rating_type);
    }

    else if ($tag == 'get_user_rating') {
        require_once 'rating_function.php';
        $db = new RATING_FUNCTION();
        $user_rating_id = $_POST['user_rating_id']; 
        $user_rated_id = $_POST['user_rated_id']; 
        $db->get_user_rating($user_rating_id, $user_rated_id);
    }

    else if ($tag == 'get_total_rating') {
        require_once 'rating_function.php';
        $db = new RATING_FUNCTION();
        $user_rated_id = $_POST['user_rated_id']; 
        $db->get_total_rating($user_rated_id);
    }

    else if ($tag == 'get_matches_by_field') {
        require_once 'match_function.php';
        $db = new MATCH_FUNCTION();
        $field_id = $_POST['field_id'];
        $db->get_matches_by_field($field_id);
    }
}

?>
