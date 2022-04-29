package com.app.barbershopweb.barbershop.closure;

import com.app.barbershopweb.barbershop.closure.repository.JdbcBarbershopClosureRepository;
import com.app.barbershopweb.barbershop.crud.repository.BarbershopRepository;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.mailservice.MailService;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import com.app.barbershopweb.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.app.barbershopweb.barbershop.closure.constants.BarbershopClosure_ErrorMessage__TestConstants.BARBERSHOP_CLOSURE_ERR_BARBERSHOP_NOT_FOUND;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USER_VALID_ENTITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BarbershopClosureServiceTest {

    @Mock
    MailService mailService;

    @Mock
    BarbershopRepository barbershopRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    WorkspaceRepository workspaceRepository;

    @Mock
    JdbcBarbershopClosureRepository barbershopClosureRepository;

    @InjectMocks
    BarbershopClosureService barbershopClosureService;


    @Test
    void outOfBusiness_NoBarbershop() {
        when(barbershopRepository.barbershopExistsById(BARBERSHOP_VALID_BARBERSHOP_ID))
                .thenReturn(false);

        List<String> errorMessages = assertThrows(NotFoundException.class,
                () -> barbershopClosureService.outOfBusiness(BARBERSHOP_VALID_BARBERSHOP_ID)
        ).getMessages();

        assertEquals(1, errorMessages.size());
        assertEquals(BARBERSHOP_CLOSURE_ERR_BARBERSHOP_NOT_FOUND, errorMessages.get(0));
    }

    @Test
    void outOfBusiness() {
        when(barbershopRepository.barbershopExistsById(BARBERSHOP_VALID_BARBERSHOP_ID))
                .thenReturn(true);
        when(barbershopClosureRepository.getBarbershopVictimCustomers(BARBERSHOP_VALID_BARBERSHOP_ID))
                .thenReturn(List.of(USER_VALID_ENTITY));

        barbershopClosureService.outOfBusiness(BARBERSHOP_VALID_BARBERSHOP_ID);

        verify(barbershopRepository).deactivateBarbershop(BARBERSHOP_VALID_BARBERSHOP_ID);
        verify(workspaceRepository).deactivateWorkspacesByBarbershopId(BARBERSHOP_VALID_BARBERSHOP_ID);
        verify(orderRepository).deactivateOrdersByBarbershopId(BARBERSHOP_VALID_BARBERSHOP_ID);
        verify(mailService).notifyAboutOutOfBusiness(
                List.of(USER_VALID_ENTITY)
        );

    }
}