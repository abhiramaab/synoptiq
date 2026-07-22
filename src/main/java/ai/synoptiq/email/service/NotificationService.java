package ai.synoptiq.email.service;

import ai.synoptiq.email.dto.response.NotificationResponse;
import ai.synoptiq.email.entity.Notification;
import ai.synoptiq.user.entity.User;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getMyNotifications();
    void markAsRead(Long notificationId);
}
