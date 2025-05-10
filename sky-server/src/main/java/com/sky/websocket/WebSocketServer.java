package com.sky.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.*;

/**
 * WebSocket 服务器类，用于处理与帖子浏览相关的实时连接，实时更新并推送每个帖子的浏览人数
 */
@Component
// 定义 WebSocket 端点的路径，其中 {postId} 是路径参数，表示帖子的 ID
@ServerEndpoint("/ws/post/{postId}")
public class WebSocketServer {

    // 存储每个帖子对应的会话集合，键为帖子 ID，值为该帖子对应的所有客户端会话集合
    private static final Map<Integer, Set<Session>> postSessions = new HashMap<>();

    /**
     * 当客户端与服务器建立 WebSocket 连接时调用此方法
     *
     * @param session 与客户端建立的会话对象
     * @param postId  客户端正在浏览的帖子的 ID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("postId") Integer postId) {
        // 如果 postSessions 中不存在该帖子 ID 对应的会话集合，则创建一个新的 HashSet 并添加到 postSessions 中
        // 然后将会话添加到该集合中
        postSessions.computeIfAbsent(postId, k -> new HashSet<>()).add(session);
        // 向所有正在浏览该帖子的客户端发送最新的浏览人数
        sendViewCountToAll(postId);
    }

    /**
     * 当客户端与服务器的 WebSocket 连接关闭时调用此方法
     *
     * @param session 与客户端建立的会话对象
     * @param postId  客户端正在浏览的帖子的 ID
     */
    @OnClose
    public void onClose(Session session, @PathParam("postId") Integer postId) {
        // 根据帖子 ID 从 postSessions 中获取对应的会话集合
        Set<Session> sessions = postSessions.get(postId);
        // 如果会话集合不为空
        if (sessions != null) {
            // 从会话集合中移除该客户端的会话
            sessions.remove(session);
            // 向所有正在浏览该帖子的客户端发送最新的浏览人数
            sendViewCountToAll(postId);
        }
    }

    /**
     * 当服务器接收到客户端发送的消息时调用此方法
     *
     * @param message 客户端发送的消息
     * @param session 与客户端建立的会话对象
     * @param postId  客户端正在浏览的帖子的 ID
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("postId") Integer postId) {
        // 这里可以添加处理客户端消息的逻辑，目前仅占位
        // 处理客户端消息
    }

    /**
     * 当 WebSocket 连接发生错误时调用此方法
     *
     * @param error 发生的错误对象
     */
    @OnError
    public void onError(Throwable error) {
        // 打印错误堆栈信息
        error.printStackTrace();
    }

    /**
     * 向所有正在浏览指定帖子的客户端发送该帖子的最新浏览人数
     *
     * @param postId 要发送浏览人数的帖子的 ID
     */
    private void sendViewCountToAll(Integer postId) {
        // 根据帖子 ID 从 postSessions 中获取对应的会话集合
        Set<Session> sessions = postSessions.get(postId);
        // 如果会话集合不为空
        if (sessions != null) {
            // 计算该帖子的浏览人数，即会话集合的大小
            int viewCount = sessions.size();
            // 构造包含浏览人数信息的 JSON 格式消息
            String message = "{\"type\": \"viewCount\", \"count\": " + viewCount + "}";
            // 遍历会话集合中的每个会话
            for (Session session : sessions) {
                try {
                    // 向该会话对应的客户端发送消息
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    // 若发送消息时发生异常，打印异常堆栈信息
                    e.printStackTrace();
                }
            }
        }
    }
}