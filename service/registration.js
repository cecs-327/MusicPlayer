let fs = require('fs');
const electron = require('electron');
const remote = electron.remote;
const url = require('url');
const path = require('path');

function register() {
    let userName = document.getElementById("UserName").value;
    let password = document.getElementById("Password").value;
    let cpassword = document.getElementById("CPassword").value;

    if(password == '' || userName == '' || cpassword == '' || password == null || userName == null || cpassword == null) {
        alert('Must fill out all fields to register');
    } else if(password !== cpassword) {
        alert('Passwords must match');
    } else {
        fs.readFile(__dirname + '/../data/users.json', (err, data) => {
            if(err) console.log(err);
            else {
                let rawData = JSON.parse(data);
                let isUniqueUserName = true;
                rawData['users'].forEach(element => {
                    if(userName == element['UserName']){
                        isUniqueUserName = false;
                    }
                });

                if(!isUniqueUserName) {
                    alert('UserName has already been taken');
                }
                else {
                     rawData['users'].push({
                         'UserName': userName,
                         'Password': password
                     });
                    let json = JSON.stringify(rawData);
                    fs.writeFile(__dirname + '/../data/users.json', json, (err) => {
                        if(err) console.log(err);
                        else {
                            let win = remote.getCurrentWindow();
                            win.loadURL(url.format({
                                pathname: path.join(__dirname, '/../view/login.html'),
                                protocol: 'file',
                                slashes: true
                            }));
                        }
                    });
                }
            }
        });
    }
}
