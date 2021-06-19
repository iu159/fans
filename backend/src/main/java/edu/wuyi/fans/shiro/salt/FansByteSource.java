package edu.wuyi.fans.shiro.salt;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.util.ByteSource;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/13
 */
public class FansByteSource implements ByteSource, Serializable {
    private  byte[] bytes;
    private String cachedHex;
    private String cachedBase64;

    public FansByteSource(){

    }

    public FansByteSource(byte[] bytes) {
        this.bytes = bytes;
    }

    public FansByteSource(char[] chars) {
        this.bytes = CodecSupport.toBytes(chars);
    }

    public FansByteSource(String string) {
        this.bytes = CodecSupport.toBytes(string);
    }

    public FansByteSource(ByteSource source) {
        this.bytes = source.getBytes();
    }

    public FansByteSource(File file) {
        this.bytes = (new FansByteSource.BytesHelper()).getBytes(file);
    }

    public FansByteSource(InputStream stream) {
        this.bytes = (new FansByteSource.BytesHelper()).getBytes(stream);
    }

    public static boolean isCompatible(Object o) {
        return o instanceof byte[] || o instanceof char[] || o instanceof String || o instanceof ByteSource || o instanceof File || o instanceof InputStream;
    }

    @Override
    public byte[] getBytes() {
        return this.bytes;
    }

    @Override
    public boolean isEmpty() {
        return this.bytes == null || this.bytes.length == 0;
    }

    @Override
    public String toHex() {
        if (this.cachedHex == null) {
            this.cachedHex = Hex.encodeToString(this.getBytes());
        }

        return this.cachedHex;
    }

    @Override
    public String toBase64() {
        if (this.cachedBase64 == null) {
            this.cachedBase64 = Base64.encodeToString(this.getBytes());
        }

        return this.cachedBase64;
    }

    @Override
    public String toString() {
        return this.toBase64();
    }

    @Override
    public int hashCode() {
        return this.bytes != null && this.bytes.length != 0 ? Arrays.hashCode(this.bytes) : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof ByteSource) {
            ByteSource bs = (ByteSource)o;
            return Arrays.equals(this.getBytes(), bs.getBytes());
        } else {
            return false;
        }
    }

    private static final class BytesHelper extends CodecSupport {
        private BytesHelper() {
        }

        public byte[] getBytes(File file) {
            return this.toBytes(file);
        }

        public byte[] getBytes(InputStream stream) {
            return this.toBytes(stream);
        }
    }
}
