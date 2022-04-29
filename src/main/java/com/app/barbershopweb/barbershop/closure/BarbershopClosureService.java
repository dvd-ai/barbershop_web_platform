package com.app.barbershopweb.barbershop.closure;

import com.app.barbershopweb.barbershop.closure.repository.JdbcBarbershopClosureRepository;
import com.app.barbershopweb.barbershop.crud.repository.BarbershopRepository;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.mailservice.MailService;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import com.app.barbershopweb.user.crud.User;
import com.app.barbershopweb.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarbershopClosureService {
    private final JdbcBarbershopClosureRepository barbershopClosureRepository;
    private final BarbershopRepository barbershopRepository;
    private final WorkspaceRepository workspaceRepository;
    private final OrderRepository orderRepository;
    private final MailService mailService;

    public BarbershopClosureService(JdbcBarbershopClosureRepository barbershopClosureRepository,
                                    BarbershopRepository barbershopRepository, OrderRepository orderRepository,
                                    WorkspaceRepository workspaceRepository, MailService mailService) {
        this.barbershopClosureRepository = barbershopClosureRepository;
        this.barbershopRepository = barbershopRepository;
        this.workspaceRepository = workspaceRepository;
        this.orderRepository = orderRepository;
        this.mailService = mailService;
    }


    public void outOfBusiness(Long barbershopId) {
        if (!barbershopRepository.barbershopExistsById(barbershopId)) {
            throw new NotFoundException(
                    List.of(
                            "Barbershop closure (out of business): No barbershop with id " + barbershopId + " found."
                    )
            );
        }

        List<User> victims = barbershopClosureRepository.getBarbershopVictimCustomers(barbershopId);

        barbershopRepository.deactivateBarbershop(barbershopId);
        workspaceRepository.deactivateWorkspacesByBarbershopId(barbershopId);
        orderRepository.deactivateOrdersByBarbershopId(barbershopId);
        mailService.notifyAboutOutOfBusiness(victims);
    }
}
