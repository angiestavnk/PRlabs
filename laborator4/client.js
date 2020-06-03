const net = require('net');  

const client = new net.Socket();
const port = 3000;
const host = '127.0.0.1';

process.stdin.resume();
process.stdin.setEncoding("utf-8");

var userName = "";
var input_data = "";
var isFirstMessage = true;

client.connect(port, host, function() {
    console.log('Connected');
    console.log('Enter your name:')
});

client.on('data', function(data) {
    let json = JSON.parse(data)
    let type = json.type;
    if (type === 'newUser') {
        console.log(json.name + ' has joined!')
    }
    else if (type === 'newMessage') {
        console.log(json.name + ": " + json.message)
    }
    else {
        console.log('some message from server ' + data.toString())
    }
});

function sendMessage() {
    let data = ""
    input_data = input_data.slice(0, input_data.length - 2);
    if (isFirstMessage) {
        data = {
            name: input_data,
            type: 'newUser'
        }
        data = JSON.stringify(data)
        userName = input_data
        isFirstMessage = false
    }
    else {
        data = {
            name: userName,
            message: input_data,
            type: "newMessage"
        }
        data = JSON.stringify(data)
    }
    client.write(data)
}

process.stdin.on("data", function(input) {
    input_data = input; // Reading input from STDIN
    sendMessage()
 });