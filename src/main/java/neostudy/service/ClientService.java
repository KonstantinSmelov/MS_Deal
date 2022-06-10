package neostudy.service;

import neostudy.dto.LoanApplicationRequestDTO;
import neostudy.entity.Client;
import neostudy.exception.NoElementException;

public interface ClientService {
    Client getClient(Long id) throws NoElementException;
    Client initClient(LoanApplicationRequestDTO loanApplicationRequestDTO);
    void saveClient(Client client);
}
