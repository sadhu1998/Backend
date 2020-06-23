    function addUser() {
        var username = document.getElementById("uname").value;
        var phonenumber = document.getElementById("phonenumber").value;
        var bloodgroup = document.getElementById("bloodgroup").value;
        var town = document.getElementById("town").value;
        var district = document.getElementById("district").value;
        var city = document.getElementById("city").value;
        var state = document.getElementById("state").value;
        var country = document.getElementById("country").value;
        var mail_id = document.getElementById("mail_id").value;

        var addUserInput = {
          "url": "http://35.238.212.200:8080/addUser",
          "method": "POST",
          dataType: "json",
          "timeout": 0,
          "headers": {
            "Content-Type": "application/json"
          },
          async: false,
          "data":JSON.stringify({
              "username": username,
              "phonenumber": phonenumber,
              "bloodgroup": bloodgroup,
              "town": town,
              "district": district,
              "city": city,
              "state": state,
              "country": country,
              "mail_id": mail_id
          }),
        };

        var responseajax = $.ajax(addUserInput)
        .done(function (response) {
          console.log("Hi");
          document.getElementById("regSuccess").innerHTML = response;
        });

        document.getElementById("regSuccess").innerHTML = responseajax.responseText;
    }

    function getDetails() {
    var town = document.getElementById("dtown").value;
    var district = document.getElementById("ddistrict").value;
    var city = document.getElementById("dcity").value;
    var state = document.getElementById("dstate").value;
    var bloodgroup = document.getElementById("dbloodgroup").value;
        var settings = {
          "url": "http://localhost:8080/getDetails/"+town+"/"+district+"/"+city+"/"+bloodgroup,
          "method": "GET",
          "timeout": 0,
        };

        $.ajax(settings).done(function (response) {
          console.log(response);
          document.getElementById("detailSuccess").innerHTML = response;
        });
    }


    function validate() {
        var username = document.getElementById("usname").value;
        var password = document.getElementById("pwrd").value;
            var settings = {
              "url": "http://localhost:8080/users/"+username,
              "method": "GET",
              "timeout": 0,
            };

            $.ajax(settings).done(function (response) {
              console.log(response);
              document.getElementById("output").innerHTML = response;

              if (response === password) window.alert("validation Sucessful");
              else window.alert("Wrong credentials Entered");
            });
    }