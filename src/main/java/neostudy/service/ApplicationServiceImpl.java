package neostudy.service;

import neostudy.dao.ApplicationRepository;
import neostudy.entity.Application;
import neostudy.entity.Credit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Application getApplication(Long id) {

        Application application = null;

        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if(optionalApplication.isPresent()) {
            application = optionalApplication.get();
        }

        return application;
    }

    @Override
    public void saveApplication(Application application) {
        applicationRepository.save(application);
    }
}
