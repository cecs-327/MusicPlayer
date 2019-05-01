let fs = require('fs');
const electron = require('electron');
const remote = electron.remote;
const url = require('url');
const path = require('path');
let existingTitle = document.getElementById('myText').value;
const proxy = require('../web/proxy');
const ipc = require('electron').ipcRenderer;

//Fetches user currently logged into the session
let userLoggedIn = localStorage.getItem("UserName");
let playListTitle = localStorage.getItem("existingTitle");

//array to hold songs that will be added into the playlist
let songsToAdd = [];
let playlistIndex = 0;


    ipc.on('message-getPlaylistInfo', (event, message) => {
        let data = message['data']
        if(data['success']){
            let songs = data['songs'];
            if(songs != ""){
                //TODO Loading Existing playlists load songs
            }else{
                localStorage.setItem("songsToAdd", JSON.stringify(songsToAdd));
            }
        }else{
            alert('Failed loading playlist');
        }
    })


function playSongs() {
    let musicPlay = document.getElementById('music');
    musicPlay.innerHTML = '';
    songsToAdd = JSON.parse(localStorage.getItem("songsToAdd") || "[]");

    let src = document.createElement('source');
    src.src = songsToAdd[playlistIndex]['MusicFile'];
    musicPlay.appendChild(src);
    musicPlay.load();
    musicPlay.play();
    musicPlay.onended = function() {        
        nextTrackToPlay();
    }
}

function nextTrackToPlay(){
    //If song index has reached the end of playlist, return to first song
    //Or if only one song in playlist, play that song over again
    if(playlistIndex == (songsToAdd.length - 1)){
        playlistIndex = 0;
        playSongs();
    }
    else{
        playlistIndex++;
        playSongs();
    }
}

function addSongToPlaylist(file, title, artist) {
    songsToAdd = JSON.parse(localStorage.getItem("songsToAdd") || "[]");

    songsToAdd.push({
        "SongTitle":title,
        "SongArtist": artist,
        "MusicFile": file
    });
    localStorage.setItem("songsToAdd", JSON.stringify(songsToAdd));
    displayPlaylist(false);
  }

function deletePlaylist() {
    console.log("Attempting to delete playlist");
    let currentPlaylist = document.getElementById("myText").value;
    proxy.synchExecution('deletePlaylist', [currentPlaylist, userLoggedIn]);

    ipc.once('message-deletePlaylist', (event, message) => {
        let data = message['data'];
        if(data['success']){
            returnToDash();
        }else{
            alert('Something went wrong deleting playlist ', data['DeletedPlaylist']);
        }
    });
}

function savePlaylist() {
    songsToAdd = JSON.parse(localStorage.getItem("songsToAdd") || "[]");
    let promise = new Promise((resolve, reject) => {
        songsToAdd.forEach(song => {
            proxy.synchExecution('saveSong', ['message-songAdd', song.MusicFile, song.SongArtist, song.SongTitle]);
        })
        resolve('songs sent');
    });
    promise.then((val) => {
        console.log(songsToAdd);
        proxy.synchExecution('savePlaylist', [userLoggedIn, localStorage.getItem('existingTitle'), 
        document.getElementById('myText').value]);
    });    

    ipc.once('message-savePlaylist', (event, message) => {
        let data = message['data'];
        if(data['success']){
            if(data['titlesuccess']){
                returnToDash();
            }else{
                alert('Cannot set blank or preexisting title');
                document.getElementById('myText').style.borderColor = 'red';
            }
        }else{
            alert('Something went wrong');
        }
    });
}

function displayPlaylist(isFirstDisplay) {
    list = document.getElementById('theList');
    list.innerHTML = '';
    if(isFirstDisplay){
        console.log("First Time logged in");
        if(playListTitle!= ""){
            document.getElementById("myText").value = playListTitle;
            proxy.synchExecution('getAPlaylistsSongsJSON', [playListTitle, userLoggedIn]);

            ipc.on('message-getPlaylistInfo', (event, message) => {
                let data = message['data'];
                if(data['success']){
                    localStorage.setItem("songsToAdd", JSON.stringify(data['Songs']));
                    songsToAdd = JSON.parse(localStorage.getItem("songsToAdd") || "[]");
                    createElements();
                }else{
                    alert('Something went wrong retirieving playlist info');
                }
            })
        }
    }else{
        songsToAdd = JSON.parse(localStorage.getItem("songsToAdd") || "[]");
        createElements();
    }
}

function createElements() {
    let id = 0;
    let songID = id + 'btn';

    songsToAdd.forEach(song => {
        var btn = document.createElement('BUTTON');
        var text = document.createTextNode('Remove Song');
        btn.style.height = '30px';
        btn.style.width = '150px';
        btn.style.backgroundColor = 'black';
        btn.style.color = 'white';
        btn.style.margin = '8px';
        btn.appendChild(text);
        listItem= document.createElement('li');
        listItem.textContent= song['SongTitle'] + ' '+ song['SongArtist'];
        listItem.id = songID;

        btn.onclick = function(){
            let temp = []
            let deletedOne = false;
            songsToAdd.forEach(songObject => {
                if(songObject['SongTitle'] != song['SongTitle'] || songObject['SongArtist'] != song['SongArtist'] || deletedOne == true) {
                    temp.push(songObject);
                } else deletedOne = true;
            });
                localStorage.setItem("songsToAdd", JSON.stringify(temp));
                displayPlaylist(false);
        }


        listItem.appendChild(btn);
        list.appendChild(listItem);
        id += 1;
        songID = id + 'btn';
    });
    localStorage.setItem("songsToAdd", JSON.stringify(songsToAdd));
}

function enterListen(event){
    var x = event.which || event.keyCode;
    if(x == 13)
        search();
}

function search()
{
    let userInput = document.getElementById('search_song').value;
    proxy.synchExecution('searchSong',[userInput, "message-searchSongsPlaylist"])

    ipc.on('message-searchSongsPlaylist', (event, message)=> {
        if(message['success']){
            let list = document.getElementById('resultList');
            list.innerHTML = '';
            let i = 0;
            let id;
            message['data'].forEach(song => {
                id = i + '_song';
                listItem = document.createElement('li');
                listItem.textContent = song['SongTitle'] + ' by: ' + song['SongArtist'];
                listItem.className = 'song_info';
                listItem.id = id;
                listItem.musicFile = song['MusicFile'];
                listItem.songTitle = song['SongTitle'];
                listItem.artist = song['SongArtist'];
                listItem.style.margin = '3px';
                var btn = document.createElement('BUTTON');
                var text = document.createTextNode('Add to Playlist');
                btn.style.height = '30px';
                btn.style.width = '120px';
                btn.style.backgroundColor = 'black';
                btn.style.color = 'white';
                btn.style.margin = '8px';
                btn.appendChild(text);

                btn.onclick = function(){
                    addSongToPlaylist(song['MusicFile'], song['SongTitle'], song['SongArtist']);
                }

                listItem.appendChild(btn);
                list.appendChild(listItem);
                i++;
            })
        }
    });
}

function selectName(){
var x = document.getElementById("myText").value;
document.getElementById("pTitle").innerHTML = x;
}

function returnToDash(){
    localStorage.setItem('songsToAdd', []);
    let window = remote.getCurrentWindow();
    localStorage.setItem('existingTitle', '');
    window.loadURL(url.format({
        pathname: path.join(__dirname, '/../view/dashboard.html'),
        protocol: 'file',
        slashes: true
    }));
}
