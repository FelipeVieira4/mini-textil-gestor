package com.minitextil.gestor.natureza.respository;

import com.minitextil.gestor.natureza.model.Natureza;
import com.minitextil.gestor.natureza.model.NaturezaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NaturezaRepository extends JpaRepository<Natureza, NaturezaId> {
    List<Natureza> findByIdCodEmpresaOrderByIdCodNatureza(Long codEmpresa);

    @Query("select coalesce(max(n.id.codNatureza), 0) + 1 from Natureza n where n.id.codEmpresa = :codEmpresa")
    Long proximoCodigo(@Param("codEmpresa") Long codEmpresa);
}
