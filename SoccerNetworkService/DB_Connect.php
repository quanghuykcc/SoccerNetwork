<?php
class DB_Connect {
    // hàm khởi tạo
    function __construct() {
        
    }

    // hàm hủy
    function __destruct() {
        // $this->close();
    }

    // kết nối đến cơ sở dữ liệu
    public function connect() {
        require_once 'include/config.php';
        // kêt nối đến mysql
        $con = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
        // chọn cơ sở dữ liệu
        mysql_select_db(DB_DATABASE);

        // trả về kết nối cơ sở dữ liệu
        return $con;
    }

    // đóng kết nối tới cơ sở dữ liệu
    public function close() {
        mysql_close();
    }
}
?>
