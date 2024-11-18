package ping.task

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class HelloTest extends Specification {
    MockWebServer mockBackEnd;
    @Autowired
    Hello hello;

    def "test"() {
        given:
        mockBackEnd.enqueue(new MockResponse().setBody("world"))
        expect:
        hello.run()
    }

    def setup() {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    def cleanup() {
        mockBackEnd.shutdown();
    }
}