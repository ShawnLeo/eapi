package com.meimeitech.eapi.repository;

import com.meimeitech.eapi.entity.DataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DataModelRepository extends JpaRepository<DataModel, String>,JpaSpecificationExecutor<DataModel> {

    List<DataModel> findByTypeOrderByName(String system);

    List<DataModel> findByTypeAndProjectIdOrderByName(String type, String projectId);
}
