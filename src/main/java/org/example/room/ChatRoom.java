package org.example.room;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChatRoom {

    private final String roomId;
    private final String name;
    private final Set<String> participants = Collections.synchronizedSet(new HashSet<>());
    private boolean isGame;

    public ChatRoom(String roomId, String name, boolean isGame) {
        this.roomId = roomId;
        this.name = name;
        this.isGame = isGame;
    }

    public void addParticipant(String userId) {
        participants.add(userId);
    }

    public void removeParticipant(String userId) {
        participants.remove(userId);
    }

    public Set<String> getParticipants() {
        return new HashSet<>(participants);
    }

    public String getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public boolean isGame() {
        return isGame;
    }
}