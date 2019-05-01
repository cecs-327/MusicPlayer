let fs = require('fs');

exports.af = function() {
    fs.readFile(__dirname + '/../data/music.json', (err, rawdata) => {
        if(err) console.log(err);
        else {
            let arr = ['../mp3/bensound-betterdays.mp3', '../mp3/bensound-epic.mp3', '../mp3/bensound-sunny.mp3', '../mp3/bensound-energy.mp3'];
            let data = JSON.parse(rawdata);

            data.forEach(element => {
                let min = 0, max = arr.length;
                let i = Math.floor(Math.random() * (+max - +min)) + +min;
                element['file'] = arr[i];
            });

            let json = JSON.stringify(data);
            fs.writeFile(__dirname + '/../data/music.json', json, (err) => {
                if(err) console.log(err);
            })
        }
    });
};