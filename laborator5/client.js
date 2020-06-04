const udp = require('dgram')
const conf = require('./config')
var buffer = require('buffer').Buffer;
const path = require('path');
const fs = require('fs');
var screenshot = require('desktop-screenshot');

// creating a client socket
const client = udp.createSocket('udp4')


function encode_base64(filename){
    var buffer =  fs.readFileSync(path.join(__dirname, '/public/',filename))
    const base64 = buffer.toString('base64');
    return base64;
  }

client.on('message', (msg, info) => {
    console.log('Data received from server : ' + msg.toString())
    console.log('Received %d bytes from %s:%d\n', msg.length, info.address, info.port)
})

function sendMessage(image) {
    client.send(image, conf.port, conf.host, error => {
        if(error){
            console.log(error)
            client.close()
        }else{
            console.log('Data sent !!!')
        }
    })
}

function makeScreenshot() {
    screenshot("public/screenshot.jpg", {quality: 0.5, width: 611, height:350}, function(error, complete) {
        if(error)
            console.log("Screenshot failed", error);
        else
            console.log("Screenshot succeeded");
            var image = encode_base64('screenshot.jpg')
            sendMessage(image)
            
    });
}


setInterval(makeScreenshot, 1000)