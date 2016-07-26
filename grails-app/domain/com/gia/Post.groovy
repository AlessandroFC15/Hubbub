package com.gia

class Post {
    String content
    Date dateCreated

    static belongsTo = [user: User]

    static constraints = {
        content blank: false
    }
}
