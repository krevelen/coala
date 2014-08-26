/**
 * Created by Alex on 5/5/14.
 */

/**
 * This function communicates with the agent by a async HTTP POST request.
 *
 * @param {String} url
 * @param {String} method
 * @param {Object} params
 * @param {Function} callback
 */
function askAgent(url,method,params,callback, async) {
  if (async === undefined) {
    async = true;
  }
  // create post request
  var POSTrequest = JSON.stringify({"id":0, "method": method, "params": params});

  // create XMLHttpRequest object to send the POST request
  var http = new XMLHttpRequest();

  // insert the callback function. This is called when the message has been delivered and a response has been received
  http.onreadystatechange = function () {
    if (http.readyState == 4 && http.status == 200) {
      if (callback === undefined || callback === null) {
      }
      else {
        // launch callback function
        callback(JSON.parse(http.responseText));
      }
    }
    else if (http.readyState == 4 && http.status != 200) {
      console.log("Make sure that the Node server has started.");
    }
  };

  // open an asynchronous POST connection
  http.open("POST", url, async);
  // include header so the receiving code knows its a JSON object
  http.setRequestHeader("Content-type", "application/json");
  // send
  http.send(POSTrequest);
}

///**
//* This function communicates with the agent by construnction a HTTP POST request.
//*
//* @param {String} url
//* @param {String} method
//* @param {Object} params
//* @param {Function} callback
//*/
//function publishToEve(url, topic, data) {
//  // create post request
//  var POSTrequest = JSON.stringify({"id":0, "topic": topic, "data": data});
//
//  // create XMLHttpRequest object to send the POST request
//  var http = new XMLHttpRequest();
//
//  // open an asynchronous POST connection
//  http.open("POST", url, true);
//
//  // include header so the receiving code knows its a JSON object
//  http.setRequestHeader("Content-type", "application/json");
//
//  // insert the callback function. This is called when the message has been delivered and a response has been received
//  http.onreadystatechange = function() {
//    if(http.readyState == 4 && http.status == 200) {
//      console.log(http.responseText);
//    }
//  }
//
//  // actually sending the request
//  try {
//    http.send(POSTrequest);
//  }
//  catch(err) {
//    console.log(err);
//    return;
//  }
//}