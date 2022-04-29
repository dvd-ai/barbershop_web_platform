package com.app.barbershopweb.integrationtests.container;

import com.app.barbershopweb.mailservice.pojo.ExpectedMailMetadata;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MailHogContainer extends GenericContainer<MailHogContainer> {
    private static final String IMAGE = "mailhog/mailhog:v1.0.1";
    private static final Integer SMTP_PORT = 1025;
    private static final Integer HTTP_PORT = 8025;

    private static final String MAIL_SERVICE_MESSAGES_URL = "/api/v1/messages";

    public MailHogContainer() {
        this(IMAGE);
    }

    private MailHogContainer(String dockerImageName) {
        super(dockerImageName);
        addExposedPort(SMTP_PORT);
        addExposedPort(HTTP_PORT);
        setWaitStrategy(Wait.forListeningPort());
    }

    public String getSmtpHost() {
        return getContainerIpAddress();
    }

    public Integer getSmtpPort() {
        return getMappedPort(SMTP_PORT);
    }

    public String getWebInterfaceUrl() {
        return "http://" + getContainerIpAddress() + ":" + getMappedPort(HTTP_PORT);
    }

    public void deleteAllMailHogMessages(TestRestTemplate testRestTemplate) {
        String url = getWebInterfaceUrl() + MAIL_SERVICE_MESSAGES_URL;
        testRestTemplate.delete(url, Object.class);
    }

    public String getAllMailMessages(TestRestTemplate testRestTemplate) {
        String url = getWebInterfaceUrl() + MAIL_SERVICE_MESSAGES_URL;
        return testRestTemplate.getForObject(url, String.class);
    }

    public void checkMailInbox(TestRestTemplate testRestTemplate, ExpectedMailMetadata expected) {
        String json = getAllMailMessages(testRestTemplate);
        DocumentContext context = JsonPath.parse(json);
        List<Object> object = context.read("$");

        assertEquals(1, object.size());
        assertEquals(expected.from(), context.read("$.[0].Content.Headers.From[0]"));
        assertEquals(expected.to(), context.read("$.[0].Content.Headers.To[0]"));
        assertEquals(expected.subject(), context.read("$.[0].Content.Headers.Subject[0]"));
        assertEquals(expected.body(), context.read("$.[0].Content.Body"));
    }
}
