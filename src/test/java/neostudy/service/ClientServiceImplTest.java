package neostudy.service;

import neostudy.dao.ClientRepository;
import neostudy.dto.LoanApplicationRequestDTO;
import neostudy.entity.Client;
import neostudy.entity.Employment;
import neostudy.entity.Passport;
import neostudy.exception.NoElementException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void getClient() throws NoElementException {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.ofNullable(Client.builder().email("test@test.com").build()));

        Client client = clientService.getClient(1L);

        assertEquals("test@test.com", client.getEmail());
    }

    @Test
    void initClient() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder().build();

        when(modelMapper.map(loanApplicationRequestDTO, Passport.class)).thenReturn(Passport.builder().build());
        when(modelMapper.map(loanApplicationRequestDTO, Client.class)).thenReturn(Client.builder().build());

        clientService.initClient(loanApplicationRequestDTO);

        verify(modelMapper, times(1)).map(loanApplicationRequestDTO, Passport.class);
        verify(modelMapper, times(1)).map(loanApplicationRequestDTO, Client.class);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void saveClient() {

        clientService.saveClient(Client.builder().build());

        verify(clientRepository, times(1)).save(any(Client.class));
    }
}