package models;

import java.time.LocalDateTime;

public class NotificationMessage {
    private Long appointmentId;
    private String contactName;
    private String title;
    private LocalDateTime startsAt;
    private String message;

    public NotificationMessage() {
    }

    public NotificationMessage(Long appointmentId, String contactName, String title,
                               LocalDateTime startsAt, String message) {
        this.appointmentId = appointmentId;
        this.contactName = contactName;
        this.title = title;
        this.startsAt = startsAt;
        this.message = message;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
