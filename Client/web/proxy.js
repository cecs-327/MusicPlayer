let fs = require('fs');
const ipc = require('electron').ipcRenderer;



var jsonRequest = {};
/*
@parameters remoteMethod is the title of the method that needs to be called
@parameters arrayOfParameters contain the values for the json object
*/

module.exports.synchExecution = function(remoteMethodName, param)
{

    fs.readFile(__dirname + '/../data/objectReference1.json', (err, rawdata) => {
        if(err)
            console.log(err)
        else{
            data = JSON.parse(rawdata);
            data.forEach(element => {
                if(element.remoteMethod.name == remoteMethodName)
                {
                    jsonRequest.remoteMethod = element.remoteMethod;
                    let i = 0;
                    for(let key in jsonRequest.remoteMethod.param) {
                        jsonRequest.remoteMethod.param[key] = param[i];
                        i++;
                    }
                }
            });
            ipc.send('message-main', JSON.stringify(jsonRequest));
        }
    });
}