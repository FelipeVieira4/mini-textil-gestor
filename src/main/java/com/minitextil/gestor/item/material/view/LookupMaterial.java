package com.minitextil.gestor.item.material.view;

import com.minitextil.gestor.components.grid.GridComponent;
import com.minitextil.gestor.components.grid.TipoColunas;
import com.minitextil.gestor.item.material.model.Material;
import com.minitextil.gestor.item.variantes.model.Variante;
import com.minitextil.gestor.item.material.repository.MaterialRepository;
import com.minitextil.gestor.item.variantes.repository.VarianteRepository;
import com.vaadin.flow.component.dialog.Dialog;

import java.util.List;
import java.util.function.Consumer;

public class LookupMaterial {

    public void VerTodosItens(MaterialRepository materialRepository, Consumer<Material> selecionarListener) {
        GridComponent<Material> grid = new GridComponent<>(Material.class);

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Itens");
        dialog.setWidth("280px");

        List<Material> listaOperadores = materialRepository.findAll();

        grid.setColumnReorderingAllowed(true);
        grid.addColunaComFiltro(Material::getId, "ID", TipoColunas.Numerico);
        grid.addColunaComFiltro(Material::getDescricao, "Descrição", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Material::getUnidade, "Unidade", TipoColunas.Alfanumerico);


        grid.addItemDoubleClickListener(event -> {
            Material materialSelecionado = event.getItem();
            if (selecionarListener != null) {
                selecionarListener.accept(materialSelecionado);
            }
            dialog.close();
        });

        dialog.setWidth("60vw");
        dialog.setHeight("40vh");

        grid.setItensComFiltro(listaOperadores);
        dialog.add(grid);

        dialog.open();
    }


    public void VerTodasVariantes(VarianteRepository varianteRepositoryRepository, Consumer<Variante> selecionarListener) {
        GridComponent<Variante> grid = new GridComponent<>(Variante.class);

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Variantes");
        dialog.setWidth("280px");

        List<Variante> listaVariantes = varianteRepositoryRepository.findAll();

        grid.setColumnReorderingAllowed(true);
        grid.addColunaComFiltro(Variante::getId, "ID", TipoColunas.Numerico);
        grid.addColunaComFiltro(Variante::getDescricao, "Descrição", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Variante::isAtivo, "Situação", TipoColunas.Boolean);
        grid.addColunaComFiltro(Variante::getHexaCor, "Cor Variante", TipoColunas.Alfanumerico);

        grid.addItemDoubleClickListener(event -> {
            Variante varianteSelecionada = event.getItem();
            if (selecionarListener != null) {
                selecionarListener.accept(varianteSelecionada);
            }
            dialog.close();
        });

        dialog.setWidth("60vw");
        dialog.setHeight("40vh");

        grid.setItensComFiltro(listaVariantes);
        dialog.add(grid);

        dialog.open();
    }
}