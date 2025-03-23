package org.example.msg;

import java.nio.ByteBuffer;

public class BasicMsgUtils {
    public static void createMsg(ByteBuffer data, int msgId) {
        // clear the byte buffer
        data.clear();
        final BasicMsg msg = new BasicMsg();
        msg.setMsgId(msgId);
        msg.setTimestamp(System.nanoTime());
        msg.toByteBuffer(data);
    }
}
