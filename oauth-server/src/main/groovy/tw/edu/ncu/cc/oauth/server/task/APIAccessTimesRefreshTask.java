package tw.edu.ncu.cc.oauth.server.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tw.edu.ncu.cc.oauth.server.repository.model.ClientAccessLogRepository;

@Component
public class APIAccessTimesRefreshTask {

    @Autowired
    private ClientAccessLogRepository clientAccessLogRepository;

    @Scheduled( cron = "0 0 3 1 * ?" )
    public void run() {
        clientAccessLogRepository.resetAllClientAccessTimesPerMonth();
    }

}
