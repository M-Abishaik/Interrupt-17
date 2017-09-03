<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['email']) && isset($_POST['password'])) {
 
    $email = $_POST['email'];
    $password = $_POST['password'];
 
    // get the user by email and password
    $user = $db->getUserByEmailAndPassword($email, $password);
 
    if ($user != false) {
        // user is found
        $response["error"] = FALSE;
		
		$eventslist = $db->getUserEvents($email);
		if($eventslist != false){
		        $response["eid"] = $eventslist["eid"];
				$response["eventslist"]["codeninja"] = $eventslist["codeninja"];
				$response["eventslist"]["codesprint"] = $eventslist["codesprint"];
				$response["eventslist"]["cognitionquest"] = $eventslist["cognitionquest"];
				$response["eventslist"]["datadequeue"] = $eventslist["datadequeue"];
				$response["eventslist"]["donoflogic"] = $eventslist["donoflogic"];
				$response["eventslist"]["gameofarchives"] = $eventslist["gameofarchives"];
				$response["eventslist"]["myb"] = $eventslist["myb"];
				$response["eventslist"]["picturesque"] = $eventslist["picturesque"];
				
				$response["uid"] = $user["uid"];
				$response["user"]["name"] = $user["username"];
				$response["user"]["email"] = $user["usermail"];
				$response["user"]["password"] = $user["userpassword"];
				$response["user"]["phoneNumber"] = $user["userphone"];
				$response["user"]["collegeName"] = $user["usercollege"];
		
        
		
        echo json_encode($response);
		}
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Please enter valid login credentials.";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters email or password is missing!";
    echo json_encode($response);
}
?>