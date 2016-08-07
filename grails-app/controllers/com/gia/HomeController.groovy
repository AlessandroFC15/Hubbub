package com.gia

class HomeController {

    def index() {
        [posts: Post.list()]
    }
}
