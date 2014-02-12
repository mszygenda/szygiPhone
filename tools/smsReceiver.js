var http = require('http');
var fs = require('fs');
var receiver = "+48505585272"
var inboxFolder = "/tmp/phone-inbox"
var i = 0

http.createServer(function (req, res) {
  try {
    var phoneNum = req.headers["phone"]
    var url = require('url').parse(req.url, true)
    var msg = url.query["text"]

    var formattedMsg =  formatMessage(phoneNum, receiver, msg)
    writeToInbox(formattedMsg) 

    res.writeHead(200, {'Content-Type': 'text/plain'});
    res.end();
  } catch (e) {
    console.log(e)
    try {
      res.end()
    } catch (e2) {

    }
  }
}).listen(9000);

var formatMessage = function (sender, receiver, msg) {
  return sender + "\n##\n" + receiver + "\n##\n" + msg;
}

var writeToInbox = function (msg) {
  var filename = inboxFolder + "/msg." + i;

  fs.writeFile(filename, msg)

  i += 1;
}
