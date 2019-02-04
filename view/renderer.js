const {getCurrentWindow, globalShortcut} = require('electron').remote;
//Reload function to reload page by pressing F5 or Control+R.
//The purpose is for testing and to quickly show changes to HTML and
//CSS
var reload = ()=>{
    getCurrentWindow().reload()
}

globalShortcut.register('F5', reload);
globalShortcut.register('CommandOrControl+R', reload);

window.addEventListener('beforeunload', ()=>{
    globalShortcut.unregister('F5', reload);
    globalShortcut.unregister('CommandOrControl+R', reload);
})
