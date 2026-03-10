package com.elevideo.backend.notification.internal;

import com.elevideo.backend.notification.internal.dto.EmailRequest;
import com.elevideo.backend.notification.internal.dto.EmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * Servicio de infraestructura responsable únicamente de enviar emails.
 * Visibilidad de paquete — solo los listeners lo usan.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final TemplateEngine templateEngine;
    private final RestClient     resendRestClient;

    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${resend.from-email}")
    private String fromEmail;

    @Value("${resend.from-name}")
    private String fromName;

    /**
     * Renderiza una plantilla Thymeleaf y envía el correo resultante.
     *
     * @param to           Destinatario
     * @param subject      Asunto del correo
     * @param templateName Nombre de la plantilla (ej: "emails/welcome-email")
     * @param variables    Variables para la plantilla
     */
    public void sendTemplate(String to, String subject, String templateName, Map<String, Object> variables) {
        log.info("📧 Preparando correo a '{}' con plantilla '{}'", to, templateName);
        String html = renderTemplate(templateName, variables);
        sendHtml(to, subject, html);
    }

    // ----------------------------------------------------------------
    // Helpers privados
    // ----------------------------------------------------------------

    private void sendHtml(String to, String subject, String html) {
        try {
            EmailRequest request = new EmailRequest(
                    String.format("%s <%s>", fromName, fromEmail),
                    to,
                    subject,
                    html
            );

            EmailResponse response = resendRestClient.post()
                    .uri("/emails")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(EmailResponse.class);

            log.info("✅ Correo enviado a '{}'. ID Resend: {}", to, response != null ? response.id() : "N/A");

        } catch (RestClientException e) {
            log.error("❌ Error enviando correo a '{}': {}", to, e.getMessage());
            throw e;
        }
    }

    private String renderTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
}
