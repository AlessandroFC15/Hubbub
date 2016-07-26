package com.gia

import grails.test.spock.IntegrationSpec

class PostIntegrationSpec extends IntegrationSpec {

    def "Create a user and add new posts to their account" () {
        given: "A brand new user"
        def user = new User(loginId: 'alessandro', password: '123456')

        when: "we add new posts to their account"
        def post1 = [content: "I'm the greatest!"] as Post
        def post2 = [content: "I would fight that guy in a heartbeat!"] as Post

        user.addToPosts(post1)
        user.addToPosts(post2)

        then: "the posts are successfully added"
        user.posts.size() == 2
    }

    def "Ensure posts linked to a user can be retrieved"() {
        given: "A user with several posts"
        def user = new User(loginId: 'joe', password: 'secret')
        user.addToPosts(new Post(content: "First"))
        user.addToPosts(new Post(content: "Second"))
        user.addToPosts(new Post(content: "Third"))
        user.save(failOnError: true)

        when: "The user is retrieved by their id"
        def foundUser = User.get(user.id)
        def sortedPostContent = foundUser.posts.collect {
            it.content
        }.sort()

        then: "The posts appear on the retrieved user"
        sortedPostContent == ['First', 'Second', 'Third']
    }
}
