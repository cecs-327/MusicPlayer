//Run application with npm start
const { app, BrowserWindow } = require('electron');
const path = require('path');
const url = require('url');

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
        pathname: path.join(__dirname, './view/registration.html'),
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
