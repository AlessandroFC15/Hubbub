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

    def "Exercise tagging several posts with various tags"() {
        given: "A user with a set of tags"
        def user = new User(loginId: 'joe', password: 'secret')
        def tagGroovy = new Tag(name: 'groovy')
        def tagGrails = new Tag(name: 'grails')
        user.addToTags(tagGroovy)
        user.addToTags(tagGrails)
        user.save(failOnError: true)

        when: "The user tags two fresh posts"
        def groovyPost = new Post(content: "A groovy post")
        user.addToPosts(groovyPost)
        groovyPost.addToTags(tagGroovy)

        def bothPost = new Post(content: "A groovy and grails post")
        user.addToPosts(bothPost)
        bothPost.addToTags(tagGroovy)
        bothPost.addToTags(tagGrails)

        then:
        user.tags*.name.sort() == [ 'grails', 'groovy']
        1 == groovyPost.tags.size()
        2 == bothPost.tags.size()
    }

    def "Ensure a user can follow other users"() {
        given: "A brand new user"
        def user = new User(loginId: 'joe', password: 'secret')
        user.save(failOnError: true)

        when: "the user follows other users"
        def userToBeFollowed = new User(loginId: 'bill', password: 'secret')
        def userToBeFollowed2 = new User(loginId: 'ale', password: 'secret')

        userToBeFollowed.save(failOnError: true)
        userToBeFollowed2.save(failOnError: true)

        user.addToFollowingUsers(userToBeFollowed)
        user.addToFollowingUsers(userToBeFollowed2)

        then: "the users followed show up in the list of following"
        user.followingUsers.size() == 2
        user.followingUsers.toList().collect{ it.loginId }  == ['bill', 'ale']
    }
}
