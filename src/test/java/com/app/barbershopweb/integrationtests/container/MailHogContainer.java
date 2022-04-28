package com.app.barbershopweb.integrationtests.container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class MailHogContainer extends GenericContainer<MailHogContainer> {
    private static final String IMAGE = "mailhog/mailhog:v1.0.1";
    private static final Integer SMTP_PORT = 1025;
    private static final Integer HTTP_PORT = 8025;

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

}
