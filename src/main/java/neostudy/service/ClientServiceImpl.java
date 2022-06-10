package neostudy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neostudy.dao.ClientRepository;
import neostudy.dto.LoanApplicationRequestDTO;
import neostudy.entity.Client;
import neostudy.entity.Employment;
import neostudy.entity.Passport;
import neostudy.exception.NoElementException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    @Override
    public Client getClient(Long id) throws NoElementException {

        return clientRepository.findById(id).orElseThrow(() -> new NoElementException("Клиента с таким номером в БД нет"));
    }

    @Override
    public Client initClient(LoanApplicationRequestDTO loanApplicationRequestDTO) {

        Employment employment = new Employment();
        Passport passport = modelMapper.map(loanApplicationRequestDTO, Passport.class);
        Client client = modelMapper.map(loanApplicationRequestDTO, Client.class);
        client.setEmployment(employment);
        client.setPassport(passport);

        clientRepository.save(client);
        log.debug("initClient(): в БД инициализирован Client: {}", client);

        return client;
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
        log.debug("saveClient(): в БД сохранён Client: {}", client);
    }
}
