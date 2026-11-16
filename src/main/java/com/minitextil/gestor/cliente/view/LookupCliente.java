package com.minitextil.gestor.cliente.view;

import java.util.List;
import java.util.function.Consumer;

import com.minitextil.gestor.cliente.model.Cliente;
import com.minitextil.gestor.cliente.repository.ClienteRepository;
import com.minitextil.gestor.components.grid.GridComponent;
import com.minitextil.gestor.components.grid.TipoColunas;
import com.minitextil.gestor.genericos.utils.CnpjUtils;
import com.minitextil.gestor.genericos.utils.TelefoneUtils;
import com.vaadin.flow.component.dialog.Dialog;

public class LookupCliente {

    public void VerTodosCliente(ClienteRepository clienteRepository, Consumer<Cliente> selecionarListener) {
        GridComponent<Cliente> grid = new GridComponent<>(Cliente.class);

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Clientes");

        List<Cliente> listaClientes = clienteRepository.findAll();

        grid.addColunaComFiltro(Cliente::getId, "ID", TipoColunas.Numerico);
        grid.addColunaComFiltro(Cliente::getDescricao, "Razão Social / Nome", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Cliente::getSituacao, "Situação", TipoColunas.Boolean);
        grid.addColunaComFiltro(c -> CnpjUtils.formatar(c.getCnpj()), "CNPJ", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(c -> TelefoneUtils.formatar(c.getTelefone()), "Telefone", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Cliente::getEmail, "Email", TipoColunas.Alfanumerico);

        grid.addItemDoubleClickListener(event -> {
            Cliente selecionado = event.getItem();
            if (selecionarListener != null) {
                selecionarListener.accept(selecionado);
            }
            dialog.close();
        });

        dialog.setWidth("60vw");
        dialog.setHeight("40vh");

        grid.setItensComFiltro(listaClientes);
        dialog.add(grid);

        dialog.open();
    }
}