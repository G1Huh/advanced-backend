package com.lion.demo.util;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class TimeUtil {
    
    // 메시지를 받은 지 얼마 안됐을 때 --> "~ 전" 으로 출력
    public static String timeAgo(LocalDateTime dateTime){
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        
        if(duration.toMinutes() < 1){
            return "방금 전";
        }else if(duration.toHours() < 1){
            return duration.toMinutes() + "분 전";
        }else if(duration.toDays() < 1){
            return duration.toHours() + "시간 전";
        }else if(duration.toDays() < 7){
            return duration.toDays() + "일 전";
        }else{
            return dateTime.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
        }

    }

    public String amPmStr(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm", Locale.KOREAN);
        return dateTime.format(formatter);
    }
}
