package com.progra3.app.repository.postgres;

import com.progra3.app.entity.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaNodeRepository extends JpaRepository<NodeEntity, Long> {

    List<NodeEntity> findByParentIsNull();
}