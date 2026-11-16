package com.minitextil.gestor.cliente.view;

import com.minitextil.gestor.cliente.model.Cliente;
import com.minitextil.gestor.cliente.repository.ClienteRepository;
import com.minitextil.gestor.components.core.program.ProgramErp;
import com.minitextil.gestor.components.grid.GridComponent;
import com.minitextil.gestor.components.grid.TipoColunas;
import com.minitextil.gestor.genericos.utils.CnpjUtils;
import com.minitextil.gestor.genericos.utils.TelefoneUtils;
import com.vaadin.flow.component.Component;

import java.util.List;

public class ConsultaCliente extends ProgramErp {
    private final GridComponent<Cliente> grid = new GridComponent<>(Cliente.class);
    private final ClienteRepository repository;

    public ConsultaCliente(ClienteRepository repository) {
        this.repository = repository;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        setCabecalho("Consulta de Clientes");

        configurarGrid();
        add(grid);

        carregarClientes();
    }

    private void configurarGrid() {
        grid.addColunaComFiltro(Cliente::getId, "ID", TipoColunas.Numerico);
        grid.addColunaComFiltro(Cliente::getDescricao, "Razão Social / Nome", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Cliente::getSituacao, "Situação", TipoColunas.Boolean);
        grid.addColunaComFiltro(c -> CnpjUtils.formatar(c.getCnpj()), "CNPJ", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(c -> TelefoneUtils.formatar(c.getTelefone()), "Telefone", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Cliente::getEmail, "Email", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(cliente -> cliente.getEndereco().getUf(), "UF", TipoColunas.Alfanumerico);
    }

    private void carregarClientes() {
        List<Cliente> listaClientes = repository.findAll();
        grid.setItensComFiltro(listaClientes);
    }

    @Override
    public Component getView() {
        return this;
    }
}