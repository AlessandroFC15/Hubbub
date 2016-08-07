import com.gia.Post
import com.gia.User

import java.util.regex.*

class BootStrap {

    def init = { servletContext ->
        addQuotesFrom("Conor_McGregor", "http://www.brainyquote.com/quotes/authors/c/conor_mcgregor.html")
    }

    def destroy = {
    }

    def addQuotesFrom(author, link) {
        log.debug("-- Adding quotes")

        def user = User.findOrCreateByLoginId(author)
        user.password = "123456"
        user.save(failOnError: true)

        System.setProperty("http.agent", "Chrome");

        def url = "http://www.brainyquote.com/quotes/authors/c/conor_mcgregor.html".toURL().text

        Matcher m = Pattern.compile(/"view quote">(.*)<\/a>/).matcher(url)

        while (m.find()) {
            def post = new Post(content: m.group(1).substring(0, Math.min(250, m.group(1).size())))
            user.addToPosts(post)
        }

        user.save(flush: true)

        log.debug("-- End of quotes")
    }
}
