package com.gia

class User {
    String loginId
    String password
    Date dateCreated

    static hasOne = [profile: Profile]

    static hasMany = [posts: Post, tags: Tag, followingUsers: User]

    static constraints = {
        loginId size: 3..20, unique: true, nullable: false
        password size: 6..8, blank: false, validator: { passwd, user ->
            passwd != user.loginId
        }
        profile nullable: true
    }

    static mapping = {
        posts sort:'dateCreated'
    }

    String toString() {
        profile ? profile.fullName : loginId
    }
}
