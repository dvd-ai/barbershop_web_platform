package com.app.barbershopweb.mailservice.constants.outofbusiness;

import static com.app.barbershopweb.order.reservation.constants.list.fk.OrderReservation_FkEntityList__TestConstants.ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST;
import static com.app.barbershopweb.order.reservation.constants.list.fk.OrderReservation_FkEntityList__TestConstants.ORDER_RESERVATION_FK_USER_ENTITY_LIST;

public final class MailService_Metadata_OutOfBusiness__TestConstants {
    public static final String MAIL_OUT_OF_BUSINESS_NO_REPLY_PREFIX = "no-reply@";
    public static final String MAIL_OUT_OF_BUSINESS_EMAIL_FROM = MAIL_OUT_OF_BUSINESS_NO_REPLY_PREFIX +
            ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST.get(0).getEmail().split("@")[1];

    public static final String MAIL_OUT_OF_BUSINESS_EMAIL_TO =
            "Dear " + ORDER_RESERVATION_FK_USER_ENTITY_LIST.get(0).getFirstName() +
                    " " + ORDER_RESERVATION_FK_USER_ENTITY_LIST.get(0).getLastName() +
                    ", your ...";
    public static final String MAIL_OUT_OF_BUSINESS_SUBJECT = "Your appointments canceled because barbershop is out of business";
}
