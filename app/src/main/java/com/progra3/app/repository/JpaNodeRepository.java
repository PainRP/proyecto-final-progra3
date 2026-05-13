package com.progra3.app.repository;

import com.progra3.app.entity.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaNodeRepository extends JpaRepository<NodeEntity, Long> {

    Optional<NodeEntity> findByParentIsNull();
}