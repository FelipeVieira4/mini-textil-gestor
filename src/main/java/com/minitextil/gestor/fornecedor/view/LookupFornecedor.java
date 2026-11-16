package com.minitextil.gestor.fornecedor.view;

import com.minitextil.gestor.fornecedor.model.Fornecedor;
import com.minitextil.gestor.fornecedor.repository.FornecedorRepository;
import com.minitextil.gestor.components.grid.GridComponent;
import com.minitextil.gestor.components.grid.TipoColunas;
import com.minitextil.gestor.genericos.utils.CnpjUtils;
import com.minitextil.gestor.genericos.utils.TelefoneUtils;
import com.vaadin.flow.component.dialog.Dialog;

import java.util.List;
import java.util.function.Consumer;

public class LookupFornecedor {

    public void VerTodosFornecedor(FornecedorRepository FornecedorRepository, Consumer<Fornecedor> selecionarListener) {
        GridComponent<Fornecedor> grid = new GridComponent<>(Fornecedor.class);

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Fornecedors");

        List<Fornecedor> listaFornecedors = FornecedorRepository.findAll();

        grid.addColunaComFiltro(Fornecedor::getId, "ID", TipoColunas.Numerico);
        grid.addColunaComFiltro(Fornecedor::getDescricao, "Razão Social / Nome", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Fornecedor::getSituacao, "Situação", TipoColunas.Boolean);
        grid.addColunaComFiltro(c -> CnpjUtils.formatar(c.getCnpj()), "CNPJ", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(c -> TelefoneUtils.formatar(c.getTelefone()), "Telefone", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Fornecedor::getEmail, "Email", TipoColunas.Alfanumerico);

        grid.addItemDoubleClickListener(event -> {
            Fornecedor selecionado = event.getItem();
            if (selecionarListener != null) {
                selecionarListener.accept(selecionado);
            }
            dialog.close();
        });

        dialog.setWidth("60vw");
        dialog.setHeight("40vh");

        grid.setItensComFiltro(listaFornecedors);
        dialog.add(grid);

        dialog.open();
    }
}