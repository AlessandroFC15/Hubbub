package com.gia

import grails.test.spock.IntegrationSpec

class UserIntegrationIntegrationSpec extends IntegrationSpec {

    def "Saving our first user to the database" () {
        given: "A brand new user"
        def joe = new User(loginId: 'joe', password: '123456', homepage: 'http://www.grailsinaction.com')

        when: "The user is saved"
        joe.save()

        then: "It saved successfully and can be found in the database"
        joe.errors.errorCount == 0
        joe.id != null
        User.get(joe.id).loginId == joe.loginId
    }

    def "Updating an existing user" () {
        given: "A brand new user"
        def joe = new User(loginId: 'joe', password: '123456', homepage: 'http://www.grailsinaction.com').save()

        when: "Some property is changed"
        joe.loginId = "blah"
        joe.save()

        then: "It updated successfully"
        joe.errors.errorCount == 0
        joe.id != null
        User.get(joe.id).loginId == 'blah'
    }

    def "Deleting an existing user" () {
        given: "An existing user"
        def joe = new User(loginId: 'joe', password: '123456', homepage: 'http://www.grailsinaction.com').save()

        when: "we delete the motherfucker"
        joe.delete(flush: true)

        then: "when we search for him, he is nowhere to be found"
        User.get(joe.id) == null
        ! User.exists(joe.id)
    }

    def "Saving a user with invalid properties causes an error" () {
        given: "a user with invalid loginID"
        def user = new User(loginId: 'joe', password: 'x', homepage: 'not-a-url')

        when: "we try to validate the object"
        user.validate()

        then: 'we found an error'
        user.hasErrors()
    }
}
