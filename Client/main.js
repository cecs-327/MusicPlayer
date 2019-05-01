//Run application with npm start
const { app, BrowserWindow } = require('electron');
const path = require('path');
const url = require('url');
const ipc = require('electron').ipcMain

const dgram = require('dgram');
const client = dgram.createSocket({ type: 'udp4'});
let clientPort = 41235;
let serverPort = 41236;
let host = '127.0.0.1';
//If this window object is not created the browser window will be removed automatically by js garbage collector
let win;

function createWindow() {
    //Create Browser Window
    win = new BrowserWindow({
        width: 800,
        height: 600
    });
    win.maximize();

    //Load index file
    win.loadURL(url.format({

        pathname: path.join(__dirname, './view/login.html'),
        protocol: 'file',
        slashes: true
    }));

    //Open Devtools
    win.webContents.openDevTools();

    win.on('closed', () => {
        win = null;
    });

}
//When app is ready run createWindow function asd
app.on('ready', createWindow);

// Quit when all windows are closed unless they are on a MAC(darwin)
app.on('window-all-closed', () => {
    if(process.platform !== 'darwin') {
        app.quit();
    }
});

client.on('error', (err) => {
    console.log(`client error:\n${err.stack}`);
    client.close();
});

client.on('message', (msg, rinfo) => {
    let response = msg.toString('utf-8').replace(/\\/g, "");
    console.log("Client got:\n\n",response);
    let jsonResponse = JSON.parse(response);
    jsonResponse = jsonResponse['ret'];
    win.webContents.send(jsonResponse['eventListenerName'], jsonResponse);
});

client.on('listening', () => {
    const address = client.address();
    console.log(`client listening ${address.address}:${address.port}`);
});

client.bind(clientPort, host);


ipc.on('message-main', (event, message) => {
    console.log('Message Sent:\n\n', message);
    client.send(Buffer.from(message), 0, message.length, serverPort, host, function(err, bytes) {
        if (err) throw err;
        console.log('UDP message sent to ' + host +':'+ serverPort);
    });
});