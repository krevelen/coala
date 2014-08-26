/**
 * Created by Alex on 4/24/14.
 */

// prototype, non functional
var MeshImplementation = {

  // required init function
  init : function(options, eve) {
    this.eve = eve;
    if (options.prefix === undefined) {
      this.prefix = "mesh://";
    }
    else { // make the prefix end in a slash
      this.prefix = options.prefix;
      if (this.prefix.slice(-1) != "/") {
        this.prefix += "/";
      }
    }
  },

  /**
   * This function give a mnew message a UID, checks if the address has a prefix, if so constructs the default.
   * It adds the fields required for Eve to deliver messages.
   *
   * @param fullAddress
   * @param messageContent
   * @param agentId
   * @returns {{address: *, origin: *, UID: *, content: *}}
   */
  wrapInEnvelope : function(fullAddress, messageContent, agentId) {
    var UID = getUID();
    var message = {
      address: fullAddress,
      origin: this.prefix + agentId,
      UID: UID,
      content: messageContent
    };
    return message;
  },

  getAgentId : function(address) {
    return address.replace(this.prefix,"");
  },

  receiveMessage : function(message) {
    var agentId = this.getAgentId(message.address);
    var recipientFound = this.eve.routeMessage(message, agentId);
    if (recipientFound == true) {
      // if this message is not a response (has a "method" method)
      if ("method" in message.content) {
        var agent = this.eve.agents[agentId];
        var callback = function (data) {agent.send(message.origin, data, null, message.content.id);};
        this.eve.deliverMessage(message, agentId, callback);
      }
      else {
        this.eve.deliverReply(message, agentId);
      }
    }
    else {
      // if the receipient is not on this node, forward message.
      this.sendMessage(message);
    }
  },

  sendMessage : function(message) {
    // this sends a message to all its connected nodes unless it is in the list of directly connected nodes.

  }




};

module.exports = MeshImplementation;