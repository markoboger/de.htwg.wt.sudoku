$(function() {
    // add a click handler to the button
    $("#getMessageButton").click(function(event) {
        // make an ajax get request to get the message
        jsRoutes.controllers.MessageController.getMessage().ajax({
            success: function(data) {
                console.log(data)
                $(".well").append($("<h1>").text(data.value))
            }
        })
    })
})

$(function() {  

	connect();  

	function connect(){       
		var socket = new WebSocket("ws://localhost:9000/socket");

		message('Socket Status: '+socket.readyState + ' (ready)');  

		socket.onopen = function(){  message('Socket Status: '+socket.readyState+' (open)');  }  ;

		socket.onmessage = function(msg){
			var msg = JSON.parse(msg.data);
			fill_grid(msg); 
		} ;

		socket.onclose = function(){ message('Socket Status: '+socket.readyState+' (Closed)');  }  ;          

		function send(){  
			var grid = "";
			socket.send(grid);  
			message('Sent grid '); 
		}  

		function message(msg){  
			$('#wsLog').append('<p>' + msg +'</p>');  
		}  


	}//End connect  


});  