package com.minitextil.gestor.natureza.view;

import com.minitextil.gestor.components.grid.GridComponent;
import com.minitextil.gestor.components.grid.TipoColunas;
import com.minitextil.gestor.natureza.model.Natureza;
import com.minitextil.gestor.natureza.respository.NaturezaRepository;
import com.vaadin.flow.component.dialog.Dialog;

import java.util.List;
import java.util.function.Consumer;

public class LookupNatureza {

    public void VerTodasNatureza(NaturezaRepository naturezaRepository, Consumer<Natureza> selecionarListener,Long codEmpresa) {
        GridComponent<Natureza> grid = new GridComponent<>(Natureza.class);

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Naturezas");
        dialog.setWidth("280px");

        List<Natureza> listaNatureza = naturezaRepository.findByIdCodEmpresaOrderByIdCodNatureza(codEmpresa);

        grid.setColumnReorderingAllowed(true);
        //grid.addColunaComFiltro(natureza -> natureza.getId().getCodEmpresa(), "Código Empresa", TipoColunas.Numerico);
        grid.addColunaComFiltro(natureza -> natureza.getId().getCodNatureza(), "Código Natureza", TipoColunas.Numerico);
        grid.addColunaComFiltro(Natureza::getDescricao, "Descrição", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Natureza::getAtivo, "Ativo", TipoColunas.Boolean);


        grid.addItemDoubleClickListener(event -> {
            Natureza selecionado = event.getItem();
            if (selecionarListener != null) {
                selecionarListener.accept(selecionado);
            }
            dialog.close();
        });

        dialog.setWidth("60vw");
        dialog.setHeight("40vh");

        grid.setItensComFiltro(listaNatureza);
        dialog.add(grid);

        dialog.open();
    }
}
