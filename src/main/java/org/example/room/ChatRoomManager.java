package org.example.room;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatRoomManager {

    private static final ChatRoomManager instance = new ChatRoomManager();
    private final Map<String, ChatRoom> rooms = new ConcurrentHashMap<>();
    private final ChatRoom lobby;  // 로비는 특별한 채팅방

    private ChatRoomManager() {
        lobby = new ChatRoom("lobby", "Lobby", false);
        rooms.put("lobby", lobby);
    }

    public static ChatRoomManager getInstance() {
        return instance;
    }

    public ChatRoom createGameRoom(String name) {
        String roomId = UUID.randomUUID().toString();
        ChatRoom room = new ChatRoom(roomId, name, true);
        rooms.put(roomId, room);
        return room;
    }

    public void removeRoom(String roomId) {
        if (!"lobby".equals(roomId)) {
            rooms.remove(roomId);
        }
    }

    public ChatRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public ChatRoom getLobby() {
        return lobby;
    }

    public Collection<ChatRoom> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }
}