package com.dersonlopes.picpaysimplificado.services;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;
import java.util.Map;
import com.dersonlopes.picpaysimplificado.domain.user.User;


@Slf4j
// Este serviço vai bater no link para envio de notificacoes.
@Service
public class NotificationService {

    // Injeta o Rest Client
    private final RestClient restClient;

    // Injeta a URL definida pelo properties no construtor garantindo que o obj. RestClient nao seja nulo
    // SOLID - SRP - A tarefa de ler arquivos e injetar os dados fica 100% por conta do Spring Framework ou Teste.
    // Se colocássemos código de leitura de propriedades (Properties.load()) dentro do AuthorizationService, a classe teria duas responsabilidades:Conectar com o serviço externo de autorização (Regra de negócio).Ler e interpretar arquivos de configuração do disco (Infraestrutura).
    // SOLID - DIP - Ao pedir a URL no construtor, a classe diz: "Eu preciso de uma String com a URL para funcionar. Quem me criar (o Spring ou um Teste Unitário) que se vire para encontrar e me entregar essa String". A classe não se importa se a String veio do application.properties, de uma variável de ambiente do Docker ou de um texto fixo em um teste.
    //  Se a classe utilizasse InputStream ou buscasse a URL direto de um arquivo, ela estaria assumindo a responsabilidade de como e onde encontrar a configuração. Ela estaria "acoplada" ao sistema de arquivos do computador.
    public NotificationService(@Value("${app.services.notification.url}") String baseUrl) {
        // Inicializa o cliente HTTP apontando para a URL base do mock do desafio
        this.restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .build();
    }
    public void sendNotification(User user, String message) {

        String email = user.getEmail();
        // Monta o payload de envio fictício
        Map<String, String> payload = Map.of("email", email, "message", message);

        try {
            // Executa a chamada POST exigida pelo README
            restClient.post()
                .body(payload)
                .retrieve()
                .toBodilessEntity();

            log.info("Notificação enviada com sucesso para: " + email);
        } catch (Exception e) {
            // Regra do desafio: se falhar, não quebra o fluxo principal, apenas avisa o log
            log.warn("Serviço de notificação instável para o destino {}. Detalhe: {}", email, e.getMessage());
        }
    }
}
