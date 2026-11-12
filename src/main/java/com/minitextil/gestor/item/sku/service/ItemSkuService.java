package com.minitextil.gestor.item.sku.service;

import com.minitextil.gestor.item.material.model.Material;
import com.minitextil.gestor.item.material.repository.MaterialRepository;
import com.minitextil.gestor.item.sku.model.ItemSku;
import com.minitextil.gestor.item.sku.repository.ItemSkuRepository;
import com.minitextil.gestor.item.variantes.model.Variante;
import com.minitextil.gestor.item.variantes.repository.VarianteRepository;
import com.vaadin.flow.router.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemSkuService {

    private final ItemSkuRepository itemSkuRepository;
    private final MaterialRepository materialRepository;
    private final VarianteRepository varianteRepository;

    public ItemSkuService(ItemSkuRepository itemSkuRepository,
                          MaterialRepository materialRepository,
                          VarianteRepository varianteRepository) {
        this.itemSkuRepository = itemSkuRepository;
        this.materialRepository = materialRepository;
        this.varianteRepository = varianteRepository;
    }

    public Optional<ItemSku> buscarPorId(Long idSku) {
        return itemSkuRepository.findById(idSku);
    }

    /** Todos os SKUs já com item pai e variante carregados (para consulta e lookup). */
    public List<ItemSku> listarTodos() {
        return itemSkuRepository.findAllComDetalhes();
    }

    /**
     * Cria ou atualiza um SKU.
     *
     * @param itemSku    instância carregada (edição) ou nova (cadastro)
     * @param idVariante pode ser null (SKU sem variante)
     */
    @Transactional
    public ItemSku salvarItemSku(Long idItemPai, Long idVariante, ItemSku itemSku) {

        Material materialPai = materialRepository.findById(idItemPai)
                .orElseThrow(() -> new NotFoundException("Item pai não encontrado: " + idItemPai));

        Variante variante = null;
        if (idVariante != null) {
            variante = varianteRepository.findById(idVariante)
                    .orElseThrow(() -> new NotFoundException("Variante não encontrada: " + idVariante));
        }

        // Duplicidade: a unique constraint do banco não cobre variante = NULL
        // (na maioria dos bancos NULL não conflita com NULL), então checamos aqui.
        Optional<ItemSku> existente = (idVariante == null)
                ? itemSkuRepository.findByMaterialPai_IdAndVarianteIsNull(idItemPai)
                : itemSkuRepository.findByMaterialPai_IdAndVariante_Id(idItemPai, idVariante);

        existente
                .filter(sku -> !sku.getId().equals(itemSku.getId()))
                .ifPresent(sku -> {
                    throw new IllegalArgumentException(
                            "Já existe o SKU " + sku.getId() + " para esse item pai e variante.");
                });

        itemSku.setItemPai(materialPai);
        itemSku.setVariante(variante);

        return itemSkuRepository.save(itemSku);
    }
}
