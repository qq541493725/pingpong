package ping.task

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import spock.lang.Specification

class HelloTest extends Specification {
    MockWebServer mockBackEnd;

    def "test"() {
        given:
        mockBackEnd.enqueue(new MockResponse().setBody("world"))
        expect:
        new Hello().run()
    }

    def setup() {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    def cleanup() {
        mockBackEnd.shutdown();
    }
}