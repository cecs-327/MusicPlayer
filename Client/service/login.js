let fs = require('fs');
const electron = require('electron');
const remote = electron.remote;
const url = require('url');
const path = require('path');
const ipc = require('electron').ipcRenderer;
const proxy = require('../web/proxy');

function login() {
    let username = document.getElementById('UserName').value;
    let password = document.getElementById('Password').value;

    if(username == '' || password == '' || username == null || password == null) {
        alert('Please fill in all required fields');
    } else {
        proxy.synchExecution('Login', [username, password]);

        ipc.once('message-login', (event, message) => {
            if(message['success'] == true){
                localStorage.setItem('UserName', username);
                let win = remote.getCurrentWindow();
                win.loadURL(url.format({
                    pathname: path.join(__dirname, '/../view/dashboard.html'),
                    protocol: 'file',
                    slashes: true
                }));
            }else{
                alert('Invalid Credentials');
            }
        });
    }
}