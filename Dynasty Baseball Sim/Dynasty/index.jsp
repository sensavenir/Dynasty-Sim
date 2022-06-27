<!DOCTYPE html>
<html>
<head>
<style>
body {
  margin: 0;
  background-color: #606060;
  font-family: 'Courier New', monospace;
}

ul.sidenav {
  list-style-type: none;
  margin: 0;
  padding: 0;
  width: 15%;
  background-color: #333;
  position: fixed;
  height: 100%;
  overflow: auto;
  border: 4px solid #04AA6D;
  border-radius: 10px
}

li a, .dropbtn {
  display: block;
  color: white;
  padding: 16px 16px;
  text-decoration: none;
}

li a.active {
  background-color: #04AA6D;
  color: white;
}

li a:hover:not(.active), .dropdown:hover {
  background-color: #04AA6D;
  color: white;
}

li.dropdown .dropbtn {
  display: block;
  background-color: #333;
  color: white;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #333;
  min-width: 160px;  
  z-index: 1;
}

.dropdown-content a {
  color: black;
  padding: 12px 16px;
  text-decoration: none;  
  display: block;
  text-align: left;
}

.dropdown-content a:hover {background-color: #04AA6D;}

.dropdown:hover .dropdown-content {
  display: block;
}

</style>
</head>

<body>

<ul class="sidenav">
  <li class="dropdown">
    <a href="/teams" class="dropbtn">Teams</a>
    <div class="dropdown-content">
      <a href="#" class="dropbtn" >Team 1</a>      
      <a href="#" class="dropbtn">Team 2</a>      
      <a href="#" class="dropbtn">Team 3</a>
      </div>
  </li>
  <li><a href="/messages">Messages</a></li>
  <li><a href="/leagues">Leagues</a></li>
</ul>

</body>
</html>
