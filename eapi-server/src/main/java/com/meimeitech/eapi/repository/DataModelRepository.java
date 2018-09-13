package com.meimeitech.eapi.repository;

import com.meimeitech.eapi.entity.DataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DataModelRepository extends JpaRepository<DataModel, String>,JpaSpecificationExecutor<DataModel> {

    List<DataModel> findByTypeOrderByDisplayOrder(String system);

    List<DataModel> findByTypeAndProjectIdOrderByDisplayOrder(String type, String projectId);
}
