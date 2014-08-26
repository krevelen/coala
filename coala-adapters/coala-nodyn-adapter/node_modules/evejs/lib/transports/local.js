/**
 * Created by Alex on 4/24/14.
 */
var LocalImplementation = {

  /**
   * required init function
   * @param {Object} options
   * @param {Eve}    eve
   */
  init : function(options, eve) {
    this.eve = eve;
    this.prefix = "local://";
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
    var senderAddress = this.prefix + senderId;
    var agentId = this.getAgentId(receiverAddress);

    // if agent exists in this eve instance
    if (this.eve.agents[agentId] !== undefined) {
      if (message["method"] !== undefined) { // if this message is not a response (has a "method" method)
        var agent = this.eve.agents[agentId];
        var reply = this.eve.deliverMessage(message, agentId, senderId);
        if (reply.result !== undefined) {
          agent.send(senderAddress, reply, null, message.id);
        }
      }
      else {
        this.eve.deliverReply(message, agentId);
      }
    }
    else {
      console.log("Agent does not exist on this eve instance: " + agentId);
    }
  },

  /**
   * required sendMessage function
   *
   * @param {String} receiverAddress  | With prefix
   * @param {Object} messageContent   | JSON-RPC
   * @param {String} senderId         | Without prefix
   */
  sendMessage : function(receiverAddress, messageContent, senderId) {
    this.receiveMessage(receiverAddress, messageContent, senderId);
  }
};

module.exports = LocalImplementation;