package io.cdap.wrangler.api.parser;
import com.google.gson.JsonObject;

public class TimeDuration implements Token {
    private final long millis;
    private final String original;

    public TimeDuration(String value) {
        this.original = value;
        this.millis = parseMilliseconds(value);
    }

    private long parseMilliseconds(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Time duration value cannot be null or empty");
        }

        String trimmed = value.trim().toLowerCase();
        try {
            if (trimmed.endsWith("ms")) {
                return (long)(Double.parseDouble(trimmed.replace("ms", "")));
            } else if (trimmed.endsWith("sec")) {
                return (long)(Double.parseDouble(trimmed.replace("sec", "")) * 1000);
            } else if (trimmed.endsWith("s")) {
                return (long)(Double.parseDouble(trimmed.replace("s", "")) * 1000);
            } else if (trimmed.endsWith("h")) {
                return (long)(Double.parseDouble(trimmed.replace("h", "")) * 3600 * 1000);
            } else if (trimmed.endsWith("hour")) {
                return (long)(Double.parseDouble(trimmed.replace("hour", "")) * 3600 * 1000);
            } else if (trimmed.endsWith("hours")) {
                return (long)(Double.parseDouble(trimmed.replace("hours", "")) * 3600 * 1000);
            } else {
                return Long.parseLong(trimmed);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid time duration format: " + value, e);
        }
    }

    public long getMilliseconds() {
        return millis;
    }

    public double getSeconds() {
        return millis / 1000.0;
    }

    public double getMinutes() {
        return millis / (1000.0 * 60);
    }

    public double getHours() {
        return millis / (1000.0 * 60 * 60);
    }

    @Override
    public TokenType type() {
        return TokenType.TIME_DURATION;
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
        obj.addProperty("milliseconds", millis);
        return obj;
    }
}
