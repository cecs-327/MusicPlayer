const electron = require('electron');
const remote = electron.remote;
const url = require('url');
const path = require('path');
const proxy = require('../web/proxy');
const ipc = require('electron').ipcRenderer;

function register() {
    let username = document.getElementById("UserName").value;
    let password = document.getElementById("Password").value;
    let cpassword = document.getElementById("CPassword").value;

    if(password == '' || username == '' || cpassword == '' || password == null || username == null || cpassword == null) {
        alert('Must fill out all fields to register');
    } else if(password !== cpassword) {
        alert('Passwords must match');
    } else {
        proxy.synchExecution('registerUser', [username, password]);
        ipc.once('message-registration', (event, message) => {
            console.log("\n\npinged" ,message)
            if(message['data']['success'] == true){
                localStorage.setItem('UserName', username);
                let win = remote.getCurrentWindow();
                win.loadURL(url.format({
                    pathname: path.join(__dirname, '/../view/dashboard.html'),
                    protocol: 'file',
                    slashes: true
                }));
            }
        });
    }
}