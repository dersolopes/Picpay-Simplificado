package com.dersonlopes.picpaysimplificado.services;

import com.dersonlopes.picpaysimplificado.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.math.BigDecimal;
import java.util.Map;

@Slf4j // <-- Abstração do SLF4J injetada de forma limpa pelo Lombok
@Service
public class AuthorizationService {

    private final RestClient restClient;

    public AuthorizationService(@Value("${app.services.authorization.url}") String baseUrl) {
        this.restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    public boolean authorizeTransaction(User sender, BigDecimal value) {
        try {
            ResponseEntity<Map> response = restClient.get()
                .retrieve()
                .toEntity(Map.class);

            //  LOG PROFISSIONAL: Capturando o payload de resposta com nível de INFO ou DEBUG
            log.info("Payload recebido do autorizador externo: {}", response.getBody());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();

                // Estrutura padrão observada nos ambientes modernos do util.devi.tools
                if (body.containsKey("data")) {
                    Map<String, Object> data = (Map<String, Object>) body.get("data");
                    return data != null && Boolean.TRUE.equals(data.get("authorization"));
                }

                if (body.containsKey("authorization")) {
                    return Boolean.TRUE.equals(body.get("authorization"));
                }

                String status = (String) body.get("status");
                if ("success".equalsIgnoreCase(status) || "autorizado".equalsIgnoreCase(status)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            log.error("Falha de infraestrutura ao consumir a API do autorizador externo", e);
            return false;
        }
    }
}
