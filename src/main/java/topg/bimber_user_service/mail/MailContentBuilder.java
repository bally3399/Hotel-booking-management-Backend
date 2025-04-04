package topg.bimber_user_service.mail;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailContentBuilder {
    private final TemplateEngine templateEngine;

    String build(String message) {
        Context context = new Context();
        context.setVariable("verificationLink", message);
        return templateEngine.process("Mail", context);
    }
}
