package database.termproject.domain.verifyemail.service;


import database.termproject.domain.verifyemail.entity.EmailVerification;
import database.termproject.domain.verifyemail.repository.EmailRepository;
import database.termproject.global.error.ProjectError;
import database.termproject.global.error.ProjectException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static database.termproject.global.error.ProjectError.CODE_VALIDATION_EXCEPTION;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final EmailRepository emailRepository;

    public void sendEmail(String toEmail,
                          String title,
                          String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
            throw new ProjectException(ProjectError.MAIL_EXCEPTION);
        }
    }


    public void saveEmailVerification(EmailVerification emailVerification){
        emailRepository.save(emailVerification);
    }


    private SimpleMailMessage createEmailForm(String toEmail,
                                              String title,
                                              String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }

    public EmailVerification getEmailVerification(Long memberId, String email){
        return emailRepository.findByMemberIdAndCode(memberId, email)
                .orElseThrow(() -> new ProjectException(CODE_VALIDATION_EXCEPTION));
    }

    public void removeEmailVerification(EmailVerification emailVerification){
        emailRepository.delete(emailVerification);
    }


}
