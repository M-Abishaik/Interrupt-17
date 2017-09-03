<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if(isset($_POST['email']) && isset($_POST['eventslist'])) {
 
    $eventslist = $_POST['eventslist'];
    $email = $_POST['email'];
 
        // create a new events row for user
        $user = $db->updateUserEvents($email, $eventslist);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
			echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in events update!";
            echo json_encode($response);
        }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters are missing!";
    echo json_encode($response);
}
?>