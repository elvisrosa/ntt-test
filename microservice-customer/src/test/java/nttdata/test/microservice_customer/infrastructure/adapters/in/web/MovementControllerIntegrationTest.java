package nttdata.test.microservice_customer.infrastructure.adapters.in.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import nttdata.test.microservice_customer.application.ports.out.MovementClientPort;
import nttdata.test.microservice_customer.domain.models.CreateMovement;
import nttdata.test.microservice_customer.domain.models.Movement;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.response.JsonDtoResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestPropertySource(properties = {
                "BANKING_SERVICE_URL=http://localhost:8081/micro-banking/api/v1"
})
class MovementControllerIntegrationTest {

        @Autowired
        private WebTestClient webTestClient;

        @MockitoBean
        private MovementClientPort movementClientPort;

        private String testAccountNumber;
        private UUID testAccountId;

        @BeforeEach
        void setUp() {
                testAccountNumber = "ACC-TEST-" + UUID.randomUUID().toString().substring(0, 8);
                testAccountId = UUID.randomUUID();
        }

        @Test
        void createMovement_withValidData_returnsCreatedMovement() {
                CreateMovement createMovement = new CreateMovement(
                                new BigDecimal("50.00"),
                                "CREDIT",
                                "Integration test deposit");

                Movement mockMovement = new Movement(
                                UUID.randomUUID(),
                                testAccountId,
                                new BigDecimal("50.00"),
                                "Credito",
                                new BigDecimal("150.00"),
                                "Integration test deposit",
                                LocalDateTime.now());

                when(movementClientPort.registerMovement(any()))
                                .thenReturn(Mono.just(mockMovement));

                webTestClient.post()
                                .uri("/api/v1/movements/{accountNumber}", testAccountNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(createMovement)
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(JsonDtoResponse.class)
                                .value(body -> {
                                        assertThat(body).isNotNull();
                                        assertThat(body.getStatusCode()).isEqualTo(201);
                                        assertThat(body.getMessage()).isNotNull();
                                        assertThat(body.getResult()).isNotNull();
                                });
        }

        @Test
        void createMovement_withInvalidAmount_returnsBadRequest() {
                CreateMovement createMovement = new CreateMovement(
                                new BigDecimal("0"),
                                "CREDIT",
                                "Invalid amount");

                when(movementClientPort.registerMovement(any()))
                                .thenReturn(
                                                Mono.error(new IllegalArgumentException(
                                                                "El valor del movimiento debe ser mayor que cero")));
                webTestClient.post().uri("/api/v1/movements/{accountNumber}", testAccountNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(createMovement)
                                .exchange()
                                .expectStatus().isBadRequest();
        }

        @Test
        void createMovement_withNegativeAmount_returnsBadRequest() {
                CreateMovement createMovement = new CreateMovement(
                                new BigDecimal("-100.00"),
                                "CREDIT",
                                "Negative amount");

                when(movementClientPort.registerMovement(any()))
                                .thenReturn(
                                                Mono.error(new IllegalArgumentException(

                                                                "El valor del movimiento debe ser mayor que cero")));

                webTestClient.post()
                                .uri("/api/v1/movements/{accountNumber}", testAccountNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(createMovement)
                                .exchange()
                                .expectStatus().isBadRequest();
        }

        @Test
        void getMovements_byAccountNumber_returnsMovementsList() {
                List<Movement> movements = new ArrayList<>();
                movements.add(new Movement(
                                UUID.randomUUID(),
                                testAccountId,
                                new BigDecimal("100.00"),
                                "Credito",
                                new BigDecimal("1100.00"),
                                "Initial deposit",
                                LocalDateTime.now()));
                movements.add(new Movement(
                                UUID.randomUUID(),
                                testAccountId,
                                new BigDecimal("50.00"),
                                "Debito",
                                new BigDecimal("1050.00"),
                                "Purchase",
                                LocalDateTime.now()));

                when(movementClientPort.getMovementsByAccountNumber(testAccountNumber))
                                .thenReturn(Flux.fromIterable(movements));

                webTestClient.get()
                                .uri("/api/v1/movements/{accountNumber}", testAccountNumber)
                                .accept(MediaType.APPLICATION_JSON)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(JsonDtoResponse.class)
                                .value(body -> {
                                        assertThat(body).isNotNull();
                                        assertThat(body.getStatusCode()).isEqualTo(200);
                                        assertThat(body.getResult()).isNotNull();
                                });
        }
}