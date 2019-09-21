package com.meimeitech.eapi.repository;

import com.meimeitech.eapi.entity.ResponseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ResponseInfoRepository extends JpaRepository<ResponseInfo, String>, JpaSpecificationExecutor<ResponseInfo> {

    @Query("select r from ResponseInfo r where r.interfaceId=?1 and r.responseIn=?2 order by r.createTime desc ")
    List<ResponseInfo> findByInterfaceIdAndResponseInOrderByCreateTimeDesc(String id, String name);

    List<ResponseInfo> findByInterfaceIdOrderByCreateTimeDesc(String id);

    List<ResponseInfo> findByInterfaceIdInOrderByCreateTimeDesc(List<String> id);
}
