package ch.nliechti.service

import org.simplejavamail.email.Email
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.MailerBuilder
import org.simplejavamail.mailer.config.TransportStrategy


object MailService {
    fun sendMail(mailTo: String, mailBody: String) {
        val email = composeEmail(mailTo, mailBody)
        sendMail(email)
    }

    private fun composeEmail(mailTo: String, mailBody: String): Email {
        return EmailBuilder.startingBlank()
                .to(mailTo)
                .from("noreply@tbz.ch")
                .withSubject("Login informationen")
                .withReplyTo("noreply@tbz.ch")
                .withPlainText(mailBody)
                .buildEmail()
    }

    private fun sendMail(email: Email) {
        val mailer = MailerBuilder
                .withSMTPServer(
                        System.getenv("EMAIL_SMTP_SERVER"),
                        Integer.valueOf(System.getenv("EMAL_SMTP_PORT")),
                        System.getenv("EMAIL_SMTP_USER"),
                        System.getenv("EMAIL_SMTP_PASSWORD"))
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withSessionTimeout(10 * 1000)
                .buildMailer()
        mailer.sendMail(email)
    }
}