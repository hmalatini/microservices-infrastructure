package com.infra.resources.adapter.controller.cronjob;

import com.infra.resources.core.domain.CronJob;
import com.infra.resources.core.usecase.cronjob.CreateCronJob;
import com.infra.resources.core.usecase.cronjob.DeleteCronJob;
import com.infra.resources.core.usecase.cronjob.GetAllCronJobsOfService;
import com.infra.resources.core.usecase.cronjob.GetAllCronJobsOfServiceAndEnvironment;
import com.infra.resources.core.usecase.cronjob.UpdateCronJob;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backend/{microserviceName}")
@RequiredArgsConstructor
public class CronJobController {

    private final CreateCronJob createCronJob;
    private final DeleteCronJob deleteCronJob;
    private final UpdateCronJob updateCronJob;
    private final GetAllCronJobsOfService getAllCronJobsOfService;
    private final GetAllCronJobsOfServiceAndEnvironment getAllCronJobsOfServiceAndEnvironment;

    @GetMapping("/cronjob")
    public ResponseEntity<List<CronJob>> getAllOfService(@PathVariable String microserviceName) {

        List<CronJob> c = getAllCronJobsOfService.execute(microserviceName);

        return ResponseEntity.ok()
                             .body(c);
    }

    @GetMapping("{environmentName}/cronjob")
    public ResponseEntity<List<CronJob>> getAllOfServiceAndEnvironment(@PathVariable String microserviceName,
                                                                       @PathVariable String environmentName) {

        List<CronJob> c = getAllCronJobsOfServiceAndEnvironment.execute(microserviceName, environmentName);

        return ResponseEntity.ok()
                             .body(c);
    }

    @PostMapping("{environmentName}/cronjob")
    public ResponseEntity<CronJob> create(@Validated @RequestBody CronJobCreation cronJobCreation,
                                          @PathVariable String microserviceName, @PathVariable String environmentName) {

        CronJob c = createCronJob.execute(cronJobCreation, microserviceName, environmentName);

        return ResponseEntity.ok()
                             .body(c);
    }

    @PutMapping("{environmentName}/cronjob/{cronjobName}")
    public ResponseEntity<CronJob> update(@PathVariable String microserviceName, @PathVariable String environmentName,
                                          @PathVariable String cronjobName,
                                          @Validated @RequestBody CronJobUpdateDTO cronJobUpdateDTO) {

        CronJob c = updateCronJob.execute(microserviceName, environmentName, cronjobName, cronJobUpdateDTO);

        return ResponseEntity.ok()
                             .body(c);
    }

    @DeleteMapping("{environmentName}/cronjob/{cronjobName}")
    public ResponseEntity<CronJob> delete(@PathVariable String microserviceName, @PathVariable String environmentName,
                                          @PathVariable String cronjobName) {

        CronJob c = deleteCronJob.execute(microserviceName, environmentName, cronjobName);

        return ResponseEntity.ok()
                             .body(c);
    }
}
