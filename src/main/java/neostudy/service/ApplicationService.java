package neostudy.service;

import neostudy.entity.Application;

public interface ApplicationService {
    Application getApplication(Long id);
    void saveApplication(Application application);
}
