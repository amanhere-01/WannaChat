package com.example.wannachat.model

class Message {
    var message : String?= null
//    var messageId : String?= null
    var senderId : String?= null

    constructor(){}
    constructor(message : String? , /*messageId : String? ,*/  senderId : String?){
        this.message = message
//        this.messageId = messageId
        this.senderId = senderId
    }

}