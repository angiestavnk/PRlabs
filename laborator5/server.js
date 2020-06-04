const udp = require('dgram')
const conf = require('./config')
const fs = require('fs');
const uuid = require('uuid/v4');
// --------------------creating a udp server --------------------

// creating a udp server
const server = udp.createSocket('udp4')

// emits when any error occurs
server.on('error', (error) => {
    console.log("udp_server", "error", error)
    server.close()
})

// emits on new datagram msg
server.on('message', (msg, info) => {

    const filename = 'images/' + uuid() + '.jpg'
    saveImage(msg.toString(), 'test.jpg');
    saveImage(msg.toString(), filename);


})  // end server.on

//emits when socket is ready and listening for datagram msgs
server.on('listening', () => {
    const address = server.address()
    const port = address.port
    const family = address.family
    const ipaddr = address.address

    console.log("udp_server", "info", 'Server is listening at port ' + port)
    console.log("udp_server", "info", 'Server ip :' + ipaddr)
    console.log("udp_server", "info", 'Server is IP4/IP6 : ' + family)
})

//emits after the socket is closed using socket.close()
server.on('close', () => {
    console.log("udp_server", "info", 'Socket is closed !')
})

server.bind(conf.port, conf.serverHost)

function saveImage(imageData, fileName) {
       
    fs.writeFile(fileName, imageData, {encoding: 'base64'}, function(err) {
        if (!err) {
            console.log('File created');
        }
        else {
            console.log(err)
        }
    });
}