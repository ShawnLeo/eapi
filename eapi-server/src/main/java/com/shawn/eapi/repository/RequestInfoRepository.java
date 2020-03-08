package com.shawn.eapi.repository;

import com.shawn.eapi.entity.RequestInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RequestInfoRepository extends JpaRepository<RequestInfo, String>, JpaSpecificationExecutor<RequestInfo> {

    @Query("select r from RequestInfo r where r.interfaceId=?1 and r.paramIn=?2 order by r.createTime desc ")
    List<RequestInfo> findByInterfaceIdAndParamInOrderByCreateTimeDesc(String id, String name);

    //    @Query("select r from RequestInfo r where r.interfaceId=?1 order by r.createTime desc ")
    List<RequestInfo> findByInterfaceIdOrderByCreateTimeDesc(String id);

    List<RequestInfo> findByInterfaceIdInOrderByCreateTimeDesc(List<String> id);
}
