/**
 * Created by Alex on 4/24/14.
 */
var websocketImplementation = {

  /**
   * required init function
   * @param {Object} options
   * @param {Eve}    eve
   */
  init : function(options, eve) {
    this.eve = eve;
    this.prefix = "webs://";
    this.port = options.port || 8080;
    this.initiateServer();
  },

  /**
   * strip prefix from adress
   * @param {String} address | agentId with rpefix
   * @returns {String}
   */
  getAgentId : function(address) {
    return address.replace(this.prefix,"");
  },

  /**
   * receive the message
   * @param {String} receiverAddress | With prefix
   * @param {Object} message         | JSON-RPC
   * @param {String} senderId        | Without prefix
   */
  receiveMessage : function(receiverAddress, message, senderId) {

  },

  /**
   * required sendMessage function
   *
   * @param {String} receiverAddress  | With prefix
   * @param {Object} messageContent   | JSON-RPC
   * @param {String} senderId         | Without prefix
   */
  sendMessage : function(receiverAddress, messageContent, senderId) {


  },

  initiateConnection : function(server, message) {
    var WebSocket = require('ws')
    var ws = new WebSocket('ws://www.host.com/path');
    ws.on('open', function() {
      ws.send('something');
    });
    ws.on('message', function(message) {
      console.log('received: %s', message);
    });
  },

  initiateServer : function () {
    if (this.server === undefined) {
      var WebSocketServer = require('ws').Server;
      this.server = new WebSocketServer({port: 8080});
      this.server.on('connection', function(ws) {
        ws.on('message', function(message) {
          console.log('received: %s', message);
        });
        ws.send('something');
      });


      // Put a friendly message on the terminal
      console.log("Server listening at http://127.0.0.1:" + this.port);
    }
    else {
      this.server.close();
      this.server = undefined;
      this.initiateServer();
    }
  }
};

module.exports = websocketImplementation;