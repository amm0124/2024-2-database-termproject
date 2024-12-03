package database.termproject.domain.verifyemail.service;


import database.termproject.domain.verifyemail.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationCleanupService {

    private final EmailRepository emailVerificationRepository;

    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredEmailVerifications() {
        emailVerificationRepository.deleteAllByExpiredAtBefore(LocalDateTime.now());
    }

}
