let fs = require('fs');

function login() {
    let username = document.getElementById('UserName').value;
    let password = document.getElementById('Password').value;

    if(username == '' || password == '' || username == null || password == null) {
        alert('Please fill in all required fields');
    } else {
        fs.readFile(__dirname + '/../data/users.json', (err, rawData) => {
            if(err) {
                console.log(err);
            }
            else {
                let data = JSON.parse(rawData);
                let isValidCredentials = false;
                data['users'].forEach(element => {
                    if(username == element['UserName'] && password == element['Password']) {
                        isValidCredentials = true;
                    }
                });
                if(!isValidCredentials) alert('UserName or Password Does Not Exist');
                else {
                    alert('Logging you in');
                }
            }
        })
    }
} 
