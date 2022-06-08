package neostudy.service;

import neostudy.entity.Client;

public interface ClientService {
    Client getClient(Long id);
    void saveClient(Client client);
}
