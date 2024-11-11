package ping.util

import spock.lang.Specification
import spock.lang.TempDir

import java.nio.file.Path
import java.util.concurrent.TimeUnit

import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertTrue

class FileLockerTest extends Specification {
    @TempDir
    Path tempDir

    def "test"() {
        given:
        new FileLocker().setPath(tempDir.resolve("lock.tmp").toString())
        when:
        Run success = new Run()
        Run fail = new Run()
        FileLocker.lock(success)
        assertTrue(success.isFlag())
        success.reset()

        FileLocker.lock(success)
        assertTrue(success.isFlag())
        success.reset()

        FileLocker.lock(success, fail)
        assertTrue(fail.isFlag())
        assertFalse(success.isFlag())
        fail.reset()

        TimeUnit.SECONDS.sleep(1L)
        FileLocker.lock(success)
        then:
        success.isFlag()
    }
}

class Run implements Runnable {
    private boolean flag = false;

    @Override
    void run() {
        flag = true
    }

    void reset() {
        flag = false
    }

    boolean isFlag() {
        return flag
    }
}