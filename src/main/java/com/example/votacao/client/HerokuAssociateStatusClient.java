package com.example.votacao.client;

import com.example.votacao.client.dto.UserInfoResponse;
import com.example.votacao.config.properties.UserInfoProperties;
import com.example.votacao.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
@Component
public class HerokuAssociateStatusClient implements AssociateStatusClient {

    private final RestClient restClient;
    private final UserInfoProperties properties;

    public HerokuAssociateStatusClient(UserInfoProperties properties) {
        this.properties = properties;
        org.springframework.http.client.SimpleClientHttpRequestFactory requestFactory =
                new org.springframework.http.client.SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.connectTimeoutMs());
        requestFactory.setReadTimeout(properties.readTimeoutMs());

        this.restClient = RestClient.builder()
                .baseUrl(properties.baseUrl())
                .requestFactory(requestFactory)
                .build();
    }

    @Override
    public boolean canVote(String cpf) {
        if (!properties.enabled()) return true;
        String sanitizedCpf = sanitizeCpf(cpf);

        try {
            UserInfoResponse response = restClient.get()
                    .uri("/users/{cpf}", sanitizedCpf)
                    .retrieve()
                    .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(), (request, clientResponse) -> {
                        throw new BusinessException("CPF inválido ou não encontrado para validação externa");
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, clientResponse) -> {
                        throw new BusinessException("Serviço externo de elegibilidade indisponível no momento");
                    })
                    .body(UserInfoResponse.class);

            if (response == null || response.status() == null) {
                throw new BusinessException("Resposta inválida do serviço externo de elegibilidade");
            }

            return response.isAbleToVote();
        } catch (RestClientResponseException ex) {
            log.warn("Falha ao consultar elegibilidade do associado. cpf={}, status={}, body={}",
                    sanitizedCpf, ex.getRawStatusCode(), ex.getResponseBodyAsString());
            throw new BusinessException("Falha na validação externa do associado");
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Erro inesperado ao validar associado externamente. cpf={}", sanitizedCpf, ex);
            throw new BusinessException("Não foi possível validar o associado externamente");
        }
    }

    private String sanitizeCpf(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("\\D", "");
    }
}
