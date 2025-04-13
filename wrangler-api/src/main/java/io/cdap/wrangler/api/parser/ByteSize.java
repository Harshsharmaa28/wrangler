package io.cdap.wrangler.api.parser;
import com.google.gson.JsonObject;

public class ByteSize implements Token {
    private final long bytes;
    private final String original;

    public ByteSize(String value) {
        this.original = value;
        this.bytes = parseBytes(value);
    }

    private long parseBytes(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Byte size value cannot be null or empty");
        }

        String upperValue = value.trim().toUpperCase();
        try {
            if (upperValue.endsWith("KB")) {
                return (long) (Double.parseDouble(upperValue.replace("KB", "")) * 1024L);
            } else if (upperValue.endsWith("MB")) {
                return (long) (Double.parseDouble(upperValue.replace("MB", "")) * 1024L * 1024L);
            } else if (upperValue.endsWith("GB")) {
                return (long) (Double.parseDouble(upperValue.replace("GB", "")) * 1024L * 1024L * 1024L);
            } else if (upperValue.endsWith("TB")) {
                return (long) (Double.parseDouble(upperValue.replace("TB", "")) * 1024L * 1024L * 1024L * 1024L);
            } else if (upperValue.endsWith("B")) {
                return Long.parseLong(upperValue.replace("B", ""));
            } else {
                return Long.parseLong(upperValue);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid byte size format: " + value, e);
        }
    }

    public long getBytes() {
        return bytes;
    }

    public double getKB() { return bytes / 1024.0; }

    public double getMB() { return bytes / (1024.0 * 1024.0); }

    public double getGB() { return bytes / (1024.0 * 1024.0 * 1024.0); }

    public double getTB() { return bytes / (1024.0 * 1024.0 * 1024.0 * 1024.0); }


    @Override
    public TokenType type() {
        return TokenType.BYTE_SIZE;
    }

    @Override
    public String value() {
        return original;
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", type().name());
        obj.addProperty("value", original);
        obj.addProperty("bytes", bytes);
        return obj;
    }
}
