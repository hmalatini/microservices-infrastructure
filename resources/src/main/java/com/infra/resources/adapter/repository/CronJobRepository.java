package com.infra.resources.adapter.repository;

import com.infra.resources.core.domain.CronJob;
import com.infra.resources.core.domain.CronJob.CronJobId;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CronJobRepository extends CrudRepository<CronJob, CronJobId> {

    List<CronJob> findAllByMicroserviceName(String microserviceName);

    List<CronJob> findAllByMicroserviceNameAndEnvironmentName(String microserviceName, String environmentName);
}
