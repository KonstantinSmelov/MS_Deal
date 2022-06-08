package neostudy.service;

import neostudy.dao.ClientRepository;
import neostudy.entity.Client;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client getClient(Long id) {
        Client client = null;

        Optional<Client> optionalClient = clientRepository.findById(id);
        if(optionalClient.isPresent()) {
            client = optionalClient.get();
        }
        return client;
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
