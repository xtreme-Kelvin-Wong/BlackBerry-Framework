package com.xtremelabs.xtremeutil.util.json.parser;

import com.xtremelabs.xtremeutil.util.json.JsonException;
import com.xtremelabs.xtremeutil.util.json.pull.Json;

public interface JsonListener {
    public void keyFound(String key, Json parser) throws JsonException;
}
