package com.minecraftplay;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.UUID;

public class PathManager {
    public static final Map<UUID, PathSession> ACTIVE_SESSIONS = new ConcurrentHashMap<>();
}