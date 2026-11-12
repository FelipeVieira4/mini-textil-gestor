package com.minitextil.gestor.item.sku.repository;

import com.minitextil.gestor.item.sku.model.ItemSku;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemSkuRepository extends JpaRepository<ItemSku, Long> {

    // Pai e variante são LAZY; a tela lê os ids fora da transação, então já trazemos junto.
    @Override
    @EntityGraph(attributePaths = {"materialPai", "variante"})
    Optional<ItemSku> findById(Long id);

    Optional<ItemSku> findByMaterialPai_IdAndVariante_Id(Long idPai, Long idVariante);

    Optional<ItemSku> findByMaterialPai_IdAndVarianteIsNull(Long idPai);

    // Consulta/lookup: as colunas mostram descrição do pai e da variante, então traz tudo
    // em uma query só (evita LazyInitializationException e N+1). Variante é opcional (left join).
    @Query("select s from ItemSku s join fetch s.materialPai left join fetch s.variante order by s.id")
    List<ItemSku> findAllComDetalhes();
}
