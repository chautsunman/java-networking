package org.example.msg;

import java.nio.ByteBuffer;

public class BasicMsg {
    private int msgId;
    private long timestamp;

    public BasicMsg() {
        clear();
    }

    public void toByteBuffer(ByteBuffer data) {
        // data should be cleared (e.g. data.clear())
        data.putInt(msgId);
        data.putLong(timestamp);
    }

    public void fromByteBuffer(ByteBuffer data) {
        // data should be flipped and be ready for reading (e.g. data.flip())
        msgId = data.getInt();
        timestamp = data.getLong();
    }

    public void clear() {
        msgId = -1;
        timestamp = -1;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
