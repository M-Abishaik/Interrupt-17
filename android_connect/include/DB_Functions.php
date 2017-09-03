<?php
 
class DB_Functions {
 
    private $conn;
 
    function __construct() {
        require_once 'DB_Connect.php';
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $phone, $collegename, $password) {
        $uuid = uniqid('', true);
		$singleevent = "F";
      
 
        $stmt = $this->conn->prepare("INSERT INTO userdetails(username, usermail, userphone, usercollege, userpassword) VALUES(?,?,?,?,?)");
        $stmt->bind_param("sssss", $name, $email, $phone, $collegename, $password);
        $result = $stmt->execute();
        $stmt->close();
		
		$stmt1 = $this->conn->prepare("INSERT INTO events(usermail, codeninja, codesprint, cognitionquest, datadequeue, donoflogic, gameofarchives, myb, picturesque) VALUES(?,?,?,?,?,?,?,?,?)");
        $stmt1->bind_param("sssssssss", $email, $singleevent, $singleevent, $singleevent, $singleevent, $singleevent, $singleevent, $singleevent, $singleevent);
        $result1 = $stmt1->execute();
        $stmt1->close();
 
        // check for successful store
        if ($result && $result1) {
            $stmt = $this->conn->prepare("SELECT * FROM userdetails WHERE usermail = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
	
	 public function updateUser($name, $email, $password, $phone, $collegename) {
        $uuid = uniqid('', true);
		
 
        $stmt = $this->conn->prepare("UPDATE userdetails SET username = ?, userpassword = ?, userphone = ?, usercollege = ? WHERE usermail = ?");
        $stmt->bind_param("sssss", $name, $password, $phone, $collegename, $email);
        $result = $stmt->execute();
        $stmt->close();
		
 
        // check for successful store
         if($result){
            $stmt = $this->conn->prepare("SELECT * FROM userdetails WHERE usermail = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
	
	public function updateUserEvents($email, $eventslist){
		
		$codeninja = "F";
		$codesprint = "F";
		$cognitionquest = "F";
		$datadequeue = "F";
		$donoflogic = "F";
		$gameofarchives = "F";
		$myb = "F";
		$picturesque = "F";
		
		$events = explode(",", $eventslist);
		foreach($events as $event) {
		$event = trim($event);
		if($event == "Code Ninja"){
			$codeninja = "T";
		}else if($event == "Code Sprint"){
			$codesprint = "T";
		}else if($event == "Cognition Quest"){
			$cognitionquest = "T";
		}else if($event == "Data De-queue"){
			$datadequeue = "T";
		}else if($event == "Don of Logic"){
			$donoflogic = "T";
		}else if($event == "Game of Archives"){
			$gameofarchives = "T";
		}else if($event == "MYB"){
			$myb = "T";
		}else if($event == "Picturesque"){
			$picturesque = "T";
		}
	}
	
		$stmt = $this->conn->prepare("UPDATE events SET codeninja = ?, codesprint = ?, cognitionquest = ?, datadequeue = ?, donoflogic = ?, gameofarchives = ?, myb = ?, picturesque = ? WHERE usermail = ?");
        $stmt->bind_param("sssssssss", $codeninja, $codesprint, $cognitionquest, $datadequeue, $donoflogic, $gameofarchives, $myb, $picturesque, $email);
        $result = $stmt->execute();
        $stmt->close();
		 
        // check for successful store
         if($result){
			return true;
        } else {
            return false;
        }
	}
 
    
    public function getUserByEmailAndPassword($email, $password) {
 
        $stmt = $this->conn->prepare("SELECT * FROM userdetails WHERE usermail = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifying user password
            $retpass = $user['userpassword'];
            // check for password equality
            if ($retpass == $password) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
	
	public function getUserEvents($email) {
 
        $stmt = $this->conn->prepare("SELECT * FROM events WHERE usermail = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $eventslist = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
			return $eventslist;
            }
		else {
            return NULL;
        }
    }
 
    /**
     * Check user exists or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT usermail from userdetails WHERE usermail = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            $stmt->close();
            return true;
        } else {
            $stmt->close();
            return false;
        }
    }
 
}