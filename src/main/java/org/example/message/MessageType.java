package org.example.message;

public enum MessageType {
    // 시스템 메시지
    CONNECT,         // 최초 접속
    DISCONNECT,      // 연결 종료

    // 로비 관련
    LOBBY_CHAT,     // 로비 채팅

    // 게임방 관련
    CREATE_ROOM,    // 방 생성
    JOIN_ROOM,      // 방 입장
    LEAVE_ROOM,     // 방 퇴장
    ROOM_CHAT,      // 방 채팅

    // 게임 진행 관련
    GAME_START,     // 게임 시작
    GAME_END        // 게임 종료
}
