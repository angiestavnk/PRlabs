var request = require('request');
const fs = require('fs');
request = request.defaults({jar: true })
const options = {
    url: 'https://www.tekwill.md/api/LoginUser.php',
    headers: {
        host: 'www.tekwill.md',
        connection: 'keep-alive'
    },
    form: {
        email: 'angiestavnk@gmail.com',
        password: '*******'
    }
};

request.post(options, (err, res, body) => {
    if (err) {
        return console.log(err);
    }
    console.log(res.statusCode);
    const cookies = res.headers['set-cookie'].toString();

    const getOptions = {
        method: 'GET',
        url: 'https://www.tekwill.md/account/profile',
         proxy: 'http://37.75.72.45:8080',
        headers: {
            host: 'www.tekwill.md',
            connection: 'keep-alive',
            accept: 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
            referer: 'https://www.tekwill.md/account/profile',
            'Cookie': cookies,
        }
    }
    request.get(getOptions, (err, res, body) => {
    if (err) {
        return console.log(err);
    }
    console.log(res.statusCode);
    fs.writeFile('get.txt',JSON.stringify(body), 'utf8', (err) => {
        if (err) throw err;
        console.log('Get saved!');
    }) 
    fs.writeFile('hrefs.txt', JSON.stringify(body).match(/https:\/\/[-a-zA-Z0-9.]+\.[a-zA-Z]{2,3}/g),  'utf8', (err) => {
        if (err) throw err;
        console.log('Hrefs saved!');
    }) 
    
})
   // console.log(body);
    
    const head = {
        method: 'HEAD',
        url: 'https://www.tekwill.md/account/profile',
        proxy: 'http://37.75.72.45:8080',
        headers: {
            host: 'www.tekwill.md',
            connection: 'keep-alive',
            accept: 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
            referer: 'https://www.tekwill.md/account/profile',
            'Cookie': cookies,
        }
    }
    request.head(head, (err, res, body) => {
        if (err)  {
            return console.log(err);
        }
        console.log(res.headers)
        fs.writeFile('head.txt',JSON.stringify(res.headers), 'utf8', (err) => {
            if (err) throw err;
            console.log('Head saved!');
        })
    })
    const options = {
        method: 'OPTIONS',
        url: 'https://google.com',
        proxy: 'http://37.75.72.45:8080',
        headers: {
            host: 'www.google.com',
            connection: 'keep-alive',
            accept: 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
            referer: 'https://www.google.com',
            'Cookie': cookies,
    }
};
    request(options, (err, res, body) => {
    if (err) {
        return console.log(err);
    }
    fs.writeFile('options.txt',JSON.stringify(res.headers), 'utf8', (err) => {
        if (err) throw err;
        console.log('Option saved!');
    })
});
    

});