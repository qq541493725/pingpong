package ping.util;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Component
public class FileLocker {
    /**
     * 临时文件位置
     */
    private static Path path;
    /**
     * 限流数量
     */
    private static Integer current;

    @Value("${tmpPath:./lock.tmp}")
    public void setPath(String path) throws IOException {
        FileLocker.path = Path.of(path);
        if (!Files.exists(FileLocker.path)) {
            Files.createFile(FileLocker.path);
        }
    }

    public static void lock(Runnable success) {
        lock(success, null);
    }

    @SneakyThrows
    public synchronized static void lock(Runnable success, Runnable fail) {
        @Cleanup FileChannel channel = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);
        FileLock lock = channel.tryLock(0, Long.MAX_VALUE, false);
        // 计数器while
        if (lock != null) {
            boolean flag = checkAndUpdateFile(channel);
            lock.release();
            // 调用
            if (flag && success != null) {
                success.run();
                return;
            }
        }
        if (fail != null) {
            fail.run();
        }
    }

    private static boolean checkAndUpdateFile(FileChannel channel) throws IOException {
        // 限流
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES * 2);
        long now = System.currentTimeMillis();
        long time1 = 0;
        long time2 = 0;
        if (channel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            if (byteBuffer.limit() >= Long.BYTES * 2) {
                time1 = byteBuffer.getLong();
                time2 = byteBuffer.getLong(Long.BYTES);
            }
        }
        if (time1 == 0) {
            time1 = now;
        } else if (time2 == 0) {
            time2 = now;
        } else {
            // 判断第一个是否过期
            if ((now - time1) < 1000) {
                return false;
            } else {
                // 过期
                time1 = time2;
                time2 = now;
            }
        }
        // 写入文件
        byteBuffer.clear();
        byteBuffer.putLong(0, time1);
        byteBuffer.putLong(Long.BYTES, time2);
        channel.position(0);
        channel.write(byteBuffer);
        channel.force(false);
        return true;
    }
}
