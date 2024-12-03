package database.termproject.domain.verifyemail.service;


import database.termproject.domain.verifyemail.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationCleanupService {

    private final EmailRepository emailVerificationRepository;

    @Scheduled(cron = "0 * * * * *") // 매 정각에 실행 (1시간마다)
    public void deleteExpiredEmailVerifications() {
        emailVerificationRepository.deleteAllByExpiredAtBefore(LocalDateTime.now());
    }

}
